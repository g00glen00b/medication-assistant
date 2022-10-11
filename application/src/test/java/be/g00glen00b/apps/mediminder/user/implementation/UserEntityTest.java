package be.g00glen00b.apps.mediminder.user.implementation;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {
    @Test
    void of_returnsResult() {
        UserEntity entity = UserEntity.of("me@example.org", "Štefan Hallsteinn", "password", ZoneId.of("Australia/Brisbane"));
        assertThat(entity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new UserEntity(
                null,
                "me@example.org",
                "Štefan Hallsteinn",
                "password",
                ZoneId.of("Australia/Brisbane"),
                null,
                null
            ));
        assertThat(entity.getId()).isNotNull();
    }
}