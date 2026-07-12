package modulos;

public class ExpressionParser {

    private String input;
    private int pos;
    private double x;

    public double evaluate(String expression, double xValue) {

        input = expression
                .replace(" ", "")
                .toLowerCase();
        pos = 0;
        x = xValue;

        double value = parseExpression();

        if (pos < input.length()) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    private double parseExpression() {

        double value = parseTerm();

        while (pos < input.length()) {

            char c = input.charAt(pos);

            if (c == '+') {

                pos++;
                value += parseTerm();
            }

            else if (c == '-') {

                pos++;
                value -= parseTerm();
            }

            else {

                break;
            }
        }

        return value;
    }

    private double parseTerm() {

        double value = parsePower();

        while (pos < input.length()) {

            char c = input.charAt(pos);

            if (c == '*') {

                pos++;
                value *= parsePower();
            }

            else if (c == '/') {

                pos++;
                value /= parsePower();
            }

            else {

                break;
            }
        }

        return value;
    }

    private double parsePower() {

        double value = parseFactor();

        if (pos < input.length() && input.charAt(pos) == '^') {

            pos++;
            value = Math.pow(value, parsePower());
        }

        return value;
    }

    private double parseFactor() {

        if (pos >= input.length()) {

            throw new IllegalArgumentException();
        }

        char c = input.charAt(pos);

        if (c == '+') {

            pos++;
            return parseFactor();
        }

        if (c == '-') {

            pos++;
            return -parseFactor();
        }

        if (c == '(') {

            pos++;
            double value = parseExpression();
            expect(')');
            return value;
        }

        if (Character.isDigit(c) || c == '.') {

            return parseNumber();
        }

        if (c == 'x') {

            pos++;
            return x;
        }

        if (Character.isLetter(c)) {

            String name = parseName();
            expect('(');
            double argument = parseExpression();
            expect(')');
            return applyFunction(name, argument);
        }

        throw new IllegalArgumentException();
    }

    private double parseNumber() {

        int start = pos;

        while (pos < input.length()
                && (
                        Character.isDigit(input.charAt(pos))
                        || input.charAt(pos) == '.'
                )) {

            pos++;
        }

        return Double.parseDouble(input.substring(start, pos));
    }

    private String parseName() {

        int start = pos;

        while (pos < input.length()
                && Character.isLetter(input.charAt(pos))) {

            pos++;
        }

        return input.substring(start, pos);
    }

    private void expect(char expected) {

        if (pos >= input.length() || input.charAt(pos) != expected) {

            throw new IllegalArgumentException();
        }

        pos++;
    }

    private double applyFunction(String name, double argument) {

        switch (name) {
            case "sin":
                return Math.sin(argument);
            case "cos":
                return Math.cos(argument);
            case "tan":
                return Math.tan(argument);
            default:
                throw new IllegalArgumentException();
        }
    }
}
