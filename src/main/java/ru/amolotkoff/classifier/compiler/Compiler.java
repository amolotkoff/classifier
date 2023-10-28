package ru.amolotkoff.classifier.compiler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.commons.io.FilenameUtils;
import ru.amolotkoff.classifier.builder.Class;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Compiler {

    @Value("${compiler.libs.path}")
    private String libsPath;

    private final JavaCompiler compiler;
    private final DiagnosticCollector<JavaFileObject> diagnostics;
    private final InMemoryFileManager manager;
    private final List<String> options;

    public Compiler() throws Exception {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.diagnostics = new DiagnosticCollector<>();
        this.manager = new InMemoryFileManager(compiler.getStandardFileManager(null, null, null));
        this.options = exportLibs();
    }

    private List<String> exportLibs() throws IOException {
        Path path = Paths.get(libsPath);

        if (!Files.exists(path))
            return null;

        List<String> options = new ArrayList<>();
        options.add("-classpath");

        StringBuilder libs = new StringBuilder();
        libs.append(".");

        Files.find(path, Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile() &&
                                                                    FilenameUtils.getExtension(filePath.toString())
                                                                                 .equals("jar"))
             .forEach((file) -> {
                 if (libs.length() > 1)
                     libs.append(";");

                 libs.append(file.toString());
             });

        options.add(libs.toString());

        return options;
    }

    private String qualifiedName(Class clazz) {
        return clazz.getFromPackage() + '.' + clazz.getName();
    }

    public java.lang.Class Compile(Class clazz) throws Exception {
        JavaFileObject sourceFile = new JavaSourceFromString(qualifiedName(clazz), clazz.get());
        List<JavaFileObject> sourceFiles = new ArrayList<>();

        sourceFiles.add(sourceFile);
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, options, null, sourceFiles);

        boolean result = task.call();

        if (!result) {
            diagnostics.getDiagnostics()
                    .forEach(d -> {
                        // TO-DO logs
                    });

            throw new Exception();
        } else {
            ClassLoader classLoader = manager.getClassLoader(null);
            return classLoader.loadClass(qualifiedName(clazz));
        }
    }
}