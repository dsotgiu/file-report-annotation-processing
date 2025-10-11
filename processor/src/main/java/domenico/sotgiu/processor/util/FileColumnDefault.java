package domenico.sotgiu.processor.util;

import domenico.sotgiu.annotation.FileColumn;

import java.util.Optional;
import java.util.function.Supplier;

public interface FileColumnDefault extends Supplier<String> {
    static FileColumnDefault from(Optional<FileColumn> annotation ){
        return () -> annotation.flatMap(an ->
                Optional.of(an.defaultMethod()))
                .filter(df -> !df.isBlank())
                .map(df -> "ofNullable(this).orElse(e." + df + "())")
                .orElse("this");
    }

}
