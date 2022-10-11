package be.g00glen00b.apps.mediminder.notification.implementation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:14.5-alpine3.16:///mediminder"
})
class NotificationEntityRepositoryTest {
    private static final UUID USER_1_ID = UUID.fromString("0a808223-9fd3-425e-9953-bbadbf44a79d");
    private static final UUID USER_2_ID = UUID.fromString("2b5ecbc9-7665-4263-9b75-16bc3f05e352");
    private static final UUID NOTIFICATION_1_ID = UUID.fromString("2b338afc-1ec9-48c9-9fca-f4aa046ecd51");
    @Autowired
    private NotificationEntityRepository repository;

    @Test
    @Sql("classpath:notifications.sql")
    void findAllActiveByUserId() {
        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<NotificationEntity> results = repository.findAllActiveByUserId(USER_1_ID, pageRequest);
        assertThat(results)
            .hasSize(1)
            .extracting(NotificationEntity::getId)
            .containsOnly(NOTIFICATION_1_ID);
    }

    @Test
    @Sql("classpath:notifications.sql")
    void findByUserIdAndId() {
        Optional<NotificationEntity> result = repository.findByUserIdAndId(USER_1_ID, NOTIFICATION_1_ID);
        assertThat(result)
            .isNotEmpty()
            .get()
            .extracting(NotificationEntity::getId)
            .isEqualTo(NOTIFICATION_1_ID);
    }

    @Test
    @Sql("classpath:notifications.sql")
    void findByUserIdAndId_returnsNothingIfIdsDontMatch() {
        Optional<NotificationEntity> result = repository.findByUserIdAndId(USER_2_ID, NOTIFICATION_1_ID);
        assertThat(result).isEmpty();
    }

    @Test
    @Sql("classpath:notifications.sql")
    void findByUserIdAndReference() {
        Optional<NotificationEntity> result = repository.findByUserIdAndReference(USER_1_ID, "REF-1");
        assertThat(result)
            .isNotEmpty()
            .get()
            .extracting(NotificationEntity::getId)
            .isEqualTo(NOTIFICATION_1_ID);
    }

    @Test
    @Sql("classpath:notifications.sql")
    void findByUserIdAndReference_returnsNothingIfReferencesDontMatch() {
        Optional<NotificationEntity> result = repository.findByUserIdAndReference(USER_2_ID, "REF-2");
        assertThat(result).isEmpty();
    }
}