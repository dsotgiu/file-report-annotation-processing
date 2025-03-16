package domenico.sotgiu.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Target({RECORD_COMPONENT, FIELD})
@Retention(CLASS)
public @interface FileColumn {
    String value();
}
