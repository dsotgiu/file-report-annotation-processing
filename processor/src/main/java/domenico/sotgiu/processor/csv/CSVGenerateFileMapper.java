package domenico.sotgiu.processor.csv;

import com.palantir.javapoet.*;
import domenico.sotgiu.annotation.FileHeader;
import domenico.sotgiu.runtime.FileMapper;
import domenico.sotgiu.runtime.util.CSVEscapeCharacters;
import domenico.sotgiu.processor.util.GenerateFile;
import domenico.sotgiu.processor.util.TypeMapping;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class CSVGenerateFileMapper extends GenerateFile<TypeMapping[]> {

    public CSVGenerateFileMapper(Element annotatedElement) {
        super(annotatedElement);
    }

    private final Function<MethodSpec.Builder, Function<TypeMapping[], IntConsumer>> buildMethod =
            builder -> head -> i -> Optional.of(head[i]).ifPresent(el->
                    builder.addStatement("row[$L] = ESCAPE_CHARACTERS.apply(e.$L())", i, el.field()));


    public TypeSpec generate(TypeMapping[] fields) {

        var fileMapperClassName = annotatedElement.getSimpleName() + "FileMapper";
        var separator = annotatedElement.getAnnotation(FileHeader.class).separator();

        var annotatedElementTypeName = TypeName.get(annotatedElement.asType());
        var applyMethodBuilder = MethodSpec.methodBuilder("apply")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC).returns(String.class)
                .addParameter(annotatedElementTypeName, "e")
                .addStatement("var row = ROW.clone()");

        var consumer = buildMethod.apply(applyMethodBuilder).apply(fields);
        IntStream.range(0, fields.length).filter(e -> fields[e] != null).forEach(consumer);

        var applyMethod = applyMethodBuilder.addStatement("return String.join($S, row)", separator)
                .build();

        return TypeSpec.classBuilder(fileMapperClassName).
                addSuperinterface(ParameterizedTypeName.get(ClassName.get(FileMapper.class), annotatedElementTypeName))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(FieldSpec.builder(String[].class, "ROW")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC).build())
                .addField(FieldSpec.builder(CSVEscapeCharacters.class, "ESCAPE_CHARACTERS")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC).build())
                .addMethod(applyMethod)
                .addStaticBlock(CodeBlock.of("""
                        ROW = new String[$L];
                        $T.fill(ROW, "");
                        ESCAPE_CHARACTERS = $L.of($S);""", fields.length, Arrays.class, ClassName.get(CSVEscapeCharacters.class), separator))
                .build();
    }


}
