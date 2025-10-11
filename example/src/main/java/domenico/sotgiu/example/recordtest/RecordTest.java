package domenico.sotgiu.example.recordtest;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.annotation.FileHeader;
import domenico.sotgiu.example.classtest.ClassTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;

@FileHeader(value = {"tst ${t}", "test1", "test2", "test3", "test4"}, separator = ";")
public record RecordTest(
        // @FileColumn(value = "test", format = "this.substring(0,2)")
        String test1,
        @FileColumn(value = "tst ${t}", defaultMethod = "defaultInteger") Integer tst,
        @FileColumn("test2") ClassTest test,
        @FileColumn(value = "test3", format = "method.formatBigDecimal(this)")BigDecimal test3
        ) {

    @FileColumn(value = "test4", format = "this.substring(0,2)")
    public String prova() {
        return test1+ Optional.ofNullable(test1).orElse("test");
    }
    public Integer defaultInteger() {
        return -1;
    }

    public String formatBigDecimal(BigDecimal e) {
        var symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        var df = new DecimalFormat("#,##0.00", symbols);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(e);
    }
}
