package domenico.sotgiu.runtime.util;

import java.util.function.Function;


public interface CSVEscapeCharacters  extends Function<Object, String> {

    static CSVEscapeCharacters of(String separator) {
       return i->{
           if (i == null) {
            return "";
        }

        var input = String.valueOf(i);
        String escaped = input.replace("\"", "\"\"");

        if (escaped.contains(",") ||
                escaped.contains(";")||
                escaped.contains(separator) ||
                escaped.contains("\n") ||
                escaped.contains("\"")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    };}
}
