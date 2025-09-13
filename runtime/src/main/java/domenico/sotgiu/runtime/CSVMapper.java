package domenico.sotgiu.runtime;

import java.util.function.Function;

@FunctionalInterface
public interface CSVMapper<T> extends Function<T, String> {

}
