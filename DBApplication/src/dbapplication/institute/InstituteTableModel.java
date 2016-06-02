package dbapplication.institute;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Sishi
 */
public class InstituteTableModel extends AbstractTableModel{
    private ArrayList<Institute> searchResults = new ArrayList<>();
    private final String[] columnNames = new String[] { "org_id", "city", "name", "country", "address", "is_business" };
    public InstituteTableModel() {
        super();
    }
    
    public void setResults(ArrayList<Institute> results) {
        searchResults = results;
        this.fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return searchResults.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return searchResults.get(rowIndex).getDataAt(columnIndex);
    }
    
}