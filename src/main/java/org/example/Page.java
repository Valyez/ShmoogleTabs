package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Page {
    private final Cell[][] table;
    private final int rowsSize;
    private final int columnSize;
    Set<Cell> usedCells;

    public Page(int x, int y) {
        x++;
        y++;
        table = new Cell[x][y];
        for (int i = 0; i < x; i++) {
            char rowName = (char) (i + 48); //number
            for (int j = 0; j < y; j++) {
                char columnName = (char) (j + 64); //letter
                String cellAddress = columnName + "" + rowName;
                table[i][j] = new Cell();
                table[i][j].setInteger(null);
                table[i][j].setAddress(cellAddress);
                if (j == 0) {
                    table[i][j].setInteger(String.valueOf(i));
                }
                if (i == 0) {
                    table[i][j].setInteger(String.valueOf((char) (j + 64)));
                }
            }
        }
        table[0][0].setInteger(" ");
        rowsSize = x;
        columnSize = y;
        usedCells = new HashSet<>();
    }

    public void setCellFormula(String address, String f) {

        Cell cell = getCell(address); // Mожет выбросить ошибку если обратиться за пределы таблицы
        List<Lexeme> tmp = cell.getLexemes();
        cell.setLexemes(Parser.parse(f));// Может выбросить ошибку если при парсинге найдена ошибка
        try {
            calculateCell(cell); // Может выбросить ошибку если есть цикличность, пропущен "=" или ошибка в формуле
        } catch (RuntimeException e) {
            cell.setLexemes(tmp);
            throw e;
        }
        cell.setFormula(f);
    }

    public Double calculateCell(Cell cell) {
        usedCells.add(cell);
        List<Lexeme> lexemes = cell.getLexemes();
        if (lexemes == null) {
            throw new RuntimeException("ERROR! Sell " + cell.getAddress() + " is empty");
        }
        if (lexemes.size() > 1 && lexemes.get(0).getType() != LexemeType.EQUALS || lexemes.size() == 1 && lexemes.get(0).getType() != LexemeType.NUMBER) {
            throw new RuntimeException("ERROR! Incorrect formula, you may have missed '='");
        }
        int pos = 0;
        Stack<Double> numbers = new Stack<>();
        Stack<Lexeme> operations = new Stack<>();
        while (pos < lexemes.size()) {
            Lexeme lexeme = lexemes.get(pos);
            switch (lexeme.getType()) {
                case LINK -> {
                    Cell linkedCell = getCell(lexeme.getValue());
                    if (usedCells.contains(linkedCell)) {        // check cyclical links
                        throw new RuntimeException("ERROR! Cyclic dependence is found");
                    }
                    numbers.add(calculateCell(linkedCell));
                }
                case NUMBER -> numbers.add(Double.valueOf(lexeme.getValue()));
                case DIVIDE, MULTIPLY, LEFT_BRACE -> operations.add(lexeme);
                case PLUS, MINUS -> {
                    while (!(operations.empty() || operations.peek().getRate() <= lexeme.getRate())) {
                        double two = numbers.pop();
                        double one = numbers.pop();
                        switch (operations.pop().getType()) {
                            case DIVIDE -> numbers.add(one / two);
                            case MULTIPLY -> numbers.add(one * two);
                        }
                    }
                    operations.add(lexeme);
                }
                case RIGHT_BRACE -> {
                    while (operations.peek().getType() != LexemeType.LEFT_BRACE) {
                        double two = numbers.pop();
                        double one = numbers.pop();
                        if (operations.empty()) {
                            throw new RuntimeException("ERROR! Incorrect formula, you may have missed \"(\"");
                        }
                        switch (operations.pop().getType()) {
                            case DIVIDE -> numbers.add(one / two);
                            case MULTIPLY -> numbers.add(one * two);
                            case PLUS -> numbers.add(one + two);
                            case MINUS -> numbers.add(one - two);
                        }
                    }
                    operations.pop();
                }
            }
            pos++;
        }
        while (!operations.empty()) {
            double two = numbers.pop();
            double one = numbers.pop();
            switch (operations.pop().getType()) {
                case DIVIDE -> numbers.add(one / two);
                case MULTIPLY -> numbers.add(one * two);
                case PLUS -> numbers.add(one + two);
                case MINUS -> numbers.add(one - two);
            }
        }
        usedCells.remove(cell);
        return numbers.pop();
    }


    public Cell getCell(String address) {
        try {
            int pos = 0;
            int x = 0;
            int y = 0;
            while (pos < address.length()) {
                char c = address.charAt(pos);
                if (c >= 65 && c <= 90) {
                    y = y * 26 + (c - 64);//letters
                }
                if (c >= 48 && c <= 57) {
                    x = x * 10 + (c - 48);//numbers
                }
                pos++;
            }
            if (x == 0 || y == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            return table[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException(" ERROR! Cell with this address doesn't exist in the table");
        }
    }

    public Cell getCell(int x, int y) {
        return table[x][y];
    }

    public String getCellAddress(Cell cell) {
        return cell.getAddress();
    }

    public int getRowsSize() {
        return rowsSize;
    }

    public int getColumnSize() {
        return columnSize;
    }


}

