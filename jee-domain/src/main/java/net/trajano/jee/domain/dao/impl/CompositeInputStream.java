package net.trajano.jee.domain.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * THis is an input stream comprised of multiple input streams.
 *
 * @author Archimedes Trajano
 */
public class CompositeInputStream extends InputStream {

    public static class Builder {

        private final List<InputStream> streams = new LinkedList<>();

        public Builder addStream(final InputStream is) {

            streams.add(is);
            return this;
        }

        public CompositeInputStream build() {

            return new CompositeInputStream(streams);
        }
    }

    private InputStream current;

    private final Iterator<InputStream> iterator;

    private CompositeInputStream(final Iterable<InputStream> inputStreams) {
        iterator = inputStreams.iterator();
        current = iterator.next();
    }

    @Override
    public int read() throws IOException {

        for (;;) {
            final int c = current.read();
            if (c == -1 && iterator.hasNext()) {
                current = iterator.next();
            } else if (c != -1) {
                return c;
            } else {
                break;
            }
        }
        return -1;
    }

}
