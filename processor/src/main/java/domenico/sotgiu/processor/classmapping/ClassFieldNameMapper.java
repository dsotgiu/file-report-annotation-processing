package domenico.sotgiu.processor.classmapping;

import domenico.sotgiu.processor.util.FieldNameMapper;

import javax.lang.model.element.ElementKind;


public interface ClassFieldNameMapper extends FieldNameMapper {
    static FieldNameMapper of(){
        return e -> {
            var fieldName =  e.getSimpleName().toString();
            return  e.getKind()== ElementKind.METHOD?fieldName:
                    "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);};
    }
}
