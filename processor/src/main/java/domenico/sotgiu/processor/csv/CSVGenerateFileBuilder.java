package domenico.sotgiu.processor.csv;

import com.palantir.javapoet.*;
import domenico.sotgiu.annotation.FileHeader;
import domenico.sotgiu.runtime.FileBuilder;
import domenico.sotgiu.runtime.FileMapper;
import domenico.sotgiu.runtime.util.CSVEscapeCharacters;
import domenico.sotgiu.processor.util.GenerateFile;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVGenerateFileBuilder extends GenerateFile<String[]> {
    public CSVGenerateFileBuilder(Element annotatedElement) {
        super(annotatedElement);
    }

    private static ParameterSpec getSupplierParameter(TypeName annotatedElementTypeName) {

        return ParameterSpec.builder(
                ParameterizedTypeName.get(ClassName.get("java.util.function", "Supplier"),
                        ParameterizedTypeName.get(ClassName.get("java.util.stream", "Stream"),
                                annotatedElementTypeName)), "supplier").build();

    }


    @Override
    public TypeSpec generate(String[] headers) {

        var annotatedElementTypeName = TypeName.get(annotatedElement.asType());
        var separator = annotatedElement.getAnnotation(FileHeader.class).separator();
        var buildMethod = MethodSpec.methodBuilder("build")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC).returns(void.class)
                .addParameter(ParameterSpec.builder(ClassName.get("java.nio.file", "Path"), "path").build())
                .addParameter(getSupplierParameter(annotatedElementTypeName))
                .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(Map.class, String.class, String.class), "headersData").build())
                .addException(IOException.class)
                .addStatement("""
                        var escape = $2T.of($4S);
                        var escapedHeaders = $1T.stream(HEADERS)
                                            .map(mapperFunction.apply(headersData))
                                            .map(escape)
                                            .collect($3T.joining($4S))""",Arrays.class,CSVEscapeCharacters.class, Collectors.class, separator)
                .addStatement("build(escapedHeaders, mapper, path, supplier)")
                .build();

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(FileMapper.class),
                        annotatedElementTypeName), "mapper").addModifiers(Modifier.FINAL).build())
                .addStatement("this.$N = $N", "mapper", "mapper")
                .build();
        var escape= CSVEscapeCharacters.of(separator);
        return TypeSpec.classBuilder(annotatedElement.getSimpleName() + "FileBuilder").superclass(
                        ParameterizedTypeName.get(ClassName.get(FileBuilder.class), annotatedElementTypeName))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(FieldSpec.builder(String[].class, "HEADERS")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                        .initializer("$L", "new String[]{\"" + String.join("\",\"", Arrays.stream(headers)
                                .map(escape).toArray(String[]::new)) + "\"}")
                        .build())
                .addField(FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(FileMapper.class), annotatedElementTypeName), "mapper")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build())
                .addMethod(constructor)
                .addMethod(buildMethod)
                .build();

    }


}
