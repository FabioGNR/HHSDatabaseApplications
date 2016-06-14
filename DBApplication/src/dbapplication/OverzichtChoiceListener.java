package dbapplication;

import javax.swing.JDialog;

/**
 *
 * @author Sishi
 */
public class OverzichtChoiceListener {
    
    private final JDialog countryDialog, cityDialog, studyDialog;
    
    public OverzichtChoiceListener(JDialog country, JDialog city, JDialog study) {
        countryDialog = country;
        cityDialog = city;
        studyDialog = study;
    }
    
    public void CountryClicked() {
        countryDialog.setVisible(true);
        countryDialog.toFront();
    }
    
    public void CityClicked() {
        cityDialog.setVisible(true);
        cityDialog.toFront();
    }
    
    public void StudyClicked() {
        studyDialog.setVisible(true);     
        studyDialog.toFront();
    }
}
