package org.luvx.common.enums.ext;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.luvx.common.enums.EnumHasName;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Getter
public class EnumNameSerializer extends StdSerializer<Integer> implements ContextualSerializer {
    private EnumNameAnno enumNameAnno;

    public EnumNameSerializer() {
        super(Integer.class);
    }

    public EnumNameSerializer(EnumNameAnno enumNameAnno) {
        super(Integer.class);
        this.enumNameAnno = enumNameAnno;
    }

    @Override
    public void serialize(Integer value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(value);
        if (ObjectUtils.anyNull(value, enumNameAnno)) {
            return;
        }
        Class<? extends EnumHasName<?, ?>> clazz = enumNameAnno.value();
        Object name = Arrays.stream(clazz.getEnumConstants())
                .filter(e -> Objects.equals(e.getCode(), value))
                .findFirst()
                .map(EnumHasName::getName)
                .orElse(null);
        String fieldName = jsonGenerator.getOutputContext().getCurrentName() + "Name";
        String value1 = name == null ? "" : String.valueOf(name);
        jsonGenerator.writeStringField(fieldName, value1);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        EnumNameAnno annotation = property.getAnnotation(EnumNameAnno.class);
        return new EnumNameSerializer(annotation);
    }
}
