package domenico.sotgiu.core.util;

import java.util.function.UnaryOperator;


public interface CSVEscapeCharacters  extends UnaryOperator<String> {

    static UnaryOperator<String> of(String separator) {
       return input->{
           if (input == null) {
            return "";
        }

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
