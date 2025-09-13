package domenico.sotgiu.example;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.annotation.FileHeader;

@FileHeader(value = {"test", "tst ${t}", "teest"}, separator = ";")
public record RecordTest(
        String test,
        @FileColumn("tst ${t}") Integer tst,
        @FileColumn("teest") ClassTest teest) {
}
