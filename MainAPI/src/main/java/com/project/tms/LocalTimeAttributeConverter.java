package com.project.tms;

import jakarta.persistence.AttributeConverter;

import java.sql.Time;
import java.time.LocalTime;

public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, Time> {

    @Override
    public Time convertToDatabaseColumn(LocalTime localTime) {
        return localTime == null ? null : Time.valueOf(localTime);
    }

    @Override
    public LocalTime convertToEntityAttribute(Time sqlTime) {
        return sqlTime == null ? null : sqlTime.toLocalTime();
    }
}
