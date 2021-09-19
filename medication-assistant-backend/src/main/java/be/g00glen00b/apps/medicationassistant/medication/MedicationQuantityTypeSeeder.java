package be.g00glen00b.apps.medicationassistant.medication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MedicationQuantityTypeSeeder implements ApplicationRunner {
    private final MedicationProperties properties;
    private final MedicationQuantityTypeFacade facade;

    @Override
    public void run(ApplicationArguments args) {
        log.debug("Seeder initialized, creating {} types", properties.getQuantity().getNames().size());
        properties
            .getQuantity()
            .getNames()
            .stream()
            .map(CreateMedicationQuantityTypeRequestDTO::new)
            .forEach(facade::findOrCreate);
    }
}
