package domenico.sotgiu.runtime.csv;

import java.util.function.Function;

@FunctionalInterface
public interface CSVMapper<T> extends Function<T, String> {

}
