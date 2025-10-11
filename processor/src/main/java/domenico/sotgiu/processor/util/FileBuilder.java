package domenico.sotgiu.processor.util;

import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Optional;
import java.util.function.BiConsumer;

public interface FileBuilder extends BiConsumer<TypeSpec, String> {
    static FileBuilder of(Filer filer, String packageName) {
        return (typeSpec, fileName) -> {
            var javaFile = JavaFile.builder(packageName, typeSpec).addStaticImport(Optional.class, "ofNullable").build();
            try {
                var builderFile = filer.createSourceFile(fileName);
                try (var writer = builderFile.openWriter()) {
                    javaFile.writeTo(writer);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}

