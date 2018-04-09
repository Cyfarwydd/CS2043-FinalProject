package AssignSubstitutes.Settings;

import AssignSubstitutes.InputOutput.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class SettingsController {
    @FXML private ListView settingsMenu;
    @FXML private StackPane panelContainer;
    @FXML private AnchorPane panelMaxOnCalls, panelOther, panelInputLocations, panelOutputLocations, panelSave;
    @FXML private AnchorPane[] panels;
    @FXML private TextField txtMasterSchedule, txtAbsenceList, txtCourseCodes, txtSupplies, txtOnCallerDir,
            txtFormatOut, txtTmpMaxWeek, txtTmpMaxMnth, txtPermMaxWeek, txtPermMaxMnth, txtWeeksToReminder;
    @FXML private DatePicker dpStartDate;
    @FXML private CheckBox chkboxNoNagOverwriteAssignmentChanges, chkboxNoNagSaveWithEmptyAssignments, chkboxNoNagOverwriteSave;

    private Stage stage;
    private boolean saved;

    @FXML
    public void initialize(){
        saved=false;
        //put the panels into an array to make them easier to reference later
        panels = new AnchorPane[5];
        panels[0] = panelMaxOnCalls;
        panels[1] = panelOther;
        panels[2] = panelInputLocations;
        panels[3] = panelOutputLocations;
        panels[4] = panelSave;

        //remove the unused panels
        for(int i=0; i<panels.length-1; i++) {
            panelContainer.getChildren().remove(0);
        }

        //select the first item in the settings menu
        settingsMenu.getSelectionModel().select(0);

        //add a listener to detect when the settings menu selection has been changed and switch the visible panel
        //  accordingly.
        settingsMenu.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable,
                                                                                                      oldValue, newValue) ->
                                                                                                            navSelectionChange(observable, oldValue, newValue));
        populateSettingsFields();
    }

    @FXML
    private void browseMasterSchedule(){
        String closestDirToStart = getClosestDir(txtMasterSchedule.getText(), txtSupplies.getText(),
                txtAbsenceList.getText(), txtCourseCodes.getText());
        String location = getFilePath(closestDirToStart);
        if(location!=null) {
            txtMasterSchedule.setText(location);
        }
    }

    @FXML
    private void browseAbsenceList(){
        String closestDirToStart = getClosestDir(txtAbsenceList.getText(), txtMasterSchedule.getText(), txtSupplies.getText(),
                txtCourseCodes.getText());
        String location = getFilePath(closestDirToStart);
        if(location!=null) {
            txtAbsenceList.setText(location);
        }
    }

    @FXML
    private void browseCourseCodes(){
        String closestDirToStart = getClosestDir(txtCourseCodes.getText(), txtAbsenceList.getText(),
                txtMasterSchedule.getText(), txtSupplies.getText());
        String location = getFilePath(closestDirToStart);
        if(location!=null) {
            txtCourseCodes.setText(location);
        }
    }

    @FXML
    private void browseSupplies(){
        String closestDirToStart = getClosestDir(txtSupplies.getText(), txtCourseCodes.getText(), txtAbsenceList.getText(),
                txtMasterSchedule.getText());
        String location = getFilePath(closestDirToStart);
        if(location!=null) {
            txtSupplies.setText(location);
        }
    }

    @FXML
    private void browseOnCallerDir(){
        String closestDirToStart = getClosestDir(txtOnCallerDir.getText());
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(closestDirToStart));
        File file = directoryChooser.showDialog(stage);
        if(file!=null) {
            txtOnCallerDir.setText(file.getPath());
        }
    }

    @FXML
    private void onlyInts(KeyEvent event){
        if(event==null){
            onlyInts(new KeyEvent(txtTmpMaxWeek, null, null, null, null, null, false,
                    false,false, false));
            onlyInts(new KeyEvent(txtTmpMaxMnth, null, null, null, null, null, false,
                    false,false, false));
            onlyInts(new KeyEvent(txtPermMaxWeek, null, null, null, null, null, false,
                    false,false, false));
            onlyInts(new KeyEvent(txtPermMaxWeek, null, null, null, null, null, false,
                    false,false, false));
            onlyInts(new KeyEvent(txtWeeksToReminder, null, null, null, null, null, false,
                    false,false, false));
            System.out.println("onlyInt 5 times");
            return;
        }
        TextField target = (TextField) event.getSource();
        removeUnwantedCharacters(target, "[^0-9]");
    }

    @FXML
    private void removeIllegalCharactersFromFileName(){
        removeUnwantedCharacters(txtFormatOut, "(\\\\|/|\\?|%|\\.|\\||\"|<|>|\\.| |:)");
    }

    @FXML
    private void resetSettings(){
        try {
            Settings.resetDefaults();
        }catch (Exception e){
            errorHandler("ERROR: resetting configuration file and settings");
        }
        populateSettingsFields();
    }

    @FXML
    private boolean saveSettings(){
        onlyInts(null);
        removeIllegalCharactersFromFileName();
        try {
            //modify Settings object
            finalizeChanges();
        }catch (Exception e){
            errorHandler("ERROR: There was a problem before trying to save the settings file");
        }
        saved=true;
        System.out.println("Save Settings");
        return true;
    }

    private void navSelectionChange(ObservableValue<? extends String> observable, String oldValue, String newValue){
        int newIndex = settingsMenu.getItems().indexOf(newValue);
        panelContainer.getChildren().add(panels[newIndex]);
        panelContainer.getChildren().remove(0);
    }

    private String getClosestDir(String thisTxtBox, String... otherTxtBoxes){
        String dir=validateDir(thisTxtBox);
        if (dir!=null) {
            return dir;
        }else{
            for(String t : otherTxtBoxes) {
                dir = validateDir(thisTxtBox);
                if (dir != null) {
                    return dir;
                }
            }
        }
        return System.getProperty("user.home");
    }
    private String validateDir(String path){
        File f=null;
        if(!path.isEmpty()){
            f = new File(path);
            if(f.exists()){
                if (!f.isDirectory()) {
                    f = f.getParentFile();
                }
            }
        }
        if (f!=null && f.isDirectory()) {
            return f.getPath();
        }else{
            return null;
        }
    }
    private String getFilePath(String startingPoint){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(startingPoint));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        return file == null ? null : file.getPath();
    }

    private void populateSettingsFields(){
        try {
            //settings = new Settings();
            Settings.init();
            txtTmpMaxMnth.setText(Integer.toString(Settings.getTempMonthlyMax()));
            txtTmpMaxWeek.setText(Integer.toString(Settings.getTempWeeklyMax()));
            txtPermMaxMnth.setText(Integer.toString(Settings.getMaxMonthlyTally()));
            txtPermMaxWeek.setText(Integer.toString(Settings.getMaxWeeklyTally()));

            dpStartDate.setValue(Settings.getStartDate());
            txtWeeksToReminder.setText(Integer.toString(Settings.getWeeksToReminder()));

            chkboxNoNagOverwriteAssignmentChanges.selectedProperty().setValue(Settings.isNoNagOverwriteAssignmentChanges());

            chkboxNoNagOverwriteSave.selectedProperty().setValue(Settings.isNoNagOverwriteSave());
            chkboxNoNagSaveWithEmptyAssignments.selectedProperty().setValue(Settings.isNoNagSaveWithEmptyAssignments());

            txtMasterSchedule.setText(Settings.getMasterSchedulePath());
            txtSupplies.setText(Settings.getSupplyTeacherPath());
            txtAbsenceList.setText(Settings.getAbsenceInputPath());
            txtCourseCodes.setText(Settings.getCourseCodesPath());

            txtOnCallerDir.setText(Settings.getOnCallerFormPath());
            txtFormatOut.setText(Settings.getOnCallerFormNameFormat());

            System.out.println("populate Settings");
        }catch (Exception e){
            errorHandler(e.getMessage());
        }
    }

    private void errorHandler(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(msg);
        alert.initStyle(StageStyle.UTILITY);
        alert.show();
    }

    public void setStage(Stage stage){
        this.stage=stage;
    }

    public boolean getSaved(){
        return saved;
    }

    public void unload(WindowEvent event){
        try {
            if (changesMade()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Save changes before closing?");
                alert.setHeaderText("Changes to your settings configuration were detected. ");
                alert.initStyle(StageStyle.UTILITY);

                ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.APPLY);
                ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonTypeYes) {
                    if (!saveSettings()) {
                        event.consume();
                    }
                }
            }
        }catch(Exception e){
            errorHandler("ERROR: checking for changes in settings before closing window");
        }
    }

    private boolean changesMade() throws TransformerException, SAXException, ParserConfigurationException, IOException {
        if(
                !txtPermMaxWeek.getText().equals(String.format("%d", Settings.getMaxWeeklyTally())) ||
                !txtTmpMaxWeek.getText().equals(String.format("%d", Settings.getTempWeeklyMax())) ||
                !txtPermMaxMnth.getText().equals(String.format("%d", Settings.getMaxMonthlyTally())) ||
                !txtTmpMaxMnth.getText().equals(String.format("%d", Settings.getTempMonthlyMax())) ||
                !dpStartDate.getValue().equals(Settings.getStartDate()) ||
                !txtWeeksToReminder.getText().equals(String.format("%d", Settings.getWeeksToReminder())) ||
                chkboxNoNagSaveWithEmptyAssignments.isSelected() != Settings.isNoNagSaveWithEmptyAssignments() ||
                chkboxNoNagOverwriteSave.isSelected() != Settings.isNoNagOverwriteSave() ||
                chkboxNoNagOverwriteAssignmentChanges.isSelected() != Settings.isNoNagOverwriteAssignmentChanges() ||
                !txtMasterSchedule.getText().equals(Settings.getMasterSchedulePath()) ||
                !txtCourseCodes.getText().equals(Settings.getCourseCodesPath()) ||
                !txtAbsenceList.getText().equals(Settings.getAbsenceInputPath()) ||
                !txtSupplies.getText().equals(Settings.getSupplyTeacherPath()) ||
                !txtOnCallerDir.getText().equals(Settings.getOnCallerFormPath()) ||
                !txtFormatOut.getText().equals(Settings.getOnCallerFormNameFormat())
        ){
            return true;
        }else{
            return false;
        }
    }

    private void removeUnwantedCharacters(TextField target, String charactersToRemove){
        int cPos = target.getText().length()-target.getCaretPosition();
        target.setText(target.getText().replaceAll(charactersToRemove, ""));
        target.positionCaret(target.getText().length()-cPos);
    }

    private void finalizeChanges(){
        try {
            //permanent weekly tally
            if (!txtPermMaxWeek.getText().isEmpty() &&
                    Integer.parseInt(txtPermMaxWeek.getText()) != Settings.getMaxWeeklyTally()
                )
            {
                Settings.setMaxWeeklyTally(Integer.parseInt(txtPermMaxWeek.getText()));
            }
            //temp weekly tally
            if (!txtTmpMaxWeek.getText().isEmpty() ||
                    Integer.parseInt(txtTmpMaxWeek.getText()) != Settings.getTempWeeklyMax()
                )
            {
                if (txtTmpMaxWeek.getText().isEmpty()) {
                    Settings.setTempMonthlyMax(Settings.getMaxWeeklyTally());
                } else {
                    Settings.setTempWeeklyMax(Integer.parseInt(txtTmpMaxWeek.getText()));
                }
            }
            //permanent monthly tally
            if (!txtPermMaxMnth.getText().isEmpty() &&
                    Integer.parseInt(txtPermMaxMnth.getText()) != Settings.getMaxMonthlyTally()
                )
            {
                Settings.setMaxMonthlyTally(Integer.parseInt(txtPermMaxMnth.getText()));
            }
            //temp monthly tally
            if (!txtPermMaxMnth.getText().isEmpty() ||
                    Integer.parseInt(txtTmpMaxMnth.getText()) != Settings.getTempMonthlyMax()
                )
            {
                if (txtTmpMaxMnth.getText().isEmpty()) {
                    Settings.setTempMonthlyMax(Settings.getMaxMonthlyTally());
                } else {
                    Settings.setTempMonthlyMax(Integer.parseInt(txtTmpMaxMnth.getText()));
                }
            }
            //start date
            if(!dpStartDate.getValue().equals(Settings.getStartDate())){
                Settings.setStartDate(dpStartDate.getValue());
            }
            //weeks until reminder
            if(!txtWeeksToReminder.getText().isEmpty() &&
                    !txtWeeksToReminder.getText().equals(String.format("%d", Settings.getWeeksToReminder()))
                )
            {
                Settings.setWeeksToReminder(Integer.parseInt(txtWeeksToReminder.getText()));
            }
            //no reminder save with empty assignments
            if(chkboxNoNagSaveWithEmptyAssignments.isSelected() != Settings.isNoNagSaveWithEmptyAssignments()){
                Settings.setNoNagSaveWithEmptyAssignments(chkboxNoNagSaveWithEmptyAssignments.isSelected());
            }
            //no reminder overwrite save
            if(chkboxNoNagOverwriteSave.isSelected() != Settings.isNoNagOverwriteSave()){
                Settings.setNoNagOverwriteSave(chkboxNoNagOverwriteSave.isSelected());
            }
            //no reminder overwrite assignment changes
            if(chkboxNoNagOverwriteAssignmentChanges.isSelected() != Settings.isNoNagOverwriteAssignmentChanges()){
                Settings.setNoNagOverwriteAssignmentChanges(chkboxNoNagOverwriteAssignmentChanges.isSelected());
            }
            //master schedule path
            if(!txtMasterSchedule.getText().isEmpty() && !txtMasterSchedule.getText().equals(Settings
                    .getMasterSchedulePath())){
                Settings.setMasterSchedulePath(txtMasterSchedule.getText());
            }
            //Absence input path
            if(!txtAbsenceList.getText().isEmpty() && !txtAbsenceList.getText().equals(Settings.getAbsenceInputPath())){
                Settings.setAbsenceInputPath(txtAbsenceList.getText());
            }
            //Supply input path
            if(!txtSupplies.getText().isEmpty() && !txtSupplies.getText().equals(Settings.getSupplyTeacherPath())){
                Settings.setSupplyTeacherPath(txtSupplies.getText());
            }
            //course codes path
            if(!txtCourseCodes.getText().isEmpty() && !txtCourseCodes.getText().equals(Settings.getCourseCodesPath())){
                Settings.setCourseCodesPath(txtCourseCodes.getText());
            }
            //on-caller form output dir
            if(!txtOnCallerDir.getText().isEmpty() && !txtOnCallerDir.getText().equals(Settings.getOnCallerFormPath())){
                Settings.setOnCallerFormPath(txtOnCallerDir.getText());
            }
            //on-caller form name format
            if(!txtFormatOut.getText().isEmpty() && !txtFormatOut.getText().equals(Settings.getOnCallerFormNameFormat())){
                Settings.setOnCallerFormNameFormat(txtFormatOut.getText());
            }
        }catch(Exception e){
            errorHandler("ERROR: There was a problem writing settings to config file");
        }

    }
}
