package domenico.sotgiu.processor.util;

import domenico.sotgiu.annotations.FileColumn;

import javax.lang.model.element.Element;
import java.util.Objects;
import java.util.function.Function;

public record TypeMapping(String type, String header, String field) {
    public static TypeMapping of(Element e, Function<Element, String> fieldNameMapper){

        return new TypeMapping(e.asType().toString(),
                Objects.isNull(e.getAnnotation(FileColumn.class))
                        ? e.getSimpleName().toString() :
                        e.getAnnotation(FileColumn.class).value(),
                fieldNameMapper.apply(e));
    }
}
