package domenico.sotgiu.example;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.annotation.FileHeader;

@FileHeader({"test", "dt"})
public class ClassTest {
    @FileColumn("test")
    private final static String data = "data";
    private String dt;

    public String getData() {
        return data;
    }

    public String getDt() {
        return dt;
    }

    public ClassTest(String dt) {
        this.dt = dt;
    }

    @Override
    public String toString() {
        return data + dt;
    }
}
