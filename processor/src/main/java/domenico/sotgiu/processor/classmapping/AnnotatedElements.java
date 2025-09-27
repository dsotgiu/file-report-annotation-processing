package domenico.sotgiu.processor.classmapping;

import domenico.sotgiu.annotation.FileColumn;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface AnnotatedElements extends Supplier<Set<String>> {

    static AnnotatedElements of(Element annotatedElement) {
        var mapper = ClassFieldNameMapper.of();
        return () -> annotatedElement.getEnclosedElements().stream().filter(e -> {
            if (!(ElementKind.METHOD.equals(e.getKind())||
                    ElementKind.FIELD.equals(e.getKind()))) {
                return false;
            }
            var column = e.getAnnotation(FileColumn.class);
            return Objects.nonNull(column) || e.getSimpleName().toString().startsWith("get");
        }).map(mapper).map(Objects::toString).collect(Collectors.toSet());
    }

}
