package net.trajano.jee.domain.converter;

import javax.persistence.Converter;

import net.trajano.jee.domain.entity.Gender;

@Converter(autoApply = true)
class GenderConverter extends EnumToStringConverter<Gender> {

    protected GenderConverter() {
        super(Gender.class);
    }

}
