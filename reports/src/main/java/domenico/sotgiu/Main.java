package domenico.sotgiu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args)  {
        Supplier<Stream<RecordTest>> supplier = ()-> Stream.of(new RecordTest("0\"",10,new ClassTest("EEE")));
        try {
            var f = Files.createFile(Path.of("reports/src/main/resources/file.csv"));
            new domenico.sotgiu.RecordTestFileBuilder(new domenico.sotgiu.RecordTestFileMapper()).build(f, supplier, Map.of("t","pr,ova"));
            try(var s = Files.lines(f)){
                s.forEach(System.out::println);
            }
            f.toFile().delete();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println("Hello, World!");
    }
}