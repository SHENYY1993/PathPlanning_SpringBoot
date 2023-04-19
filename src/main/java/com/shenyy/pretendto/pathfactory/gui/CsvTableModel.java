package com.shenyy.pretendto.pathfactory.gui;

public class CsvTableModel extends javax.swing.table.AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private String[][] data;

    public CsvTableModel(String[][] data) {
        this.data = data;
    }

    public int getColumnCount() {
        return data[0].length;
    }

    public int getRowCount() {
        return data.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public String getColumnName(int columnIndex) {
        return "Column " + (columnIndex + 1);
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        // Do nothing, table is not editable
    }
}