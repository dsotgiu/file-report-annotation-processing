package domenico.sotgiu.example;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.annotation.FileHeader;

@FileHeader("test")
public interface InterfaceTest {
    @FileColumn("test")
    String getTest();
}
