package domenico.sotgiu.example;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.annotation.FileHeader;

import java.util.Optional;

@FileHeader(value = {"test1", "tst ${t}", "teest"}, separator = ";")
public record RecordTest(
       // @FileColumn(value = "test", format = "this.substring(0,2)")
        String test,
        @FileColumn(value = "tst ${t}", defaultMethod = "defaultInteger") Integer tst,
        @FileColumn("teest") ClassTest teest) {

    @FileColumn(value = "test1", format = "this.substring(0,2)")
    public String prova() {
        return test+ Optional.ofNullable(test).orElse("test");
    }
    public Integer defaultInteger() {
        return -1;
    }
}
