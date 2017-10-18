package ndr.brt.sieve;

import java.util.function.BiFunction;

class FieldPlaceholder {

    private static final String PLACEHOLDER_OPEN = "{{";
    private static final String PLACEHOLDER_CLOSE = "}}";

    static final BiFunction<String, Object, String> SUBSTITUTE_PLACEHOLDERS = (string, object) -> {
        StringBuilder actual = new StringBuilder();
        String left = string;
        while (left.contains(PLACEHOLDER_OPEN)) {
            int start = left.indexOf(PLACEHOLDER_OPEN);
            int end = left.indexOf(PLACEHOLDER_CLOSE);

            actual.append(left.substring(0, start));
            String expression = left.substring(start + PLACEHOLDER_OPEN.length(), end);

            left = left.substring(end + PLACEHOLDER_CLOSE.length());
            ReflectionExpression reflectionExpression = new ReflectionExpression(object, expression);
            actual.append(reflectionExpression.getValue());
        }
        actual.append(left);
        return actual.toString();
    };
}
