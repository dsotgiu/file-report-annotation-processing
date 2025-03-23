package domenico.sotgiu.processor.util;

import com.palantir.javapoet.TypeSpec;

import javax.lang.model.element.Element;

public abstract class GenerateFile<T> {
    protected Element annotatedElement;

    public GenerateFile(Element annotatedElement) {
        this.annotatedElement = annotatedElement;
    }

    abstract public  TypeSpec generate(T headers);
}
