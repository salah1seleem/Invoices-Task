package com.mycompany.Fwd_firstproject_invoices.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class LinesTable extends AbstractTableModel {

    private final ArrayList<Lines> linesArray;
    private final String[] columns = {"Item Name", "Unit Price", "Count", "Line Total"};

    public LinesTable(ArrayList<Lines> linesArray) {
        this.linesArray = linesArray;
    }

    @Override
    public int getRowCount() {
        return linesArray == null ? 0 : linesArray.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (linesArray == null) {
            return "";
        } else {
            Lines line = linesArray.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return line.getItem();
                case 1:
                    return line.getPrice();
                case 2:
                    return line.getCount();
                case 3:
                    return line.getLineTotal();
                default:
                    return "";
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

}
