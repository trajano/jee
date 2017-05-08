package net.trajano.jee.bean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import net.trajano.jee.domain.entity.Gender;

@Named
@ApplicationScoped
public class EnumBean {

    public Gender[] getGender() {

        return Gender.values();
    }
}