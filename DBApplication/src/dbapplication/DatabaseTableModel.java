package dbapplication;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author fabio
 */
/**
 * @param <T> the type being displayed in this table
 */
public class DatabaseTableModel<T extends DatabaseTableClass> extends AbstractTableModel{
    private ArrayList<T> items = new ArrayList<>();
    private final String[] columnNames;
    public DatabaseTableModel(String[] columns) {
        super();
        columnNames = columns;
    }
    
    public void setItems(ArrayList<T> items) {
        this.items = items;
        this.fireTableDataChanged();
    }
    
    public void clear() {
        items = new ArrayList<>();
        this.fireTableDataChanged();
    }
    
    public ArrayList<T> getAll() {
        return items;
    }
    
    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    public T get(int rowIndex) {
        return items.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return items.get(rowIndex).getDataAt(columnIndex);
    }
    
}
