package domenico.sotgiu.processor.recordmapping;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.processor.util.FieldNameMapper;
import domenico.sotgiu.processor.util.FileColumnDefault;
import domenico.sotgiu.processor.util.ReplaceOf;

import java.util.Optional;
import java.util.function.Function;

public interface RecordFieldNameMapper extends FieldNameMapper {
    static FieldNameMapper of(){
      return element->{
          var annotation = Optional.ofNullable(element.getAnnotation(FileColumn.class));

          var repl = ReplaceOf.of("this");

          Function<String, String> methodRepl = string -> ReplaceOf.of("method")
                  .apply(string, "e");
          var defReplied = repl.apply(annotation.map(FileColumn::format).orElse("this"),
                  FileColumnDefault.from(annotation).get());
          return repl.andThen(methodRepl).apply(defReplied, "e."+element.getSimpleName().toString()+"()");
      };
    }
}
