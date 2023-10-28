package ru.amolotkoff.classifier.builder;

import lombok.Builder;
import lombok.Singular;

@Builder
public class Constructor implements ru.amolotkoff.classifier.builder.Builder {

    @Singular
    private Parameter[] parameters;
    private Segment     segment;

    @Override
    public String get() {
        StringBuilder builder = new StringBuilder();
        builder.append('(');

        int i = 0;
        for (Parameter _parameter : parameters) {

            if (i > 0)
                builder.append(", ");

            builder.append(_parameter.get());

            i++;
        }

        builder.append(String.format(") {\n%s\n}\n", segment.get()));


        builder.insert(0, "public %s ");
        return builder.toString();
    }
}