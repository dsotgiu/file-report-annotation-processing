package domenico.sotgiu.example.classtest;

import domenico.sotgiu.annotation.FileColumn;
import domenico.sotgiu.annotation.FileHeader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;


@FileHeader({"test", "dt", "dti", "nullElem", "test1"})
public class ClassTest {
    private final String dt;

    private final int dti;
    @FileColumn(value = "test",format = "this.substring(0,2)")
    public  String data() {
        return "test" + Optional.ofNullable(this.dt).orElse("test");
    }

    public  Integer data1() {
        return 1;
    }
    @FileColumn(value = "nullElem",defaultMethod = "data1")
    public Integer nullElement;

    @FileColumn(value = "test1", format = "method.formatBigDecimal(this)")
    public BigDecimal test1;

    public String getDt() {

        return dt;
    }
    public int getDti() {
        return dti;
    }

    public Integer getNullElement() {
        return nullElement;
    }

    public BigDecimal getTest1() {
        return test1;
    }

    public ClassTest(String dt, int dti) {
        this.dt = dt;
        this.dti = dti;
    }

    @Override
    public String toString() {
        return data() + dt;
    }


    public String formatBigDecimal(BigDecimal e) {
        var symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        var df = new DecimalFormat("#,##0.00", symbols);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(e);
    }
}
