package domenico.sotgiu.processor;

import com.google.auto.service.AutoService;
import domenico.sotgiu.annotations.FileHeader;
import domenico.sotgiu.processor.classmapping.ClassFieldNameMapper;
import domenico.sotgiu.processor.classmapping.ClassFilter;
import domenico.sotgiu.processor.csv.CSVGenerateFileBuilder;
import domenico.sotgiu.processor.csv.CSVGenerateFileMapper;
import domenico.sotgiu.processor.recordmapping.RecordFieldNameMapper;
import domenico.sotgiu.processor.recordmapping.RecordFilter;
import domenico.sotgiu.processor.util.FieldsMapper;
import domenico.sotgiu.processor.util.FileBuilder;
import domenico.sotgiu.processor.util.GenerateFileBuilder;
import domenico.sotgiu.processor.util.TypeMapping;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

import static javax.lang.model.element.ElementKind.*;


@SupportedAnnotationTypes({"domenico.sotgiu.annotations.FileHeader"})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class GenerateFileProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }
    private final static Set<ElementKind> kinds = Set.of(RECORD, CLASS, INTERFACE);
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(FileHeader.class)) {

            if (!kinds.contains(annotatedElement.getKind())) {
                getMessager().printMessage(Diagnostic.Kind.ERROR,
                        String.format("Only records  or classes can be annotated with @%s",
                                FileHeader.class.getSimpleName()), annotatedElement);
                return true;
            }
            FileHeader head = annotatedElement.getAnnotation(FileHeader.class);
            var headers = head.value();

            var fieldsMapper = annotatedElement.getKind() == RECORD ?
                    FieldsMapper.mapper(RecordFilter.of(headers), RecordFieldNameMapper.of()) :
                    FieldsMapper.mapper(ClassFilter.of(annotatedElement, headers), ClassFieldNameMapper.of());

            try {
                TypeMapping[] mapperFields = fieldsMapper.apply(annotatedElement, headers);

                GenerateFileBuilder generateFileBuilder = new GenerateFileBuilder(
                        new CSVGenerateFileBuilder(annotatedElement), new CSVGenerateFileMapper(annotatedElement));
                buildFiles(annotatedElement, generateFileBuilder, mapperFields, headers);
            } catch (Exception exception) {
                getMessager().printMessage(Diagnostic.Kind.ERROR, exception.getMessage());
            }
        }
        return true;
    }

    private void buildFiles(Element annotatedElement, GenerateFileBuilder generateFileBuilder, TypeMapping[]
            mapperFields, String[] headers) {

        FileBuilder builder = FileBuilder.of(processingEnv.getFiler(),
                processingEnv.getElementUtils().getPackageOf(annotatedElement).getQualifiedName().toString());
        builder.accept(generateFileBuilder.mapper().generate(mapperFields), annotatedElement.getSimpleName() + "FileMapper");
        builder.accept(generateFileBuilder.builder().generate(headers), annotatedElement.getSimpleName() + "FileBuilder");

    }


    public Messager getMessager() {
        return processingEnv.getMessager();
    }

}
