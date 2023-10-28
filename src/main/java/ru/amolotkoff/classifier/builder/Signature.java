package ru.amolotkoff.classifier.builder;

import lombok.Builder;
import lombok.Singular;

@Builder
public class Signature implements ru.amolotkoff.classifier.builder.Builder {

    @Singular
    private Parameter[]     parameter;
    private String          name;
    private java.lang.Class type;
    private Segment         segment;

    @Override
    public String get() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("public %s %s(", type.getSimpleName(), name));

        int i = 0;
        for (Parameter _parameter : parameter) {

            if (i > 0)
                builder.append(", ");

            builder.append(_parameter.get());

            i++;
        }

        builder.append(String.format(") {\n%s\n}\n", segment.get()));
        return builder.toString();
    }
}