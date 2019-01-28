package com.afesguerra.sonarqube.plugin.sns.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Optional;

public class OptionalSerializer extends StdSerializer<Optional> {

    public OptionalSerializer() {
        this(null);
    }

    private OptionalSerializer(Class<Optional> t) {
        super(t);
    }

    @Override
    public void serialize(Optional value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value.isPresent()) {
            gen.writeObject(value.get());
        }
    }
}
