package AssignSubstitutes.Settings;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SettingsController {
    Stage stage;

    public void setStage(Stage stage){
        this.stage=stage;
    }

    public void initialize(){
    }

    private String getFilePath(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xslx)", "*.xslx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file);
        //TODO: File validation
        return file.toString();
    }

}
