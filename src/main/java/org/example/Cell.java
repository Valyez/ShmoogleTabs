package org.example;

import java.util.List;

public class Cell {
    private List<Lexeme> lexemes;
    private String formula;
    private String address;
    private String integer;



    public List<Lexeme> getLexemes() {
        return lexemes;
    }

    public void setLexemes(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setInteger(String integer) {
        this.integer = integer;
    }

}