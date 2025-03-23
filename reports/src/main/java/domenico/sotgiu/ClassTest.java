package domenico.sotgiu;

import domenico.sotgiu.annotations.FileColumn;
import domenico.sotgiu.annotations.FileHeader;

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
