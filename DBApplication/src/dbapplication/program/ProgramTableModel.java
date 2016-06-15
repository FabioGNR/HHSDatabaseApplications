package dbapplication.program;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author RLH
 */
public class ProgramTableModel extends AbstractTableModel {

    
    private ArrayList<ExProgram> searchProgram = new ArrayList<>();
    private final String[] internshipColumnNames = new String[]{"Name", "Institute", "Max Credit"};
    private final String[] studyProgramColumnNames
            = new String[]{"Name", "Institute", "Max Credit", "Type", "Study Code"};

    public ProgramTableModel() {
        super();
    }

    public void setResults(ArrayList<ExProgram> results) {
        searchProgram = results;
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return searchProgram.size();
    }

    @Override
    public int getColumnCount() {
        return internshipColumnNames.length;
    }
    
    public int getColumnCount(boolean internshipSelected){
        if(internshipSelected == true){
            return internshipColumnNames.length;
        }return studyProgramColumnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return internshipColumnNames[column];
    }
    
    public ExProgram getProgramAt(int rowIndex) {
        return searchProgram.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return searchProgram.get(rowIndex).getDataAt(columnIndex);
    }

}
