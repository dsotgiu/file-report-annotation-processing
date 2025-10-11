package domenico.sotgiu.example.interfacetest;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.annotation.FileHeader;

@FileHeader("test")
public interface InterfaceTest {
    @FileColumn(value = "test", format = "this.substring(0,2)")
    String getTest();
}
