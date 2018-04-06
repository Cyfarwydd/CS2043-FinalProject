package AssignSubstitutes.Settings;

import AssignSubstitutes.InputOutput.XMLParser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.*;

import java.io.File;
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
    private XMLParser settings;

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
        String location = getFilePath();
        if(location!=null) {
            txtMasterSchedule.setText(location);
        }
    }

    @FXML
    private void browseAbsenceList(){
        String location = getFilePath();
        if(location!=null) {
            txtAbsenceList.setText(location);
        }
    }

    @FXML
    private void browseCourseCodes(){
        String location = getFilePath();
        if(location!=null) {
            txtCourseCodes.setText(location);
        }
    }

    @FXML
    private void browseSupplies(){
        String location = getFilePath();
        if(location!=null) {
            txtSupplies.setText(location);
        }
    }

    @FXML
    private void browseOnCallerDir(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        txtOnCallerDir.setText(file.getPath());
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

        System.out.println("settingsMenu width: "+settingsMenu.getWidth());
        System.out.println("panelContainer width: "+panelContainer.getWidth());
        System.out.println("panelMaxOnCalls width: "+panelMaxOnCalls.getWidth());
        System.out.println("panelContainer height: "+panelContainer.getHeight());
        System.out.println("panelMaxOnCalls height: "+panelMaxOnCalls.getHeight());
        System.out.println("panelMaxOnCalls label height: "+((Label)(panelMaxOnCalls.getChildren().get(4))).getHeight());
        int newIndex = settingsMenu.getItems().indexOf(newValue);
        panelContainer.getChildren().add(panels[newIndex]);
        panelContainer.getChildren().remove(0);
    }

    private String getFilePath(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        return file == null ? null : file.getPath();
    }

    private void populateSettingsFields(){
        try {
            settings = new XMLParser();
            txtTmpMaxMnth.setText(Integer.toString(settings.getTempMonthlyMax()));
            txtTmpMaxWeek.setText(Integer.toString(settings.getTempWeeklyMax()));
            txtPermMaxMnth.setText(Integer.toString(settings.getMaxMonthlyTally()));
            txtPermMaxWeek.setText(Integer.toString(settings.getMaxWeeklyTally()));

            dpStartDate.setValue(settings.getStartDate());
            txtWeeksToReminder.setText(Integer.toString(settings.getWeeksToReminder()));

            chkboxNoNagOverwriteAssignmentChanges.selectedProperty().setValue(settings.isNoNagOverwriteAssignmentChanges());

            chkboxNoNagOverwriteSave.selectedProperty().setValue(settings.isNoNagOverwriteSave());
            chkboxNoNagSaveWithEmptyAssignments.selectedProperty().setValue(settings.isNoNagSaveWithEmptyAssignments());

            txtMasterSchedule.setText(settings.getMasterSchedulePath());
            txtSupplies.setText(settings.getSupplyTeacherPath());
            txtAbsenceList.setText(settings.getAbsenceInputPath());
            txtCourseCodes.setText(settings.getCourseCodesPath());

            txtOnCallerDir.setText(settings.getOnCallerFormPath());
            txtFormatOut.setText(settings.getOnCallerFormNameFormat());

            System.out.println("populate settings");
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

    private boolean changesMade(){
        if(
                !txtPermMaxWeek.getText().equals(String.format("%d", settings.getMaxWeeklyTally())) ||
                !txtTmpMaxWeek.getText().equals(String.format("%d", settings.getTempWeeklyMax())) ||
                !txtPermMaxMnth.getText().equals(String.format("%d", settings.getMaxMonthlyTally())) ||
                !txtTmpMaxMnth.getText().equals(String.format("%d", settings.getTempMonthlyMax())) ||
                !dpStartDate.getValue().equals(settings.getStartDate()) ||
                !txtWeeksToReminder.getText().equals(String.format("%d", settings.getWeeksToReminder())) ||
                chkboxNoNagSaveWithEmptyAssignments.isSelected() != settings.isNoNagSaveWithEmptyAssignments() ||
                chkboxNoNagOverwriteSave.isSelected() != settings.isNoNagOverwriteSave() ||
                chkboxNoNagOverwriteAssignmentChanges.isSelected() != settings.isNoNagOverwriteAssignmentChanges() ||
                !txtMasterSchedule.getText().equals(settings.getMasterSchedulePath()) ||
                !txtCourseCodes.getText().equals(settings.getCourseCodesPath()) ||
                !txtAbsenceList.getText().equals(settings.getAbsenceInputPath()) ||
                !txtSupplies.getText().equals(settings.getSupplyTeacherPath()) ||
                !txtOnCallerDir.getText().equals(settings.getOnCallerFormPath()) ||
                !txtFormatOut.getText().equals(settings.getOnCallerFormNameFormat())
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
                    Integer.parseInt(txtPermMaxWeek.getText()) != settings.getMaxWeeklyTally()
                )
            {
                settings.setMaxWeeklyTally(Integer.parseInt(txtPermMaxWeek.getText()));
            }
            //temp weekly tally
            if (!txtTmpMaxWeek.getText().isEmpty() ||
                    Integer.parseInt(txtTmpMaxWeek.getText()) != settings.getTempWeeklyMax()
                )
            {
                if (txtTmpMaxWeek.getText().isEmpty()) {
                    settings.setTempMonthlyMax(settings.getMaxWeeklyTally());
                } else {
                    settings.setTempWeeklyMax(Integer.parseInt(txtTmpMaxWeek.getText()));
                }
            }
            //permanent monthly tally
            if (!txtPermMaxMnth.getText().isEmpty() &&
                    Integer.parseInt(txtPermMaxMnth.getText()) != settings.getMaxMonthlyTally()
                )
            {
                settings.setMaxMonthlyTally(Integer.parseInt(txtPermMaxMnth.getText()));
            }
            //temp monthly tally
            if (!txtPermMaxMnth.getText().isEmpty() ||
                    Integer.parseInt(txtTmpMaxMnth.getText()) != settings.getTempMonthlyMax()
                )
            {
                if (txtTmpMaxMnth.getText().isEmpty()) {
                    settings.setTempMonthlyMax(settings.getMaxMonthlyTally());
                } else {
                    settings.setTempMonthlyMax(Integer.parseInt(txtTmpMaxMnth.getText()));
                }
            }
            //start date
            if(!dpStartDate.getValue().equals(settings.getStartDate())){
                settings.setStartDate(dpStartDate.getValue());
            }
            //weeks until reminder
            if(!txtWeeksToReminder.getText().isEmpty() &&
                    !txtWeeksToReminder.getText().equals(String.format("%d", settings.getWeeksToReminder()))
                )
            {
                settings.setWeeksToReminder(Integer.parseInt(txtWeeksToReminder.getText()));
            }
            //no reminder save with empty assignments
            if(chkboxNoNagSaveWithEmptyAssignments.isSelected() != settings.isNoNagSaveWithEmptyAssignments()){
                settings.setNoNagSaveWithEmptyAssignments(chkboxNoNagSaveWithEmptyAssignments.isSelected());
            }
            //no reminder overwrite save
            if(chkboxNoNagOverwriteSave.isSelected() != settings.isNoNagOverwriteSave()){
                settings.setNoNagOverwriteSave(chkboxNoNagOverwriteSave.isSelected());
            }
            //no reminder overwrite assignment changes
            if(chkboxNoNagOverwriteAssignmentChanges.isSelected() != settings.isNoNagOverwriteAssignmentChanges()){
                settings.setNoNagOverwriteAssignmentChanges(chkboxNoNagOverwriteAssignmentChanges.isSelected());
            }
            //master schedule path
            if(!txtMasterSchedule.getText().isEmpty() && !txtMasterSchedule.getText().equals(settings
                    .getMasterSchedulePath())){
                settings.setMasterSchedulePath(txtMasterSchedule.getText());
            }
            //Absence input path
            if(!txtAbsenceList.getText().isEmpty() && !txtAbsenceList.getText().equals(settings.getAbsenceInputPath())){
                settings.setAbsenceInputPath(txtAbsenceList.getText());
            }
            //Supply input path
            if(!txtSupplies.getText().isEmpty() && !txtSupplies.getText().equals(settings.getSupplyTeacherPath())){
                settings.setSupplyTeacherPath(txtSupplies.getText());
            }
            //course codes path
            if(!txtCourseCodes.getText().isEmpty() && !txtCourseCodes.getText().equals(settings.getCourseCodesPath())){
                settings.setCourseCodesPath(txtCourseCodes.getText());
            }
            //on-caller form output dir
            if(!txtOnCallerDir.getText().isEmpty() && !txtOnCallerDir.getText().equals(settings.getOnCallerFormPath())){
                settings.setOnCallerFormPath(txtOnCallerDir.getText());
            }
            //on-caller form name format
            if(!txtFormatOut.getText().isEmpty() && !txtFormatOut.getText().equals(settings.getOnCallerFormNameFormat())){
                settings.setOnCallerFormNameFormat(txtFormatOut.getText());
            }
        }catch(Exception e){
            errorHandler("ERROR: There was a problem writing settings to config file");
        }

    }
}
