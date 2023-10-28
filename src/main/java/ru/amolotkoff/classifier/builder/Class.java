package ru.amolotkoff.classifier.builder;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
public class Class implements ru.amolotkoff.classifier.builder.Builder {

    @Getter
    private String name, fromPackage;

    @Singular
    private String[] imports;
    @Singular
    private Field[] fields;
    @Singular
    private Signature[] methods;
    @Singular
    private Constructor[] constructors;

    @Override
    public String get() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("package %s;\n", fromPackage));

        for(String _import : imports)
            builder.append(String.format("import %s;\n", _import));

        builder.append(String.format("public class %s {\n", name));

        for(Field field : fields)
            builder.append(String.format("%s;\n", field.get()));

        for(Constructor constructor : constructors)
            builder.append(String.format("%s\n", String.format(constructor.get(), name)));

        for(Signature signature : methods)
            builder.append(String.format("%s\n", signature.get()));

        builder.append("}");

        return builder.toString();
    }
}