package domenico.sotgiu.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Target({RECORD_COMPONENT, FIELD, METHOD})
@Retention(CLASS)
public @interface FileColumn {
    String value();
    String format() default "this";
    String defaultMethod() default  "";
}
