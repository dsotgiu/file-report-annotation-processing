package domenico.sotgiu.processor.recordmapping;

import domenico.sotgiu.processor.util.FieldNameMapper;

public interface RecordFieldNameMapper extends FieldNameMapper {
    static FieldNameMapper of(){
      return e->e.getSimpleName().toString();
    }
}
