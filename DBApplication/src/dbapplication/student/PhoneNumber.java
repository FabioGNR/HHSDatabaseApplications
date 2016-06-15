package dbapplication.student;

import dbapplication.DatabaseTableClass;

/**
 *
 * @author fabio
 */
public class PhoneNumber extends DatabaseTableClass {
    private String number;
    private boolean isCellular;
    private String[] cellData;
    public PhoneNumber(String number, boolean cellular) {
        this.number = number;
        this.isCellular = cellular;
        refreshCellData();
    }

    public void refreshCellData() {
        cellData = new String[] {number, isCellular ? "Yes" : "No" };
    }
    
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isCellular() {
        return isCellular;
    }

    public void setCellular(boolean isCellular) {
        this.isCellular = isCellular;
    }

    @Override
    public String getDataAt(int cell) {
        return cellData[cell];
    }
    
}
