package domenico.sotgiu.processor.classmapping;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface AnnotatedGetters  extends Supplier<Set<String>> {

    static AnnotatedGetters of(Element annotatedElement) {
        return () -> annotatedElement.getEnclosedElements().stream().filter(e -> {
            if (!ElementKind.METHOD.equals(e.getKind())) {
                return false;
            }
            return e.getSimpleName().toString().startsWith("get");
        }).map(Element::getSimpleName).map(Objects::toString).collect(Collectors.toSet());
    }

}
