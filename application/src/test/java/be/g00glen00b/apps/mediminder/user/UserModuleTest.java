package be.g00glen00b.apps.mediminder.user;

import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ModuleTest
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:14.5-alpine3.16:///mediminder"
})
public class UserModuleTest {
    @Test
    void moduleLoads() {

    }
}
