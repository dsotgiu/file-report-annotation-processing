package domenico.sotgiu;

import domenico.sotgiu.annotations.FileColumn;
import domenico.sotgiu.annotations.FileHeader;

@FileHeader(value = {"test", "tst ${t}", "teest"}, separator = "TEST")
public record RecordTest(
        String test,
        @FileColumn("tst ${t}") Integer tst,
        @FileColumn("teest") ClassTest teest) {
}
