package domenico.sotgiu;

import domenico.sotgiu.annotations.FileColumn;
import domenico.sotgiu.annotations.FileHeader;

@FileHeader({"test", "tst ${t}", "teest"})
public record RecordTest(
        String test,
        @FileColumn("tst ${t}") Integer tst,
        @FileColumn("teest") ClassTest teest) {
}
