package gruppo18.Controller;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import javax.swing.*;


public class ModifcaStruttura_Controller {

    /*-----Dichiarazioni-----*/
    //Spinner per la modifica della tipologia.
    ObservableList<String> Tipologia = FXCollections.observableArrayList("Ristorante", "Hotel", "Attrazione", "Seleziona un tipo");
    public static String ID;
    int risposta=10;

    //Scene Builder.
    @FXML
    private TextField EditNome;

    @FXML
    private TextField EditCitta;

    @FXML
    private TextField EditDescr;

    @FXML
    private TextField EditTel;

    @FXML
    private TextField EditLat;

    @FXML
    private TextField EditLong;

    @FXML
    private ChoiceBox EditTipo;

    @FXML
    private TextField EditInd;


    /*-----Metodi-----*/

    @FXML
    private void initialize() {     //Settaggio dello spinner.
        EditTipo.setValue("Seleziona un tipo");
        EditTipo.setItems(Tipologia);
    }


    //Tasto applica modifiche.
    public void btnApplicaPressed(ActionEvent actionEvent) throws InterruptedException {
        ID = Strutture_Dettagli_Controller.RecuperaID2();       //Recupero id.
        String nome = EditNome.getText();
        String Citta = EditCitta.getText();
        String Descrizione = EditDescr.getText();
        String Tipo = (String) EditTipo.getValue();
        String Telefono = EditTel.getText();
        String Lat = EditLat.getText();
        String Long = EditLong.getText();
        String Indirizzo = EditInd.getText();

        Firestore database = FirestoreClient.getFirestore();        //Metodo firestore.
        //Controllo dei vari campi.
        if (nome.isEmpty() && Citta.isEmpty() && Descrizione.isEmpty() && Telefono.isEmpty() && Long.isEmpty() && Lat.isEmpty() && Tipo.equals("Seleziona un tipo")
                && Indirizzo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Non hai riempito nessuno dei campi di modifica!");
        } else {
            //In caso di successo messaggio di conferma.
            risposta = JOptionPane.showConfirmDialog(null, "Confermare i cambiamenti? ","Selezione",JOptionPane.YES_NO_OPTION);
            if (risposta != JOptionPane.YES_OPTION) {
                    //Non fare nulla
            } else {
                if (!nome.isEmpty()) {
                    database.collection("Strutture").document(ID).update("Nome", nome);
                }

                if ((!Citta.isEmpty() && !Lat.isEmpty() && !Long.isEmpty() && !Indirizzo.isEmpty())
                        || (Citta.isEmpty() && Lat.isEmpty() && Long.isEmpty() && Indirizzo.isEmpty())) {
                    if (!Citta.isEmpty() && !Lat.isEmpty() && !Long.isEmpty() && !Indirizzo.isEmpty()) {
                        //Modifica nel database
                        database.collection("Strutture").document(ID).update("Città", Citta);
                        double latitudine = Double.parseDouble(Lat);
                        database.collection("Strutture").document(ID).update("Latitudine", latitudine);
                        double longitudine = Double.parseDouble(Long);
                        database.collection("Strutture").document(ID).update("Longitudine", longitudine);
                        database.collection("Strutture").document(ID).update("Indirizzo", Indirizzo);
                    }
                } else {
                    //Se si modifica la città, bisogna modificare anche l'indirizzo (compreso latitudine e longitudine).
                    JOptionPane.showMessageDialog(null, "I campi : Città , Indirizzo , Latitudine e lungitudine vanno modificati insieme!");
                    risposta =10;
                    EditLong.setText("");
                    EditLat.setText("");
                    EditCitta.setText("");
                    EditInd.setText("");
                }
                if (!Descrizione.isEmpty()) {
                    database.collection("Strutture").document(ID).update("Descrizione", Descrizione);
                }
                if (!Telefono.isEmpty()) {
                    database.collection("Strutture").document(ID).update("Telefono", Telefono);
                }
                if (!Tipo.isEmpty()) {      //Controllo sul tipo.
                    String TIPO;
                    if (Tipo.equals("Attrazione")) {
                        TIPO = "Att";
                    } else if (Tipo.equals("Ristorante")) {
                        TIPO = "Ris";
                    } else {
                        TIPO = "Hot";
                    }
                    database.collection("Strutture").document(ID).update("Tipo", TIPO);
                }
            }
        }

        //Vengono chiuse varie finestre.
        if (risposta == JOptionPane.YES_OPTION) {
            Strutture_Controller.chiudi_StrutturaDettagli();
            Thread.sleep(50);
            Strutture_Controller.mostra_StrutturaDettagli();
            Thread.sleep(500);
            Strutture_Dettagli_Controller.chiudiModifica();
        }
    }
}
