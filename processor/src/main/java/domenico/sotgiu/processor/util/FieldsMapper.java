package domenico.sotgiu.processor.util;

import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public interface FieldsMapper extends BiFunction<Element, String[], TypeMapping[]> {

    static FieldsMapper mapper(Filter filter, FieldNameMapper mapper) {
        return (annotatedElement, headers) -> {
            var fields = annotatedElement.getEnclosedElements().stream().filter(filter)
                    .map(e -> TypeMapping.of(e, mapper)).distinct()
                    .collect(toMap(TypeMapping::header, Function.identity()));
            return Arrays.stream(headers).map(fields::get).toArray(TypeMapping[]::new);

        };
    }
}
