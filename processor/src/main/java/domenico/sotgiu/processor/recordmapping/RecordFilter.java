package domenico.sotgiu.processor.recordmapping;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.processor.util.Filter;

import javax.lang.model.element.ElementKind;
import java.util.Objects;
import java.util.Set;

public interface RecordFilter extends Filter {
    static RecordFilter of(String[] headers) {
        Set<String> headersSet = setOf.apply(headers);
        return e -> {
            if (!ElementKind.FIELD.equals(e.getKind())) {
                return false;
            }
            var column = e.getAnnotation(FileColumn.class);
            if (Objects.isNull(column)) {
                return headersSet.contains(e.getSimpleName().toString());
            }
            var fieldColumn = column.value();
            return headersSet.contains(fieldColumn);
        };
    }
}

