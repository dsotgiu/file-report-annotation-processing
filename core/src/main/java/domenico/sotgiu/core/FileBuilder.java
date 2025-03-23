package domenico.sotgiu.core;

import domenico.sotgiu.core.util.CSVEscapeCharacters;
import domenico.sotgiu.core.util.ReplacePlaceholders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class FileBuilder<T> {

    Function<Map<String, String>, Function<String, String>>
            mapperFunction = e -> s -> ReplacePlaceholders.apply(s, e);

    protected void build(String[] header, FileMapper<T> mapper,
                         Path path, Supplier<Stream<T>> supplier,
                         Map<String, String> headersData) throws IOException {

        var replacePlaceholders = mapperFunction.apply(headersData);

        try (var writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            var escapedHeaders = Arrays.stream(header)
                    .map(replacePlaceholders)
                    .map(CSVEscapeCharacters::apply)
                    .toArray(String[]::new);

            writer.write(String.join(",", escapedHeaders));

            try (var stream = supplier.get()) {
                stream.map(mapper).forEach(e -> {
                    try {
                        writer.newLine();
                        writer.write(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
            writer.flush();
        }
    }

    public abstract void build(Path path, Supplier<Stream<T>> supplier, Map<String, String> headersData)
            throws IOException;

}
