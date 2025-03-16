package domenico.sotgiu.processor;

import com.palantir.javapoet.*;
import domenico.sotgiu.core.FileMapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.Map;

public class GenerateFileMapper {
    public static JavaFile generate(Element annotatedElement,
                                         String packageName,
                                         Map<String, String> fieldMap,
                                         Map<String, Integer> headersPosition,
                                         String[] headers) {
        var fileMapperClassName = annotatedElement.getSimpleName() + "FileMapper";

        var annotatedElementTypeName = TypeName.get(annotatedElement.asType());
        var applyMethodBuilder = MethodSpec.methodBuilder("apply")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC).returns(String.class)
                .addParameter(annotatedElementTypeName, "e")
                .addStatement("var row = ROW.clone()");
        fieldMap.keySet().forEach(e -> applyMethodBuilder.addStatement("row[$L] = e.$L()", headersPosition.get(e), fieldMap.get(e)));

        var applyMethod = applyMethodBuilder.addStatement("return String.join(\",\", row)")
                .build();
        var fileMapper = TypeSpec.classBuilder(fileMapperClassName).
                addSuperinterface(ParameterizedTypeName.get(ClassName.get(FileMapper.class), annotatedElementTypeName))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(FieldSpec.builder(String[].class, "ROW")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC).build())
                .addMethod(applyMethod)
                .addStaticBlock(CodeBlock.of("""
                        ROW = new String[$L];
                        $T.fill(ROW, "");""", headers.length, Arrays.class))
                .build();

        return JavaFile.builder(packageName, fileMapper).build();
    }


}
