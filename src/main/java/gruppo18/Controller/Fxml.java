package gruppo18.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
public class Fxml {

    //Metodi per il cariccamento di uno stage.
    public static FXMLLoader getFxml(String name) throws FileNotFoundException {
        return new FXMLLoader(new gruppo18.util.Resource(String.format("fxml/%s.fxml", name)).toURL());
    }

    public static Stage loadStage(FXMLLoader loader) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        return stage;
    }
    /*-------------------------------------------------------------*/

}
