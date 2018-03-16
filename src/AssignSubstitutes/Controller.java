package AssignSubstitutes;

import AssignSubstitutes.Settings.SettingsController;
import AssignSubstitutes.classes.Assignment;
import AssignSubstitutes.classes.OnStaffTeacher;
import AssignSubstitutes.classes.Teacher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.*;

public class Controller {

    private ArrayList<OnStaffTeacher> osTeachers;
    private Map<LocalDate, ArrayList<Assignment>> assignments;
    private Map<LocalDate, ArrayList<Assignment>> unsavedAssignments;
    private ArrayList<Teacher> abscences;
    private ArrayList<Teacher> supplies;
    private ArrayList<LocalDate> generated;
    @FXML private TableView<Assignment> tblAssignments;
    @FXML private TableColumn<Assignment, String> colAssignAbsent, colDeleteAssignment;
    @FXML private TableColumn<Assignment, Teacher> colAssignSub;
    @FXML private TableColumn<Assignment, Integer> colAssignPeriod;
    @FXML private DatePicker datePicker;
    @FXML private Button btnGenerate, btnSave, btnRestore;

    @FXML
    public void initialize(){
        assignments =Collections.synchronizedMap(new HashMap<LocalDate, ArrayList<Assignment>>());
        unsavedAssignments = Collections.synchronizedMap(new HashMap<LocalDate, ArrayList<Assignment>>());
        generated = new ArrayList<>();

        osTeachers = ThePointlessClassIMade.getTeachers();

        btnSave.setVisible(false);
        btnRestore.setVisible(false);

        datePicker.setValue(LocalDate.now());

        buildAssignmentsTable();
    }

    @FXML
    private void changeDate(){
        LocalDate date = datePicker.getValue();

        if(date.isBefore(LocalDate.now())) {
            btnGenerate.setVisible(false);
            btnSave.setVisible(false);
            btnRestore.setVisible(false);
            tblAssignments.setEditable(false);
            ArrayList<Assignment> prevAssignments = ThePointlessClassIMade.getAssignmentByDate(date, osTeachers);
            displayAssignments(prevAssignments);
        }else{
            btnGenerate.setVisible(true);
            if(generated.contains(date)) {
                btnRestore.setVisible(true);
                btnSave.setVisible(true);
            }else{
                btnRestore.setVisible(false);
                btnSave.setVisible(false);
            }
            tblAssignments.setEditable(true);
            displayAssignments(unsavedAssignments.get(date));
        }
    }

    @FXML
    private void clickGenerateAssignments(){
        LocalDate date = datePicker.getValue();
        if(!generated.contains(date)){
            generated.add(date);
            btnSave.setVisible(true);
            btnRestore.setVisible(true);
        }
        assignments.put(date, ThePointlessClassIMade.getAssignmentsFacsimile(osTeachers));
        clickRestore();
    }

    @FXML
    private void clickRestore(){
        LocalDate date = datePicker.getValue();
        ArrayList<Assignment> currentUnsavedAssignments = new ArrayList<>();
        unsavedAssignments.put(date, currentUnsavedAssignments);
        ArrayList<Assignment> currentAssignments = assignments.get(date);
        for(Assignment a : currentAssignments) {
            currentUnsavedAssignments.add(new Assignment(a.getAbsentee(), a.getSubstitute(), a.getPeriod()));
        }
        tblAssignments.getItems().setAll(currentUnsavedAssignments);
    }

    @FXML
    private void clickSave(){
        LocalDate date = datePicker.getValue();
        ObservableList<Assignment> tblItems = tblAssignments.getItems();
        assignments.put(date, new ArrayList<>(tblItems));
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
                getItems().setAll(ThePointlessClassIMade.getAssignableTeacherList(osTeachers, getTableView().getItems(),
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

        colDeleteAssignment.setCellFactory(new Callback<>(){
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

    private void errorHandler(String msg) {
        System.out.println("Display error: " + msg);
    }
}