/**/
package gruppo18.Controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import gruppo18.ApplicationClass.DesktopApplication;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static gruppo18.Controller.Fxml.getFxml;
import static gruppo18.Controller.Fxml.loadStage;

public class Login_Controller {

    /*-----Dichiarazioni-----*/
    private static Stage Strutture ;
    private boolean login = false;

    /*-----Metodi-----*/

    //Tasto che esegue il login.
    public void EffettuaLogin(String password,String email) {


        Firestore db = FirestoreClient.getFirestore();
        //Controllo dei dati inseriti.
        if (password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Uno o più campi vuoti!");
        } else if (!password.equals("admin")) { //La password standard scelta per l'autenticazione è admin.
            JOptionPane.showMessageDialog(null, "Password non valida!");
        } else {
            //Se tutti i controlli vengono superati, allora viene effettuata la query per il controllo utente.
            ApiFuture<QuerySnapshot> query = db.collection("Utenti")
                    .whereEqualTo("Email", email)
                    .get();

            QuerySnapshot querySnapshot = null;
            try {   //Gestione delle eccezioni.
                querySnapshot = query.get();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }

            assert querySnapshot != null;
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {  //Se l'utente autenticato ha id uguale ad uno degli admin allora potrà accedere.
                if (document.getString("Id_Utente").equals("OQ6nax7jzPUBHfY7hZ42d63uLt43") ||
                        document.getString("Id_Utente").equals("qv5tplofyhWtPek2i46ej9Gohrh2")) {
                    JOptionPane.showMessageDialog(null, "Accesso effettuato!");
                    login = true;
                    mostra_Strutture();     //Richiamo metodo.
                }
            }
            if (!getLogin()) {       //In caso di fallimento.
                JOptionPane.showMessageDialog(null, "Non hai i permessi per accedere!");
            }
        }
    }

    public boolean getLogin(){return login;}

    //Metodi per la gestione dello stage Strutture.
    public static void mostra_Strutture() {
        try{
            FXMLLoader root = getFxml("FXML_Strutture");
            Strutture=loadStage(root);
            Strutture.setTitle("CV19-Visualizza strutture.");
            Strutture.setMaximized(true);
            Strutture.show();

        }catch(IOException e){
            System.out.println("Errore Strutture");
            e.printStackTrace();
        }

        DesktopApplication.chiudiLogin();       //Chiude la finestra di login.
    }

    public static void chiudi_Strutture() {
        if (Strutture != null) {
            Strutture.close();
        } else {
            Strutture = null;
        }
    }
    /*------------------------------------------------*/

}
