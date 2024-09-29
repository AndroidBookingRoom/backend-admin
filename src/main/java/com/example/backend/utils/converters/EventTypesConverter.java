package com.example.backend.utils.converters;

import com.example.backend.utils.enums.EventTypes;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EventTypesConverter implements AttributeConverter<EventTypes, String> {

    @Override
    public String convertToDatabaseColumn(EventTypes eventTypes) {
        return eventTypes.name();
    }

    @Override
    public EventTypes convertToEntityAttribute(String s) {
        return EventTypes.valueOf(s);
    }
}
