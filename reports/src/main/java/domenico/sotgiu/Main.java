package domenico.sotgiu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args)  {
        Supplier<Stream<RecordTest>> supplier = ()-> Stream.of(new RecordTest("0","t","e"));
        Path f = null;
        try {
            f = Files.createTempFile("", "");
            new domenico.sotgiu.RecordTestFileBuilder().build(f, supplier, Map.of("t","prova"));
            try(var s = Files.lines(f)){
                s.forEach(System.out::println);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        f.toFile().delete();
        //System.out.println("Hello, World!");
    }
}