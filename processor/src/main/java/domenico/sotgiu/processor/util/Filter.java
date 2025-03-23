package domenico.sotgiu.processor.util;

import javax.lang.model.element.Element;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Filter extends Predicate<Element> {
    Function<String[], Set<String>> setOf = Set::of;
}
