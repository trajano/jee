package net.trajano.jee.domain.dao;

import javax.ejb.ApplicationException;
import javax.validation.ValidationException;

@ApplicationException
public class DuplicateSinException extends ValidationException {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 7797343376699439504L;

}
