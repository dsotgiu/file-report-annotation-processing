package domenico.sotgiu.core;

import java.util.function.Function;

@FunctionalInterface
public interface FileMapper<T> extends Function<T, String> {
    String apply(T in);
}
