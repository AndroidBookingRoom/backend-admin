package com.example.backend.utils.converters;

import com.example.backend.utils.enums.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {
    @Override
    public String convertToDatabaseColumn(RoleType roleType) {
        return roleType.name();
    }

    @Override
    public RoleType convertToEntityAttribute(String s) {
        return RoleType.valueOf(s);
    }
}
