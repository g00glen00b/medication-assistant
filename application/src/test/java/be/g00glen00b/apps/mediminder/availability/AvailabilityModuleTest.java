package be.g00glen00b.apps.mediminder.availability;

import be.g00glen00b.apps.mediminder.medication.MedicationFacade;
import be.g00glen00b.apps.mediminder.notification.NotificationFacade;
import be.g00glen00b.apps.mediminder.user.UserFacade;
import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ModuleTest
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:14.5-alpine3.16:///mediminder"
})
public class AvailabilityModuleTest {
    @MockBean
    private MedicationFacade medicationFacade;
    @MockBean
    private UserFacade userFacade;
    @MockBean
    private NotificationFacade notificationFacade;

    @Test
    void moduleLoads() {

    }
}
