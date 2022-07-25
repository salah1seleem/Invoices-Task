package com.mycompany.Fwd_firstproject_invoices.model;

import com.mycompany.Fwd_firstproject_invoices.view.Frame;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class HeaderTable extends AbstractTableModel {

    private ArrayList<Header> invoicesArray;
    private String[] columns = {"Invoice Number", "Invoice Date", "Customer Name", "Invoice Total"};
    
    public HeaderTable(ArrayList<Header> invoicesArray) {
        this.invoicesArray = invoicesArray;
    }

    @Override
    public int getRowCount() {
        return invoicesArray.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Header inv = invoicesArray.get(rowIndex);
        switch (columnIndex) {
            case 0: return inv.getNum();
            case 1: return Frame.dateFormat.format(inv.getInvDate());
            case 2: return inv.getCustomer();
            case 3: return inv.getInvoiceTotal();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
}
