package domenico.sotgiu.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import java.lang.annotation.ElementType;

import static java.lang.annotation.RetentionPolicy.CLASS;

@Target({ElementType.TYPE})
@Retention(CLASS)
public @interface FileHeader {
    String[] value();
    String separator() default ",";
}
