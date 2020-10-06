package gruppo18.ApplicationClass;

import gruppo18.Controller.Login_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login {

    /*-----Dichiarazioni-----*/
    Login_Controller Controller = new Login_Controller();
    @FXML
    private PasswordField Password;
    @FXML
    private TextField Email;

    /*-----Metodi-----*/

    //Metodo per il recupero dei dati necessari per
    //effettuare il login.
    public void btnPressed(ActionEvent actionEvent) {
        String password = Password.getText().toLowerCase();
        String email = Email.getText().toLowerCase();
        Controller.EffettuaLogin(password,email);
    }
}
