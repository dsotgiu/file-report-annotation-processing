package domenico.sotgiu.processor.classmapping;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.processor.util.Filter;

import javax.lang.model.element.Element;
import java.util.Objects;
import java.util.Set;

public interface ClassFilter extends Filter {

    static ClassFilter of(Element annotatedElement, String[] headers) {
        Set<String> headersSet = setOf.apply(headers);
        var annotatedGetters = AnnotatedElements.of(annotatedElement).get();
        return e -> {

            String val = ClassFieldNameMapper.of().apply(e);
            if (!annotatedGetters.contains(val)) {
                return false;
            }
            var column = e.getAnnotation(FileColumn.class);
            if (Objects.isNull(column)) {
                return headersSet.contains(val);
            }

            var fieldColumn = column.value();
            return headersSet.contains(fieldColumn);
        };
    }
}
