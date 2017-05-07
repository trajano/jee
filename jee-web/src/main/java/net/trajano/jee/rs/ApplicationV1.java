package net.trajano.jee.rs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/v1/*")
public class ApplicationV1 extends Application {

    /**
     * This is implemented to prevent JAX-RS from just implicitly scanning the
     * paths. This allows restricting what gets released in each version of the
     * JAX-RS API.
     */
    @Override
    public Set<Class<?>> getClasses() {

        return new HashSet<>(Arrays.asList(new Class<?>[] {
            Participants.class
        }));
    }
}
