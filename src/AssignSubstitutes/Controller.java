package AssignSubstitutes;

import AssignSubstitutes.InputOutput.IO;
import AssignSubstitutes.InputOutput.XMLParser;
import AssignSubstitutes.Settings.SettingsController;
import AssignSubstitutes.classes.Assignment;
import AssignSubstitutes.classes.OnStaffTeacher;
import AssignSubstitutes.classes.SupplyTeacher;
import AssignSubstitutes.classes.Teacher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Controller {

    private XMLParser settings;
    private ArrayList<OnStaffTeacher> osTeachers;
    private Map<LocalDate, ArrayList<Assignment>> assignments;
    private Map<LocalDate, ArrayList<Assignment>> unsavedAssignments;

    private ArrayList<OnStaffTeacher> absences;
    private ArrayList<SupplyTeacher> supplies;

    private ArrayList<LocalDate> generated;
    private boolean noNagSaveWithEmptyAssignments, noNagOverwriteAssignmentChanges, noNagOverwriteSave;
    @FXML private TableView<Assignment> tblAssignments;
    @FXML private TableColumn<Assignment, String> colAssignAbsent, colAssignDelete;
    @FXML private TableColumn<Assignment, Teacher> colAssignSub;
    @FXML private TableColumn<Assignment, Integer> colAssignPeriod;
    @FXML private TableView<OnStaffTeacher> tblCoverage;
    @FXML private TableColumn<OnStaffTeacher, String> colCovTeacher;
    @FXML private TableColumn<OnStaffTeacher, Integer> colCovWeek, colCovMonth, colCovTotal;
    @FXML private TableView<ArrayList<Object>> tblAvailability;
    @FXML private TableColumn<ArrayList<Object>, String> colAvailPeriod, colAvailWeek, colAvailMonth;
    @FXML private DatePicker datePicker;
    @FXML private Button btnGenerate, btnSave;

    @FXML
    public void initialize(){
        assignments =Collections.synchronizedMap(new HashMap<LocalDate, ArrayList<Assignment>>());
        unsavedAssignments = Collections.synchronizedMap(new HashMap<LocalDate, ArrayList<Assignment>>());
        generated = new ArrayList<>();

        //TODO: make sure that child stages are brought to front when visible, when parent stages are made active (relevant for error dialogs on load)
        //TODO: add reset reminder once implemented in settingsUI and XMLParser/Settings
        try {
            settings = new XMLParser();
        }catch (Exception e){
            errorHandler("XML config file could not be found");
        }

        //TODO: check for empty input paths and notify user rather than calling IO
        try {
            osTeachers = IO.readTeachers(settings.getMasterSchedulePath());
            for(OnStaffTeacher t: osTeachers){
                System.out.println("osTeacher: "+t+" schedule "+Arrays.toString(t.getSchedule()));
            }
        }catch (Exception e){
            errorHandler("Master Schedule file could not be found at" + settings.getMasterSchedulePath());
        }

        try {
            supplies = IO.readSupplies(settings.getSupplyTeacherPath());
            for(Teacher t: supplies){
                System.out.println("supplies: "+t+" schedule "+(t.getSchedule()==null ? "null" : Arrays.toString(t.getSchedule())));
            }
        }catch (Exception e){
            errorHandler("Supply Teacher file could not be found at" + settings.getSupplyTeacherPath());
        }

        try {
            absences = IO.readAbsences(settings.getAbsenceInputPath());
            for(Teacher t: absences){
                System.out.println("absences: "+t+" schedule "+(t.getSchedule()==null ? "null" : Arrays.toString(t.getSchedule())));
            }
        }catch (Exception e){
            errorHandler("Absences file could not be found at" + settings.getAbsenceInputPath());
        }

        //TODO: get noNag booleans from settings
        noNagOverwriteAssignmentChanges=false;
        noNagSaveWithEmptyAssignments=false;

        btnSave.setVisible(false);

        datePicker.setValue(LocalDate.now());

        buildAssignmentsTable();
        buildCoverageTable();
        if(osTeachers!=null) {
            tblCoverage.setItems(FXCollections.observableArrayList(osTeachers));
        }
        buildAvailabilityTable();
        try {
            ObservableList<ArrayList<Object>> availabilityByPeriod = InformationHandle.getAvailabilityStats(osTeachers);
            tblAvailability.setItems(availabilityByPeriod);
        }catch(Exception e){
            errorHandler(e.getMessage());
            //TODO: make alternative errorhandlers (USER and StackFrame)
            //TODO: improve error messages
        }

    }

    @FXML
    private void changeDate(){
        LocalDate date = datePicker.getValue();

        if(date.isBefore(LocalDate.now())) {
            btnGenerate.setVisible(false);
            btnSave.setVisible(false);
            tblAssignments.setEditable(false);
            ArrayList<Assignment> prevAssignments = IOTest.getAssignmentByDate(date, osTeachers);
            displayAssignments(prevAssignments);
        }else{
            btnGenerate.setVisible(true);
            if(generated.contains(date)) {
                btnSave.setVisible(true);
            }else{
                btnSave.setVisible(false);
            }
            tblAssignments.setEditable(true);
            displayAssignments(unsavedAssignments.get(date));
        }
    }

    @FXML
    private void clickGenerateAssignments(){
        LocalDate date = datePicker.getValue();
        ArrayList<Assignment> currentAssignments;
        ArrayList<Assignment> currentUnsavedAssignments;
        if(!generated.contains(date)){
            generated.add(date);
            btnSave.setVisible(true);
        }else{
            //check to see if it has already been generated.
            currentAssignments=assignments.get(date);
            currentUnsavedAssignments=unsavedAssignments.get(date);
            if(!noNagOverwriteAssignmentChanges&&!currentAssignments.equals(currentUnsavedAssignments)){
                boolean[] nagCheck={noNagOverwriteAssignmentChanges};
                ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.APPLY);
                ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert alert = createConfirmAlertWithOptOut("Changes detected!", "Are you sure that you want to overwrite the changes that you've made to the generated assignments?", nagCheck, buttonTypeYes, buttonTypeNo);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonTypeNo) {
                    return;
                }else{
                    noNagOverwriteAssignmentChanges = nagCheck[0];
                    //TODO: write noNag to settings
                    //TODO: get Absentees and Supplies
                }
            }
        }

        currentAssignments = InformationHandle.generateAssignments(osTeachers, supplies, absences);

        assignments.put(date, currentAssignments);
        currentUnsavedAssignments = new ArrayList<>();
        unsavedAssignments.put(date, currentUnsavedAssignments);
        for(Assignment a : currentAssignments) {
            currentUnsavedAssignments.add(new Assignment(a.getAbsentee(), a.getSubstitute(), a.getPeriod()));
        }
        tblAssignments.getItems().setAll(currentUnsavedAssignments);
    }

    @FXML
    private void clickSave(){
        LocalDate date = datePicker.getValue();
        ObservableList<Assignment> tblItems = tblAssignments.getItems();
        //check for empty assignments
        if(!noNagSaveWithEmptyAssignments && tblItems.stream().anyMatch(a-> a.getAbsentee().getName().isEmpty())){
            boolean[] nagCheck={noNagSaveWithEmptyAssignments};
            ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.APPLY);
            ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = createConfirmAlertWithOptOut("Blank assignments!", "Are you sure that you want to save when there are abscences that have not been assigned substitutes?", nagCheck, buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == buttonTypeNo) {
                return;
            }else{
                noNagSaveWithEmptyAssignments = nagCheck[0];
                //TODO: write noNag to settings
            }
        }
        assignments.put(date, new ArrayList<>(tblItems));
        //TODO: call IO to write assignments to file.
        if(date == LocalDate.now()) {
            //TODO: increment tallys
            //TODO: update availability
        }
    }

    @FXML
    private void clickSettings() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings/SettingsUI.fxml"));

        try {
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("AssignSubstitutes.classes.Settings");
            stage.setScene(new Scene(root1));

            //get the controller and send it the stage
            SettingsController controller = fxmlLoader.getController();
            controller.setStage(stage);

            //add a listener to run on Settings dialog close
            stage.setOnCloseRequest(event -> controller.unload(event));

            //show
            stage.showAndWait();

            boolean saved = controller.getSaved();
            System.out.println("Saved: " + saved);
        }catch(Exception e){
            errorHandler(e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayAssignments(Collection<Assignment> assignmentsIn){
        if(assignmentsIn!=null) {
            tblAssignments.getItems().setAll(assignmentsIn);
        }else{
            tblAssignments.getItems().setAll(new ArrayList<>());
        }
    }

    private void buildAssignmentsTable() {
        colAssignAbsent.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Assignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Assignment, String> assignment) {
                return new SimpleObjectProperty<>(assignment.getValue().getAbsentee().getName());
            }
        });

        //TODO: Make combobox always visible
        colAssignSub.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Assignment, Teacher>,
                ObservableValue<Teacher>>() {
            @Override
            public ObservableValue<Teacher> call(TableColumn.CellDataFeatures<Assignment, Teacher> param) {
                return new SimpleObjectProperty<>(param.getValue().getSubstitute());
            }
        });

        colAssignSub.setCellFactory(t -> new ComboBoxTableCell<Assignment, Teacher>(FXCollections.observableArrayList()) {
            @Override
            public void startEdit() {
                Assignment currentRow = getTableRow().getItem();
                getItems().setAll(InformationHandle.getAssignableTeacherList(osTeachers, getTableView().getItems(),
                        currentRow.getPeriod().getPeriodNumber(), currentRow));
                super.startEdit();
            }
        });

        colAssignSub.setOnEditCommit((TableColumn.CellEditEvent<Assignment, Teacher> event) -> {
            TablePosition<Assignment, Teacher> pos = event.getTablePosition();
            System.out.println("onEditCommit");
            Teacher newTeacher = event.getNewValue();

            int row = pos.getRow();
            Assignment assignment = event.getTableView().getItems().get(row);

            assignment.setSubstitute(newTeacher);
        });

        colAssignPeriod.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Assignment, Integer>,
                ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Assignment, Integer> assignment) {
                return new SimpleObjectProperty<>(assignment.getValue().getPeriod().getPeriodNumber());
            }
        });

        colAssignDelete.setCellFactory(new Callback<TableColumn<Assignment,String>,TableCell<Assignment,String>>(){
            @Override
            public TableCell<Assignment, String> call(final TableColumn<Assignment, String> param) {
                final TableCell<Assignment, String> cell = new TableCell<Assignment, String>() {

                    final Button btn = new Button("x");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                getTableView().getItems().remove(getTableRow().getItem());
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void buildCoverageTable() {

        colCovTeacher.setCellValueFactory(new PropertyValueFactory<>("name"));

        colCovWeek.setCellValueFactory(new PropertyValueFactory<>("WeeklyTally"));

        colCovMonth.setCellValueFactory(new PropertyValueFactory<>("MonthlyTally"));

        colCovTotal.setCellValueFactory(new PropertyValueFactory<>("totalTally"));
    }

    private void buildAvailabilityTable() {
        colAvailPeriod.setCellValueFactory(arrayList -> new SimpleObjectProperty<>((String)arrayList.getValue().get(0)));

        colAvailWeek.setCellFactory(new Callback<>() {
            @Override
            public TableCell<ArrayList<Object>, String> call(final TableColumn<ArrayList<Object>, String> param) {

                final TableCell<ArrayList<Object>, String> cell = new
                        TableCell<>() {

                    final ComboBox<OnStaffTeacher> comboBox = new ComboBox();

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            List<OnStaffTeacher> teachers = (List<OnStaffTeacher>)getTableRow().getItem().get(1);
                            comboBox.setItems(FXCollections.observableArrayList(teachers));
                            comboBox.getSelectionModel().selectFirst();
                            setGraphic(comboBox);
                            //TODO: on select, selectFirst
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });

        colAvailMonth.setCellFactory(new Callback<>() {
            @Override
            public TableCell<ArrayList<Object>, String> call(final TableColumn<ArrayList<Object>, String> param) {

                final TableCell<ArrayList<Object>, String> cell = new
                        TableCell<>() {

                            final ComboBox<OnStaffTeacher> comboBox = new ComboBox();

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    List<OnStaffTeacher> teachers = (List<OnStaffTeacher>)getTableRow().getItem().get(2);
                                    comboBox.setItems(FXCollections.observableArrayList(teachers));
                                    comboBox.getSelectionModel().selectFirst();
                                    setGraphic(comboBox);
                                    setText(null);
                                }
                            }
                        };
                return cell;
            }
        });
    }

    private static Alert createConfirmAlertWithOptOut(String title, String headerText,
                                                            boolean[] selected,
                                                            ButtonType... buttonTypes) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().applyCss();
        Node graphic = alert.getDialogPane().getGraphic();
        alert.setDialogPane(new DialogPane() {
            @Override
            protected Node createDetailsButton() {
                CheckBox optOut = new CheckBox();
                optOut.setText("Don't show me this again");
                //optOut.setOnAction(e->nagChangeHandler(optOut, selected));
                optOut.setOnAction(event -> selected[0] = optOut.isSelected());
                return optOut;
            }
        });
        alert.getDialogPane().getButtonTypes().addAll(buttonTypes);
        alert.getDialogPane().setExpandableContent(new Group());
        alert.getDialogPane().setExpanded(true);
        alert.getDialogPane().setGraphic(graphic);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.initStyle(StageStyle.UTILITY);
        return alert;
    }

    private void errorHandler(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(msg);
        alert.initStyle(StageStyle.UTILITY);
        alert.show();
    }
}