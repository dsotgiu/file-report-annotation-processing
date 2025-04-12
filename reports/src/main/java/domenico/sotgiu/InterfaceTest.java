package domenico.sotgiu;

import domenico.sotgiu.annotations.FileColumn;
import domenico.sotgiu.annotations.FileHeader;

@FileHeader("test")
public interface InterfaceTest {
    @FileColumn("test")
    String getTest();
}
