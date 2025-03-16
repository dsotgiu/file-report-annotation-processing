package domenico.sotgiu;

import domenico.sotgiu.annotations.FileColumn;
import domenico.sotgiu.annotations.FileHeader;

@FileHeader({"test", "tst ${t}"})
public record RecordTest(
        @FileColumn("test") String test,
        @FileColumn("tst ${t}") String tst,
        @FileColumn("teest") String teest) {
}
