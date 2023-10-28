package ru.amolotkoff.classifier.builder;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Field implements ru.amolotkoff.classifier.builder.Builder {
    private String          name;
    private java.lang.Class type;

    @Getter
    private FieldValue value;

    @Override
    public String get() {
        return String.format("private %s %s", type.getSimpleName(), name);
    }
}