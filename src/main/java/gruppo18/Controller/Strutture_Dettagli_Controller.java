package gruppo18.Controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static gruppo18.Controller.Fxml.getFxml;
import static gruppo18.Controller.Fxml.loadStage;

public class Strutture_Dettagli_Controller implements Initializable {

    /*-----Dichiarazioni-----*/
    private static Stage modifica = null;
    private boolean rec;
    private int change =0;
    private static  String ID_strutture = Strutture_Controller.RecuperaID();        //Recupero id della struttura.
    private String id;
    private Firestore db = FirestoreClient.getFirestore();      //Metodo firestore.

    //Richiamo da SceneBuilder.
    @FXML
    private PieChart PieTorta;

    @FXML
    private Label Nome;

    @FXML
    private Label Id;

    @FXML
    private Label nVisit;

    @FXML
    private Label n5;

    @FXML
    private Label n4;

    @FXML
    private Label n3;

    @FXML
    private Label n2;

    @FXML
    private Label n1;

    @FXML
    private Label Citta;

    @FXML
    private Label Descrizione;

    @FXML
    private Label Indirizzo;

    @FXML
    private Label LateLong;

    @FXML
    private Label Telefono;

    @FXML
    private Label Tipo;

    @FXML
    private Label Valutazione;

    @FXML
    private Label txtRecSi;

    @FXML
    private Label txtRecNo;

