package domenico.sotgiu.processor.recordmapping;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.processor.util.FieldNameMapper;
import domenico.sotgiu.processor.util.FileColumnDefault;
import domenico.sotgiu.processor.util.ReplaceOf;

import java.util.Optional;

public interface RecordFieldNameMapper extends FieldNameMapper {
    static FieldNameMapper of(){
      return e->{
          var annotation = Optional.ofNullable(e.getAnnotation(FileColumn.class));

          var repl = ReplaceOf.of("this");
          var defReplied = repl.apply(annotation.map(FileColumn::format).orElse("this"),
                  FileColumnDefault.from(annotation).get());
          return repl.apply(defReplied, "e."+e.getSimpleName().toString()+"()");
      };
    }
}
