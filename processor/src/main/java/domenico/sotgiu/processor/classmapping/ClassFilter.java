package domenico.sotgiu.processor.classmapping;

import domenico.sotgiu.annotations.FileColumn;
import domenico.sotgiu.processor.util.Filter;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.Objects;
import java.util.Set;

public interface ClassFilter extends Filter {

    static ClassFilter of(Element annotatedElement, String[] headers) {
        Set<String> headersSet = setOf.apply(headers);
        var annotatedGetters = AnnotatedGetters.of(annotatedElement).get();
        return e -> {
            if (!ElementKind.FIELD.equals(e.getKind())) {
                return false;
            }

            var fieldName = e.getSimpleName().toString();
            String getter = ClassFieldNameMapper.of().apply(fieldName);
            if (!annotatedGetters.contains(getter)) {
                throw new RuntimeException(String.format("The field %s doesn't have a getter", e.getSimpleName()));
            }
            var column = e.getAnnotation(FileColumn.class);
            if (Objects.isNull(column)) {
                return headersSet.contains(fieldName);

            }

            var fieldColumn = column.value();
            return headersSet.contains(fieldColumn);
        };
    }
}
