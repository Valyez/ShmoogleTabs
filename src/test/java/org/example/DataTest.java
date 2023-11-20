package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DataTest {
    @Autowired
    Data data;

    @Test
    void calculatePage() {
        Assertions.assertDoesNotThrow(() -> {
            data.getPage().setCellFormula("A1", "1");
            data.getPage().setCellFormula("A2", "-1");
            data.getPage().setCellFormula("A3", "=-A1+A2");
        });
        Assertions.assertEquals(-2.0, data.getPage().calculateCell(data.getPage().getCell("A3")));
    }

    @BeforeEach
    void setUp() {
        data.setPage(new Page(8,8));
    }
    @Test
    void wrongFormula() {
        Assertions.assertThrows(RuntimeException.class, () -> data.getPage().setCellFormula("A1", "A1-B1"));
    }


}