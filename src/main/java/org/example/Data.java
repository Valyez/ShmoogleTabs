package org.example;

import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class Data {
    private Page page;

    public Data() {
        page = new Page(8, 8);
    }

    public void calculatePage() {
        for (int i = 1; i < page.getColumnSize(); i++) {
            for (int j = 1; j < page.getRowsSize(); j++) {
                Cell cell = page.getCell(i, j);
                if (cell.getLexemes() != null) {
                    cell.setInteger(String.valueOf(page.calculateCell(cell)));
                    page.usedCells = new HashSet<>();
                }
            }
        }
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
