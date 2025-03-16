package domenico.sotgiu.processor;

import com.palantir.javapoet.*;
import domenico.sotgiu.core.FileBuilder;
import domenico.sotgiu.core.FileMapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Map;

public class GenerateFileBuilder {

    private static ParameterSpec getSupplierParameter(TypeName annotatedElementTypeName) {

        return ParameterSpec.builder(
                ParameterizedTypeName.get(ClassName.get("java.util.function", "Supplier"),
                        ParameterizedTypeName.get(ClassName.get("java.util.stream", "Stream"),
                                annotatedElementTypeName)), "supplier").build();

    }


    static JavaFile generate(Element annotatedElement, String packageName, String[] headers) {
            var annotatedElementTypeName = TypeName.get(annotatedElement.asType());
            var buildMethod = MethodSpec.methodBuilder("build")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC).returns(void.class)
                    .addParameter(ParameterSpec.builder(ClassName.get("java.nio.file", "Path"), "path").build())
                    .addParameter(getSupplierParameter(annotatedElementTypeName))
                    .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(Map.class, String.class, String.class), "headersData").build())
                    .addException(IOException.class)
                    .addStatement("build(HEADERS, MAPPER, path, supplier, headersData)")
                    .build();

            var builder = TypeSpec.classBuilder(annotatedElement.getSimpleName() + "FileBuilder").superclass(
                            ParameterizedTypeName.get(ClassName.get(FileBuilder.class), annotatedElementTypeName))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addField(FieldSpec.builder(String.class, "HEADERS")
                            .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                            .initializer("$S", String.join(",", headers))
                            .build())
                    .addField(
                            FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(FileMapper.class), annotatedElementTypeName), "MAPPER")
                                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                                    .initializer("new $T()",
                                            ClassName.get("", String.format("%s.%sFileMapper",packageName, annotatedElement.getSimpleName())))
                                    .build())
                    .addMethod(buildMethod)
                    .build();

            return JavaFile.builder(packageName, builder).build();

    }


}
