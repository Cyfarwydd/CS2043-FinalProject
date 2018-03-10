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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;

public class Controller {

    ArrayList<OnStaffTeacher> osTeachers;
    ArrayList<Assignment> assignments;
    ArrayList<Teacher> supplies;
    @FXML private TableView<Assignment> tblAssignments;
    @FXML private TableColumn<Assignment, String> colAssignAbsent;
    @FXML private TableColumn<Assignment, Teacher> colAssignSub;
    @FXML private TableColumn<Assignment, Integer> colAssignPeriod;


    @FXML
    public void initialize(){
        osTeachers = ThePointlessClassIMade.getTeachers();
    }

    @FXML
    private void clickGenerateAssignments(){
        assignments = ThePointlessClassIMade.getAssignmentsFacsimile(osTeachers);
        colAssignAbsent.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Assignment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Assignment, String> assignment) {
                return new SimpleObjectProperty<>(assignment.getValue().getAbsentee().getName());
            }
        });

        colAssignSub.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Assignment, Teacher>,
                ObservableValue<Teacher>>(){
            @Override
            public ObservableValue<Teacher> call(TableColumn.CellDataFeatures<Assignment, Teacher> param) {
                return new SimpleObjectProperty<>(param.getValue().getSubstitute());
            }
        });

        ObservableList<Teacher> subList = FXCollections.observableArrayList(osTeachers);
        colAssignSub.setCellFactory(t -> new ComboBoxTableCell<Assignment, Teacher>(subList){
            @Override public void startEdit() {
                Assignment currentRow = getTableRow().getItem();
                getItems().setAll(ThePointlessClassIMade.getAvailableTeachers(osTeachers, getTableView().getItems(),
                        currentRow.getPeriod()
                        .getPeriodNumber(), currentRow));
                super.startEdit();
            }
        });

        colAssignSub.setOnEditCommit((TableColumn.CellEditEvent<Assignment,Teacher> event) -> {
            TablePosition<Assignment, Teacher> pos = event.getTablePosition();
            System.out.println("onEditCommit");
            Teacher newTeacher = event.getNewValue();

            int row = pos.getRow();
            Assignment assignment = event.getTableView().getItems().get(row);

            assignment.setSubstitute(newTeacher);
        });





        colAssignPeriod.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Assignment, Integer>,
            ObservableValue<Integer>>(){
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Assignment, Integer> assignment) {
                return new SimpleObjectProperty<>(assignment.getValue().getPeriod().getPeriodNumber());
            }
        });
        ObservableList<Assignment> assignmentsObservable= FXCollections.observableArrayList();
        for(Assignment a : assignments) assignmentsObservable.add(new Assignment(a.getAbsentee(), a.getSubstitute(), a.getPeriod()));
        tblAssignments.getItems().setAll(assignmentsObservable);
    }

    @FXML
    private void clickSave(){
        ObservableList<Assignment> tblItems = tblAssignments.getItems();
        for(Assignment a : assignments){
            System.out.println(a.getAbsentee()+"\t"+a.getSubstitute()+"\t"+a.getPeriod().getPeriodNumber());
        }
        assignments = new ArrayList<>(tblItems);
        for(Assignment a : assignments){
            System.out.println(a.getAbsentee()+"\t"+a.getSubstitute()+"\t"+a.getPeriod().getPeriodNumber());
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
        }
    }
    private void errorHandler(String msg){
        System.out.println("Display error: "+msg);
    }

}