package net.trajano.jee.domain.converter;

import javax.persistence.Converter;

import net.trajano.jee.domain.entity.Gender;

@Converter(autoApply = true)
public class GenderConverter extends EnumToStringConverter<Gender> {

    public GenderConverter() {
        super(Gender.class);
    }

}
