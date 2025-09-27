package domenico.sotgiu.processor.util;

import java.util.function.BinaryOperator;


public interface ReplaceOf extends BinaryOperator<String> {
    static ReplaceOf of(String replaced){
        return (fr, repl)->fr.replace(replaced, repl);
    }

}
