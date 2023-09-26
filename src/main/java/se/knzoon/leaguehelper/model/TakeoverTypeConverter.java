package se.knzoon.leaguehelper.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class TakeoverTypeConverter implements AttributeConverter<TakeoverType, String> {
    @Override
    public String convertToDatabaseColumn(TakeoverType takeoverType) {
        if (takeoverType == null) {
            return null;
        }
        return takeoverType.getType();
    }

    @Override
    public TakeoverType convertToEntityAttribute(String type) {
        if (type == null) {
            return null;
        }
        return Stream.of(TakeoverType.values()).filter(t -> t.getType().equals(type)).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
