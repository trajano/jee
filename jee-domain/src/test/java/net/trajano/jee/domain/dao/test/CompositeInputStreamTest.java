package net.trajano.jee.domain.dao.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import net.trajano.jee.domain.dao.impl.CompositeInputStream;

public class CompositeInputStreamTest {

    public static long copyLarge(final InputStream input,
        final OutputStream output,
        final byte[] buffer)
        throws IOException {

        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    @Test
    public void testBuilder() throws Exception {

        final ByteArrayInputStream hello = new ByteArrayInputStream("Hello".getBytes(Charset.defaultCharset()));
        final ByteArrayInputStream world = new ByteArrayInputStream("World".getBytes(Charset.defaultCharset()));

        final InputStream is = new CompositeInputStream.Builder().addStream(hello).addStream(world).build();
        final byte[] buf = new byte[10];
        new DataInputStream(is).readFully(buf);
        assertEquals("HelloWorld", new String(buf));

    }

    @Test
    public void testCopyLarge() throws Exception {

        final ByteArrayInputStream hello = new ByteArrayInputStream("Hello".getBytes(Charset.defaultCharset()));
        final ByteArrayInputStream world = new ByteArrayInputStream("World".getBytes(Charset.defaultCharset()));

        final InputStream is = new CompositeInputStream.Builder()
            .addStream(hello)
            .addStream(world)
            .addStream(new ByteArrayInputStream(new byte[0]))
            .build();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copyLarge(is, baos, new byte[205]);
        assertEquals("HelloWorld", new String(baos.toByteArray()));

    }
}
