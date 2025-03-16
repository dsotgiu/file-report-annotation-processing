package domenico.sotgiu.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class  FileBuilder<T> {
    Pattern pattern = Pattern.compile("\\$\\{([^}]+)}");

    private String replacePlaceholders(String input, Map<String, String> replacements) {
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


    protected void build(String header, FileMapper<T> mapper,
                      Path path, Supplier<Stream<T>> supplier,
                      Map<String, String> headersData) throws IOException {
        try(var writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE , StandardOpenOption.APPEND)) {
            writer.write(replacePlaceholders(header, headersData));
            try(var stream = supplier.get()) {
                stream.map(mapper).forEach(e->{
                    try {
                        writer.newLine();
                        writer.write(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
            writer.flush();
        }
    }

    public abstract void build(Path path, Supplier<Stream<T>> supplier, Map<String, String> headersData)
            throws IOException;
}
