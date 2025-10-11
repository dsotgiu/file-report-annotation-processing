package domenico.sotgiu.processor.classmapping;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.processor.util.FieldNameMapper;
import domenico.sotgiu.processor.util.FileColumnDefault;
import domenico.sotgiu.processor.util.ReplaceOf;

import javax.lang.model.element.ElementKind;
import java.util.Optional;
import java.util.function.Function;


public interface ClassFieldNameMapper extends FieldNameMapper {
    static FieldNameMapper of() {
        return e -> {
            var fieldName = e.getSimpleName().toString();
            var annotation = Optional.ofNullable(e.getAnnotation(FileColumn.class));
            var repl = ReplaceOf.of("this");
            var defReplied = repl.apply(annotation.map(FileColumn::format).orElse("this"),
                    FileColumnDefault.from(annotation).get());
            Function<String, String> methodRepl = string -> ReplaceOf.of("method")
                    .apply(string, "e");
            return repl.andThen(methodRepl).apply(defReplied,
                    e.getKind() == ElementKind.METHOD ?
                            "e." + fieldName + "()" :
                            "e.get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) + "()");

        };
    }
}
