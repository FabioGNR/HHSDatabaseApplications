package dbapplication.institute;

import java.sql.ResultSet;

/**
 *
 * @author fabio
 */
public class ContactPerson {
    private String email, phone;
    private int contactID;
    private String[] cellData;
    
    public ContactPerson(ResultSet set) throws Exception {
        contactID = set.getInt("id");
        email = set.getString("email");
        phone = set.getString("phonenr");
        cellData = new String[] {email, phone};
    }
    
    Object getDataAt(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
