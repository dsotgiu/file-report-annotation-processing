package domenico.sotgiu.processor.classmapping;

import domenico.sotgiu.processor.util.FieldNameMapper;


public interface ClassFieldNameMapper extends FieldNameMapper {
    static FieldNameMapper of(){
        return fieldName -> "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
