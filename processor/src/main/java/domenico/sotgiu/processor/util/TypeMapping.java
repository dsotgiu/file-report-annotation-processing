package domenico.sotgiu.processor.util;

import domenico.sotgiu.annotation.FileColumn;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TypeMapping that = (TypeMapping) o;
        return Objects.equals(field, that.field) && Objects.equals(header, that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, field);
    }
}
