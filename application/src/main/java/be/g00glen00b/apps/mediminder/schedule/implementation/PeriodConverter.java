package be.g00glen00b.apps.mediminder.schedule.implementation;

import javax.persistence.AttributeConverter;
import java.time.Period;

public class PeriodConverter implements AttributeConverter<Period, String> {
    @Override
    public String convertToDatabaseColumn(Period attribute) {
        return attribute.toString();
    }

    @Override
    public Period convertToEntityAttribute(String dbData) {
        return Period.parse(dbData);
    }
}
