package org.example;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private Parser() {
    }

    public static List<Lexeme> parse(String primalText) {
        List<Lexeme> result = new ArrayList<>();
        int pos = 0;
        while (pos < primalText.length()) {
            char c = primalText.charAt(pos);
            switch (c) {
                case '=' -> result.add(new Lexeme(LexemeType.EQUALS, "="));
                case '-' -> {
                    if(!(result.isEmpty()) && (result.get(result.size() - 1).getType() == LexemeType.NUMBER ||  result.get(result.size() - 1).getType() == LexemeType.LINK)){
                        result.add(new Lexeme(LexemeType.MINUS, "-"));
                    }else {
                        result.add( new Lexeme(LexemeType.EQUALS, "="));
                        result.add(new Lexeme(LexemeType.NUMBER, "-1"));
                        result.add(new Lexeme(LexemeType.MULTIPLY, "*"));
                    }
                }
                case '+' -> result.add(new Lexeme(LexemeType.PLUS, "+"));
                case '*' -> result.add(new Lexeme(LexemeType.MULTIPLY, "*"));
                case '/' -> result.add(new Lexeme(LexemeType.DIVIDE, "/"));
                case '(' -> result.add(new Lexeme(LexemeType.LEFT_BRACE, "("));
                case ')' -> result.add(new Lexeme(LexemeType.RIGHT_BRACE, ")"));
                default -> {
                    if (c >= 65 && c <= 90) {
                        StringBuilder builder = new StringBuilder();
                        while (c != '-' && c != '+' && c != '*' && c != '/' && c != '(' && c != ')') {
                            builder.append(c);
                            pos++;
                            if (pos == primalText.length()) {
                                break;
                            }
                            c = primalText.charAt(pos);
                        }
                        result.add(new Lexeme(LexemeType.LINK, builder.toString()));
                        pos--;
                    } else if (c >= 48 && c <= 57) {
                        StringBuilder builder = new StringBuilder();
                        while (c >= 48 && c <= 57 || c == 46) {
                            builder.append(c);
                            pos++;
                            if (pos == primalText.length()) {
                                break;
                            }
                            c = primalText.charAt(pos);
                        }
                        result.add(new Lexeme(LexemeType.NUMBER, builder.toString()));
                        pos--;
                    } else if (c == ' ') {
                        continue;
                    } else {
                        throw new RuntimeException("ERROR! Uncorrected formula");
                    }
                }
            }
            pos++;
        }
        return result;
    }
}

enum LexemeType {
    LEFT_BRACE, RIGHT_BRACE,
    MULTIPLY, DIVIDE,
    PLUS, MINUS,
    NUMBER, LINK,
    EQUALS,

}

final class Lexeme {
    private LexemeType type;
    private final String value;
    private final int rate;

    public Lexeme(LexemeType type, String value) {
        this.type = type;
        this.value = value;
        switch (type) {
            case LEFT_BRACE -> rate = 0;
            case PLUS, MINUS -> rate = 1;
            case DIVIDE, MULTIPLY -> rate = 2;
            default -> rate = -1;
        }
    }

    public LexemeType getType() {
        return type;
    }
    public void setType(LexemeType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public int getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "Lexeme{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }

}

