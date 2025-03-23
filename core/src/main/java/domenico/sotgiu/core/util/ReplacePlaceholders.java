package domenico.sotgiu.core.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ReplacePlaceholders {
    private final static Pattern pattern = Pattern.compile("\\$\\{([^}]+)}");

    public static String apply(String input, Map<String, String> replacements) {
        // Regular expression for matching patterns like ${DATA}
        // Compile the regex pattern
        Matcher matcher = pattern.matcher(input);

        // StringBuilder to accumulate the result
        StringBuilder result = new StringBuilder();

        int lastEnd = 0;

        // Iterate over all matches
        while (matcher.find()) {
            // Append text before the match
            result.append(input, lastEnd, matcher.start());

            // Get the placeholder (key inside ${})
            String placeholder = matcher.group(1);

            // Replace with the value from the replacements map or keep the original if not found
            String replacement = replacements.getOrDefault(placeholder, matcher.group(0));
            result.append(replacement);

            // Update lastEnd to be the end of the current match
            lastEnd = matcher.end();
        }

        // Append any remaining part of the input string after the last match
        result.append(input, lastEnd, input.length());

        return result.toString();
    }
}
