package gruppo18.ApplicationClass;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static gruppo18.Controller.Fxml.getFxml;
import static gruppo18.Controller.Fxml.loadStage;

public class DesktopApplication extends Application {

    /*-----Dichiarazioni-----*/
    private static Stage Login=null;

    /*-----Metodi-----*/

    //Metodo per connettere l'applicativo desktop al database.
    @Override
    public void start(Stage stage) {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("ServiceKey.json");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,"File di configurazione non trovato");
        }
        FirebaseOptions options=null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://cv19-c5fe2.firebaseio.com")
                    .build();
        } catch (IOException ex) {
        }
        FirebaseApp.initializeApp(options);


        mostra_Login();
    }

    //Metodi per la gestione dello stage Login.
    public static void mostra_Login() {
        try{
            FXMLLoader root = getFxml("FXML_Login");
            Login=loadStage(root);
            Login.setTitle("CV19-Login");
            Login.setMaximized(true);
            Login.show();

        }catch(IOException e){
            System.out.println("Errore login");
            e.printStackTrace();
        }
    }

    public static void chiudiLogin() {
        if (Login != null) {
            Login.close();
        } else {
            Login = null;
        }
    }
 /*------------------------------------------------*/

    //Main.
    public static void main(String[] args) {
        launch(args);
    }

}