    /*-----Metodi-----*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ID_strutture = Strutture_Controller.RecuperaID();       //Recupero id struttura.
        inizializzaDettagli();      //Richiamo metodo.
        rec=harecensioni();     //Controllo sulle recensioni della struttura.
        if (rec == true) {      //Se la struttura possiede almeno una recensione allora verrà mostrato il grafico.
           inizializzaTorta();
           txtRecSi.setVisible(true);
           txtRecNo.setVisible(false);
        }
        else{
           txtRecNo.setVisible(true);
           txtRecSi.setVisible(false);
        }
    }

    //Metodo che indica se una struttura ha recensioni.
    private boolean harecensioni() {
        ApiFuture<QuerySnapshot> query = db.collection("Recensione").whereEqualTo("Id_Struttura",ID_strutture).get();
        int count = 0;
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            count++;
        }
        if (count > 0){
            return true;
        }
        else{
            return false;
        }
    }


    //Settaggio del grafico a torta.
    private void inizializzaTorta() {
        int uno,due,tre,quattro,cinque;
        uno = due = tre = quattro = cinque = 0;
        ApiFuture<QuerySnapshot> query = db.collection("Recensione").whereEqualTo("Id_Struttura",ID_strutture).get();

        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            String voto = document.getString("Voto");
            Double votod = Double.parseDouble(voto);
            if (0.5 <= votod && votod <= 1 ){
                uno ++;
            }
            else if( 1.1 <= votod && votod <= 2){
                due ++;
            }
            else if( 2.1 <= votod && votod <= 3){
                tre ++;
            }
            else if( 3.1 <= votod && votod <= 4){
                quattro ++;
            }
            else if( 4.1 <= votod && votod <= 5){
                cinque ++;
            }
        }

        //Settaggio del grafico.
        ObservableList<PieChart.Data> Data = FXCollections.observableArrayList(
                new PieChart.Data("1 Stella",uno),
                new PieChart.Data("2 Stelle",due),
                new PieChart.Data("3 Stelle",tre),
                new PieChart.Data("4 Stelle",quattro),
                new PieChart.Data("5 Stelle",cinque));
        PieTorta.setData(Data);
        //Calcolo della percentuale.
       int totale = uno + due + tre + quattro + cinque;
       double UNO,DUE,TRE,QUATTRO,CINQUE,TOTALE;
       TOTALE = totale; UNO = uno; DUE = due; TRE = tre; QUATTRO = quattro; CINQUE = cinque;
        double unoPerc=(UNO/TOTALE) * 100;
        double duePerc=(DUE/TOTALE) * 100;
        double trePerc=(TRE /TOTALE) * 100;
        double quattroPerc=(QUATTRO/TOTALE) * 100;
        double cinquePerc=(CINQUE/TOTALE) * 100;
        double unoPerc2= (Math.floor(unoPerc * 100)) /100;
        double duePerc2= (Math.floor(duePerc * 100)) /100;
        double trePerc2= (Math.floor(trePerc * 100)) /100;
        double quattroPerc2= (Math.floor(quattroPerc * 100)) /100;
        double cinquePerc2= (Math.floor(cinquePerc * 100)) /100;
        //Settaggio dei vari label.
        nVisit.setText(String.valueOf(totale));
        n5.setText((String.valueOf(cinque)) +" / ( " +(String.valueOf(cinquePerc2)) + "% )");
        n4.setText((String.valueOf(quattro)) +" / ( " +(String.valueOf(quattroPerc2)) + "% )");
        n3.setText((String.valueOf(tre)) +" / ( " +(String.valueOf(trePerc2)) + "% )");
        n2.setText((String.valueOf(due)) +" / ( " +(String.valueOf(duePerc2)) + "% )");
        n1.setText((String.valueOf(uno)) +" / ( " +(String.valueOf(unoPerc2)) + "% )");
    }

    //Metodo che recupera tutti gli attributi di una struttura.
    public void inizializzaDettagli(){
        //Query
        ApiFuture<QuerySnapshot> query = db.collection("Strutture")
                .whereEqualTo("Id_Struttura",ID_strutture).get();

        QuerySnapshot querySnapshot = null;
        try {       //Gestione delle eccezioni.
            querySnapshot = query.get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents){       //Scorre tutto il documento.
            //Recupero vari attributi della struttura selezionata.
            Double voto = document.getDouble("Valutazione");
            double voto2= (Math.floor(voto * 100)) /100;
            double latitudine = document.getDouble("Latitudine");
            double longitudine = document.getDouble("Longitudine");
            String NOME = document.getString("Nome");
            String VOTO = Double.toString(voto2);
            String LATITUDINE = Double.toString(latitudine);
            String LONGITUDINE = Double.toString(longitudine);
            String TIPO;
            String TipoQuery = document.getString("Tipo");
            //Controllo sul tipo.
            if (TipoQuery.equals("Ris")){
                TIPO = "Ristorante";
            }
            else if(TipoQuery.equals("Hot")){
                TIPO = "Hotel";
            }
            else{
                TIPO = "Attrazione";
            }

            String INDIRIZZO = document.getString("Indirizzo");
            String TELEFONO = document.getString("Telefono");
            String DESCRIZIONE = document.getString("Descrizione");
            String CITTA = document.getString("Città");

            //Settaggio dei vari campi con i valori ottenuti.
            Citta.setText(CITTA);
            Descrizione.setText(DESCRIZIONE);
            Id.setText(ID_strutture);
            Indirizzo.setText(INDIRIZZO);
            LateLong.setText(LATITUDINE+" ; " + LONGITUDINE);
            Nome.setText(NOME);
            Telefono.setText(TELEFONO);
            Tipo.setText(TIPO);
            Valutazione.setText(VOTO);
        }

    }

    //Metodo per recuperare l'id di una struttura.
    public static String RecuperaID2(){
        return ID_strutture;
    }

    //Metodo per tornare alla schermata principale.
    public void btnTornaPressed(ActionEvent actionEvent) {
        Login_Controller.mostra_Strutture();
        Strutture_Controller.chiudi_StrutturaDettagli();
    }

    //Metodi per la gestione dello stage ModificaStruttura.
    public void btnModificaPressed(ActionEvent actionEvent) {
        try{
            FXMLLoader root = getFxml("FXML_ModificaStruttura");
            modifica=loadStage(root);
            modifica.setTitle("CV19-Modifica struttura.");
            modifica.show();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void chiudiModifica(){
        if (modifica != null){
            modifica.close();
        }
        else{
            modifica = null;
        }
    }
    /*-------------------------------------*/


    //Tasto aggiorna.
    public void btnReloadPressed(MouseEvent mouseEvent) throws InterruptedException {
        Strutture_Controller.chiudi_StrutturaDettagli();
        Thread.sleep(50);
        Strutture_Controller.mostra_StrutturaDettagli();
    }
}
