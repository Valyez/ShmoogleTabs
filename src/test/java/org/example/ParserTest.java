package org.example;

import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ParserTest {

    @org.junit.jupiter.api.Test
    void parseTest() {
        List<Lexeme> parse = Parser.parse("=A1+B2");
        Assertions.assertEquals(4, parse.size(), "wrong number of lexemes");
        Assertions.assertThrows(RuntimeException.class, () -> Parser.parse(null));
    }
}