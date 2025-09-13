package domenico.sotgiu.runtime.csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class CSVBuilder<T> {

    protected void write(String escapedHeaders, CSVMapper<T> mapper,
                         Path path, Supplier<Stream<T>> supplier
                         ) throws IOException {

        try (var writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(escapedHeaders);
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
