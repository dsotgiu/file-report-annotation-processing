package domenico.sotgiu.processor.recordmapping;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.processor.util.FieldNameMapper;

import java.util.Optional;

public interface RecordFieldNameMapper extends FieldNameMapper {
    static FieldNameMapper of(){
      return e->
              Optional.ofNullable(e.getAnnotation(FileColumn.class)).map(FileColumn::format).orElse("this")
                      .replace("this", "e."+e.getSimpleName().toString()+"()");
    }
}
