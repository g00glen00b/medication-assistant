package be.g00glen00b.apps.mediminder.user.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        return http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/user/email/exists").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/user/timezone").permitAll()
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/actuators/loggers/**", "/actuator/heapdump").hasAnyRole("admin")
                .requestMatchers("/webjars/**").permitAll()
                .requestMatchers("/static/swagger-ui/").permitAll())
            .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(new FormBasedBasicAuthenticationEntryPoint()))
            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"))
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .build();
        // @formatter:on
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
