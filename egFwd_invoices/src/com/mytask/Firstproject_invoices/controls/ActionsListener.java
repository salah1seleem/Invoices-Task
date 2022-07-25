package com.mycompany.Fwd_firstproject_invoices.controls;

import com.mycompany.Fwd_firstproject_invoices.model.Header;
import com.mycompany.Fwd_firstproject_invoices.model.HeaderTable;
import com.mycompany.Fwd_firstproject_invoices.model.Lines;
import com.mycompany.Fwd_firstproject_invoices.model.LinesTable;
import com.mycompany.Fwd_firstproject_invoices.view.Frame;
import com.mycompany.Fwd_firstproject_invoices.view.HeaderDialog;
import com.mycompany.Fwd_firstproject_invoices.view.LinesDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class ActionsListener implements ActionListener {

    private final Frame frame;
    private HeaderDialog headerDialog;
    private LinesDialog lineDialog;

    public ActionsListener(Frame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
            if ("Save Files".equals(e.getActionCommand()))
        {
            saveFiles();
        } 
            else if ("Load Files".equals(e.getActionCommand()))
        {
            loadFiles();    
        }
            else if ("Create New Invoice".equals(e.getActionCommand()))
                
        {
            createNewInvoice();    
        } 
        
            else if ("Delete Invoice".equals(e.getActionCommand()))
                
        {
            deleteInvoice();    
        }
            
            else if ("New Item".equals(e.getActionCommand()))
                
        {
            createNewLine();    
        }    
            
            else if ("Delete Item".equals(e.getActionCommand()))
                
        {
            deleteLine();    
        }    
            
            else if ("newInvoiceOK".equals(e.getActionCommand()))
                
        {
            newInvoiceDialogOK();   
        }
            
            else if ("newInvoiceCancel".equals(e.getActionCommand()))
                
        {
            newInvoiceDialogCancel();   
        }
        
            else if ("newLineCancel".equals(e.getActionCommand()))
                
        {
            newLineDialogCancel();   
        }
            
           else if ("newLineOK".equals(e.getActionCommand()))
                
        {
            newLineDialogOK();  
        }   
    }

    private void loadFiles() {
        JFileChooser fileChooser = new JFileChooser();
        try {
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fileChooser.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> inv_headerLines = Files.readAllLines(headerPath);
                ArrayList<Header> invoiceHeaders = new ArrayList<>();
                for (String headerLine : inv_headerLines) {
                    String[] arr = headerLine.split(",");
                    String str1 = arr[0];
                    String str2 = arr[1];
                    String str3 = arr[2];
                    int code = Integer.parseInt(str1);
                    Date invoiceDate = Frame.dateFormat.parse(str2);
                    Header header = new Header(code, str3, invoiceDate);
                    invoiceHeaders.add(header);
                }
                frame.setInvoicesArray(invoiceHeaders);

                result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fileChooser.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    ArrayList<Lines> invoiceLines = new ArrayList<>();
                    for (String lineLine : lineLines) {
                        String[] arr = lineLine.split(",");
                        int invCode = Integer.parseInt(arr[0]);
                        String name = arr[1];
                        double price = Double.parseDouble(arr[2]);
                        int count = Integer.parseInt(arr[3]);
                        Header inv = frame.getInvObject(invCode);
                        Lines line = new Lines(name, price, count, inv);
                        inv.getLines().add(line);
                    }
                }
                HeaderTable headerTableModel = new HeaderTable(invoiceHeaders);
                frame.setHeaderTableModel(headerTableModel);
                frame.getInvHTbl().setModel(headerTableModel);
                System.out.println("files read");
            }

        } catch (IOException | ParseException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createNewInvoice() {
        headerDialog = new HeaderDialog(frame);
        headerDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedInvoiceIndex = frame.getInvHTbl().getSelectedRow();
        if (selectedInvoiceIndex != -1) {
            frame.getInvoicesArray().remove(selectedInvoiceIndex);
            frame.getHeaderTableModel().fireTableDataChanged();

            frame.getInvLTbl().setModel(new LinesTable(null));
            frame.setLinesArray(null);
            frame.getCustNameLbl().setText("");
            frame.getInvNumLbl().setText("");
            frame.getInvTotalIbl().setText("");
            frame.getInvDateLbl().setText("");
        }
    }

    private void createNewLine() {
        lineDialog = new LinesDialog(frame);
        lineDialog.setVisible(true);
    }

    private void deleteLine() {
        int selectedLineIndex = frame.getInvLTbl().getSelectedRow();
        int selectedInvoiceIndex = frame.getInvHTbl().getSelectedRow();
        if (selectedLineIndex != -1) {
            frame.getLinesArray().remove(selectedLineIndex);
            LinesTable lineTableModel = (LinesTable) frame.getInvLTbl().getModel();
            lineTableModel.fireTableDataChanged();
            frame.getInvTotalIbl().setText("" + frame.getInvoicesArray().get(selectedInvoiceIndex).getInvoiceTotal());
            frame.getHeaderTableModel().fireTableDataChanged();
            frame.getInvHTbl().setRowSelectionInterval(selectedInvoiceIndex, selectedInvoiceIndex);
        }
    }

    private void saveFiles() {
        ArrayList<Header> invoicesArray = frame.getInvoicesArray();
        JFileChooser fc = new JFileChooser();
        try {
            int result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                String headers = "";
                String lines = "";
                for (Header invoice : invoicesArray) {
                    headers += invoice.toString();
                    headers += "\n";
                    for (Lines line : invoice.getLines()) {
                        lines += line.toString();
                        lines += "\n";
                    }
                }
            
                headers = headers.substring(0, headers.length()-1);
                lines = lines.substring(0, lines.length()-1);
                result = fc.showSaveDialog(frame);
                File lineFile = fc.getSelectedFile();
                FileWriter lfw = new FileWriter(lineFile);
                hfw.write(headers);
                lfw.write(lines);
                hfw.close();
                lfw.close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void newInvoiceDialogCancel() {
        headerDialog.setVisible(false);
        headerDialog.dispose();
        headerDialog = null;
    }

    private void newInvoiceDialogOK() {
        headerDialog.setVisible(false);

        String custName = headerDialog.getCustNameField().getText();
        String str = headerDialog.getInvDateField().getText();
        Date d = new Date();
        try {
            d = Frame.dateFormat.parse(str);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, "Cannot parse date, resetting to today.", "Invalid date format", JOptionPane.ERROR_MESSAGE);
        }

        int invNum = 0;
        for (Header inv : frame.getInvoicesArray()) {
            if (inv.getNum() > invNum) {
                invNum = inv.getNum();
            }
        }
        invNum++;
        Header newInv = new Header(invNum, custName, d);
        frame.getInvoicesArray().add(newInv);
        frame.getHeaderTableModel().fireTableDataChanged();
        headerDialog.dispose();
        headerDialog = null;
    }

    private void newLineDialogCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void newLineDialogOK() {
        lineDialog.setVisible(false);

        String name = lineDialog.getItemNameField().getText();
        String str1 = lineDialog.getItemCountField().getText();
        String str2 = lineDialog.getItemPriceField().getText();
        int item_count = 1;
        double item_price = 1;
        try {
            item_count = Integer.parseInt(str1);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Cannot convert number", "Invalid number format", JOptionPane.ERROR_MESSAGE);
        }

        try {
            item_price = Double.parseDouble(str2);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Cannot convert price", "Invalid number format", JOptionPane.ERROR_MESSAGE);
        }
        int selectedInvHeader = frame.getInvHTbl().getSelectedRow();
        if (selectedInvHeader != -1) {
            Header invHeader = frame.getInvoicesArray().get(selectedInvHeader);
            Lines line = new Lines(name, item_price, item_count, invHeader);
            //invHeader.getLines().add(line);
            frame.getLinesArray().add(line);
            LinesTable lineTableModel = (LinesTable) frame.getInvLTbl().getModel();
            lineTableModel.fireTableDataChanged();
            frame.getHeaderTableModel().fireTableDataChanged();
        }
        frame.getInvHTbl().setRowSelectionInterval(selectedInvHeader, selectedInvHeader);
        lineDialog.dispose();
        lineDialog = null;
    }

}
