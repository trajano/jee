package net.trajano.jee.domain.entity;

import javax.persistence.Converter;

@Converter(autoApply = true)
class GenderConverter extends EnumToStringConverter<Gender> {

    protected GenderConverter() {
        super(Gender.class);
    }

}
