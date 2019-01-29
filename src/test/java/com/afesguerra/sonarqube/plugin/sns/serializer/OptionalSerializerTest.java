package com.afesguerra.sonarqube.plugin.sns.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OptionalSerializerTest {

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider serializerProvider;

    private StdSerializer<Optional> serializer;

    @Before
    public void setUp() {
        serializer = new OptionalSerializer();
    }

    @Test
    public void testSerializeNoValue() throws Exception {
        Optional<Integer> optional = Optional.empty();
        serializer.serialize(optional, gen, serializerProvider);

        verify(gen, never()).writeObject(any());
    }

    @Test
    public void testWithValue() throws Exception {
        Optional<Integer> optional = Optional.of(1);
        serializer.serialize(optional, gen, serializerProvider);

        verify(gen).writeObject(optional.get());
    }
}
