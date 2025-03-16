package domenico.sotgiu.processor;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.*;
import domenico.sotgiu.annotations.FileColumn;
import domenico.sotgiu.annotations.FileHeader;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;
import static javax.lang.model.element.ElementKind.RECORD;


@SupportedAnnotationTypes({"domenico.sotgiu.annotations.FileHeader"})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class GenerateFileProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(FileHeader.class)) {
            if (annotatedElement.getKind() != RECORD) {
                getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("Only records can be annotated with @%s", FileHeader.class.getSimpleName()), annotatedElement);
                return true;
            }

            var headers = getHeaders(annotatedElement);
            var fieldMap = getFieldMap(annotatedElement, Set.of(headers));
            var headersPosition = getHeadersPosition(headers);
            var packageName = processingEnv.getElementUtils().getPackageOf(annotatedElement)
                    .getQualifiedName().toString();
            try {

                buildFile(GenerateFileMapper.generate(annotatedElement, packageName, fieldMap, headersPosition, headers),
                        annotatedElement.getSimpleName() + "FileMapper");

                buildFile(GenerateFileBuilder.generate(annotatedElement, packageName, headers),
                        annotatedElement.getSimpleName() + "FileBuilder");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return true;
    }



    private void buildFile(JavaFile javaFile, String fileName) throws IOException {
        var builderFile = processingEnv.getFiler().createSourceFile(fileName);

        try (var writer = builderFile.openWriter()) {
            javaFile.writeTo(writer);
        }
    }


    public Messager getMessager() {
        return processingEnv.getMessager();
    }

    private String[] getHeaders(Element classElement) {
        FileHeader head = classElement.getAnnotation(FileHeader.class);
        return head.value();
    }

    private Map<String, String> getFieldMap(Element annotatedElement, Set<String> headerSet) {

        Predicate<Element> filter = e -> {
            if (!ElementKind.FIELD.equals(e.getKind())) {
                return false;
            }
            var column = e.getAnnotation(FileColumn.class);
            if (Objects.isNull(column)) {
                getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                        String.format("The field %s is not mapped with @FileColumn", e.getSimpleName()));
                return false;
            }
            var fieldColumn = column.value();
            if (!headerSet.contains(fieldColumn)) {
                getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                        String.format("The @FileHeader doesn't contains the column %s", fieldColumn));
                return false;
            }
            return true;
        };
        return annotatedElement.getEnclosedElements().stream().filter(filter).collect(
                toMap(e -> e.getAnnotation(FileColumn.class).value(),
                        e -> e.getSimpleName().toString()));
    }

    private Map<String, Integer> getHeadersPosition(String[] headers) {
        return IntStream.range(0, headers.length).boxed().collect(Collectors.toMap(e -> headers[e], Function.identity()));
    }


}
