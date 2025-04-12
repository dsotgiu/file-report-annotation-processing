package domenico.sotgiu.processor.util;

import javax.lang.model.element.Element;
import java.util.function.Function;

public interface FieldNameMapper extends Function<Element, String> {
}
