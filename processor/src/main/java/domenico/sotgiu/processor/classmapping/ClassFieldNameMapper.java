package domenico.sotgiu.processor.classmapping;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.processor.util.FieldNameMapper;

import javax.lang.model.element.ElementKind;
import java.util.Optional;


public interface ClassFieldNameMapper extends FieldNameMapper {
    static FieldNameMapper of(){
        return e -> {
            var fieldName =  e.getSimpleName().toString();
            return  Optional.ofNullable(e.getAnnotation(FileColumn.class))
                    .map(FileColumn::format).orElse("this")
                    .replace("this",
                            e.getKind() == ElementKind.METHOD ?
                                   "e."+ fieldName+"()":
                    "e.get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)+"()");};
    }
}
