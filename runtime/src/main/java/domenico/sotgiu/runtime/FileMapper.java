package domenico.sotgiu.runtime;

import java.util.function.Function;

@FunctionalInterface
public interface FileMapper<T> extends Function<T, String> {

}
