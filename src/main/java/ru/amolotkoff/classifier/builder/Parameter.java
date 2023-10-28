package ru.amolotkoff.classifier.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Parameter implements ru.amolotkoff.classifier.builder.Builder {

    private java.lang.Class type;
    private String          name;

    @Override
    public String get() {
        return String.format("%s %s", type.getSimpleName(), name);
    }
}