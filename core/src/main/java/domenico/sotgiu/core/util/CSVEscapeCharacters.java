package domenico.sotgiu.core.util;

public final class CSVEscapeCharacters  {

    public static String apply(String input) {
        if (input == null) {
            return "";
        }

        String escaped = input.replace("\"", "\"\"");

        if (escaped.contains(",") ||
                escaped.contains(";")||
                escaped.contains("\n") ||
                escaped.contains("\"")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }
}
