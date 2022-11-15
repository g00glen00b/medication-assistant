package be.g00glen00b.apps.mediminder;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.docs.Documenter;
import org.springframework.modulith.model.ApplicationModules;

class MediminderApplicationTest {
    @Test
    void verifyModularity() {
        ApplicationModules modules = ApplicationModules.of(MediminderApplication.class);
        modules.verify();
        new Documenter(modules)
            .writeModulesAsPlantUml()
            .writeIndividualModulesAsPlantUml();
    }
}