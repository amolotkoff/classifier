package ru.amolotkoff.classifier.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class FieldValueTermination implements FieldValue {

    private Object value;

    @Override
    public Object get() {
        return value;
    }
}
