package domenico.sotgiu.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args)  {
        Supplier<Stream<RecordTest>> supplier = ()-> Stream.of(new RecordTest("0\"",10,new ClassTest("EEE",1)));
        try {
            var f = Files.createFile(Path.of("example/src/main/resources/file.csv"));
            new domenico.sotgiu.example.RecordTestCSVBuilder(new domenico.sotgiu.example.RecordTestCSVMapper()).build(f, supplier, Map.of("t","pr,ova"));
            try(var s = Files.lines(f)){
                s.forEach(System.out::println);
            }

            Files.delete(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}