package gruppo18.Controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import gruppo18.Modelli.Modello_Struttura;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static gruppo18.Controller.Fxml.getFxml;
import static gruppo18.Controller.Fxml.loadStage;

public class Strutture_Controller implements Initializable {

    /*-----Dichiarazioni-----*/
    private static Stage StrutturaDettagli = null;
    private static String ID = null;
    private Firestore db = FirestoreClient.getFirestore();  //Metodo CloudFirestore.
    private Modello_Struttura struttura_selezionata;
    private int riga = -1;
    boolean reset;
    private List<String> lista;

    //Richiami da SceneBuilder.
    @FXML
    private PieChart PieTorta2;

    @FXML
    private TextField etRic;

    @FXML
    private TextField etRicNome;

    @FXML
    private PieChart PieTorta3;

    @FXML
    private  Button btnDettagli;

    @FXML
    private Label nVisitatori;

    @FXML
    private Label n5Stelle;

    @FXML
    private Label n4Stelle;

    @FXML
    private Label n3Stelle;

    @FXML
    private Label n2Stelle;

    @FXML
    private Label n1Stella;

    @FXML
    private Label nRistoranti;

    @FXML
    private Label nHotel;

    @FXML
    private Label nAttrazioni;

    @FXML
    private TableView<Modello_Struttura> Struct_Table;

    @FXML
    private TableColumn<?, ?> Struct_Name;

    @FXML
    private TableColumn<?, ?> Struct_Type;

    @FXML
    private TableColumn<?, ?> Struct_Address;

    @FXML
    private TableColumn<?, ?> Struct_Val;

    @FXML
    private TableColumn<?, ?> Struct_Tel;


    /*-----Metodi-----*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inizializzaStrutture();      //Richiamo metodo.
        inizializzaTortaTotale();    //Richiamo metodo.
        try {       //Gestione delle eccezioni.
            inizializzaTortaPerTipologia();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Metodo che inizializza il grafico a torta per tipologia.
    private void inizializzaTortaPerTipologia() throws ExecutionException, InterruptedException {
        int Ristoranti, Hotel, Attrazioni;
        Ristoranti = Hotel = Attrazioni = 0;
        ApiFuture<QuerySnapshot> query = db.collection("Recensione").get();

        QuerySnapshot querySnapshot = null;
        try { //Gestione delle eccezioni.
            querySnapshot = query.get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }


        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            String ID = document.getString("Id_Struttura");
            ApiFuture<QuerySnapshot> query2 = db.collection("Strutture").whereEqualTo("Id_Struttura", ID).get(); //Query per il recupero delle recensioni.
            QuerySnapshot querySnapshot2 = query2.get();
            List<QueryDocumentSnapshot> documentSnapshot = querySnapshot2.getDocuments();
            for (QueryDocumentSnapshot document2 : documentSnapshot) {
                String tipo = document2.getString("Tipo"); // Recupero del tipo della struttura per il conteggio.
                if (tipo.equals("Hot")) {
                    Hotel++;
                } else if (tipo.equals("Ris")) {
                    Ristoranti++;
                } else {
                    Attrazioni++;
                }
            }
        }
        //Settaggio del grafico.
        ObservableList<PieChart.Data> Data = FXCollections.observableArrayList(
                new PieChart.Data("Ristoranti", Ristoranti),
                new PieChart.Data("Hotel", Hotel),
                new PieChart.Data("Attrazioni", Attrazioni));
        PieTorta3.setData(Data);
        //Calcolo delle percentuali.
        int tot = Attrazioni + Hotel + Ristoranti;
        double TOT, ATTRAZIONI, HOTEL, RISTORANTI;
        TOT = tot;
        ATTRAZIONI = Attrazioni;
        HOTEL = Hotel;
        RISTORANTI = Ristoranti;
        double attPerc = (ATTRAZIONI / TOT) * 100;
        double attPerc2 = (Math.floor(attPerc * 100)) / 100;
        double hotPerc = (HOTEL / TOT) * 100;
        double hotPerc2 = (Math.floor(hotPerc * 100)) / 100;
        double risPerc = (RISTORANTI / TOT) * 100;
        double risPerc2 = (Math.floor(risPerc * 100)) / 100;
        //Settaggio dei vari label che mostreranno le percentuali.
        nAttrazioni.setText((String.valueOf(Attrazioni)) + " / (" + String.valueOf(attPerc2) + "%)");
        nHotel.setText((String.valueOf(Hotel)) + " / (" + String.valueOf(hotPerc2) + "%)");
        nRistoranti.setText((String.valueOf(Ristoranti)) + " / (" + String.valueOf(risPerc2) + "%)");
    }

    //Metodo per la gestione del grafico a torta.
    private void inizializzaTortaTotale() {
        double uno, due, tre, quattro, cinque;
        uno = due = tre = quattro = cinque = 0;
        ApiFuture<QuerySnapshot> query = db.collection("Recensione").get();     //Query per il recupero delle recensioni.

        QuerySnapshot querySnapshot = null;
        try {       //Gestione delle eccezioni.
            querySnapshot = query.get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            String voto = document.getString("Voto");   //Recupero del voto associato ad ogni recensione.
            Double votod = Double.parseDouble(voto);
            if (0.5 <= votod && votod <= 1) {       //Se il voto è compreso tra 0.5 e 1 allora verrà contato come 1.
                uno++;
            } else if (1.1 <= votod && votod <= 2) {   //...
                due++;
            } else if (2.1 <= votod && votod <= 3) {   //...
                tre++;
            } else if (3.1 <= votod && votod <= 4) {   //...
                quattro++;
            } else if (4.1 <= votod && votod <= 5) {   //...
                cinque++;
            }
        }

        //Settaggio del grafico.
        ObservableList<PieChart.Data> Data = FXCollections.observableArrayList(
                new PieChart.Data("1 Stella", uno),
                new PieChart.Data("2 Stelle", due),
                new PieChart.Data("3 Stelle", tre),
                new PieChart.Data("4 Stelle", quattro),
                new PieChart.Data("5 Stelle", cinque));
        PieTorta2.setData(Data);
        //Calcolo della percentuale.
        int UNO, DUE, TRE, QUATTRO, CINQUE, TOTALE;
        UNO = (int) uno;
        DUE = (int) due;
        TRE = (int) tre;
        QUATTRO = (int) quattro;
        CINQUE = (int) cinque;
        TOTALE = UNO + DUE + TRE + QUATTRO + CINQUE;   //Numero totale di recensioni.
        double totale = uno + due + tre + quattro + cinque;     //Serve per il calcolo delle percentuali.
        double unoPerc = (uno / totale) * 100;
        double unoPerc2 = (Math.floor(unoPerc * 100)) / 100;
        double duePerc = (due / totale) * 100;
        double duePerc2 = (Math.floor(duePerc * 100)) / 100;
        double trePerc = (tre / totale) * 100;
        double trePerc2 = (Math.floor(trePerc * 100)) / 100;
        double quattroPerc = (quattro / totale) * 100;
        double quattroPerc2 = (Math.floor(quattroPerc * 100)) / 100;
        double cinquePerc = (cinque / totale) * 100;
        double cinquePerc2 = (Math.floor(cinquePerc * 100)) / 100;
        //Settaggio dei vari label che mostreranno le percentuali.
        nVisitatori.setText(String.valueOf(TOTALE));
        n5Stelle.setText((String.valueOf(CINQUE)) + " / (" + String.valueOf(cinquePerc2) + "%)");
        n4Stelle.setText((String.valueOf(QUATTRO)) + " / (" + String.valueOf(quattroPerc2) + "%)");
        n3Stelle.setText((String.valueOf(TRE)) + " / (" + String.valueOf(trePerc2) + "%)");
        n2Stelle.setText((String.valueOf(DUE)) + " / (" + String.valueOf(duePerc2) + "%)");
        n1Stella.setText((String.valueOf(UNO)) + " / (" + String.valueOf(unoPerc2) + "%)");
    }

    //Metodo che inizializza la tabella che conterrà tutte le strutture.
    public void inizializzaStrutture() {
        btnDettagli.setDisable(true);   //Rende cliccabile il tasto MOSTRA DETTAGLI.
        ApiFuture<QuerySnapshot> query = db.collection("Strutture").get();  //Query.

        QuerySnapshot querySnapshot = null;
        try {   //Gestione delle eccezioni.
            querySnapshot = query.get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();       //Metodo della classe Throwable che serve a localizzare l'eccezione generata.
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        //Setta la tabella che mostrerà le strutture presenti nel database.
        Struct_Name.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        Struct_Type.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
        Struct_Address.setCellValueFactory(new PropertyValueFactory<>("Indirizzo"));
        Struct_Val.setCellValueFactory(new PropertyValueFactory<>("Valutazione"));
        Struct_Tel.setCellValueFactory(new PropertyValueFactory<>("Telefono"));

        ObservableList<Modello_Struttura> observableList = FXCollections.observableArrayList();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        lista = new LinkedList<>();
        for (QueryDocumentSnapshot document : documents) {
            lista.add(document.getString("Id_Struttura"));      //Aggiunge ad una linked list gli id di tutte le strutture.
            double voto = document.getDouble("Valutazione");
            double voto2 = (Math.floor(voto * 100)) / 100;
            String VOTO = Double.toString(voto2);
            String TIPO;
            String TipoQuery = document.getString("Tipo");
            if (TipoQuery.equals("Ris")) {
                TIPO = "Ristorante";
            } else if (TipoQuery.equals("Hot")) {
                TIPO = "Hotel";
            } else {
                TIPO = "Attrazione";
            }
            observableList.add(new Modello_Struttura(document.getString("Nome"), TIPO, document.getString("Indirizzo"), VOTO, document.getString("Telefono"), document.getString("Id_Struttura")));
        }

        Struct_Table.setItems(observableList);
        Struct_Table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                struttura_selezionata = Struct_Table.getSelectionModel().getSelectedItem();
                riga = Struct_Table.getSelectionModel().getSelectedIndex();
                ID = struttura_selezionata.getId_Struttura();
                btnDettagli.setDisable(false);      //Quando si clicca su una struttura, si attiva il tasto.
            }
        });
        setTasto();     //Se invece, non si clicca su nessuna struttura allora il tasto verrà disattivato.
    }

    //Gestione del tasto; se non si seleziona nessuna struttura il tasto non è cliccabile.
    private void setTasto() {
        if (ID == null) {
            btnDettagli.setDisable(true);
        }
    }

    public void btnDettagliPressed(ActionEvent actionEvent) {
        if (ID == null) {
            JOptionPane.showMessageDialog(null, "Nessuna struttura selezionata.");
        } else {
            mostra_StrutturaDettagli();
        }

    }
    /*-----------------------------------------*/

    //Metodo che recupera l'id della struttura che servirò per altre classi.
    public static String RecuperaID() {
        return ID;
    }


    //Metodi per la gestione dello stage StrutturaDettagli.
    public static void mostra_StrutturaDettagli() {
        try {        //Gestione delle eccezioni.
            FXMLLoader root = getFxml("FXML_StrutturaDettagli");
            StrutturaDettagli = loadStage(root);
            StrutturaDettagli.setTitle("CV19-Dettagli struttura.");

            StrutturaDettagli.setMaximized(true);
            StrutturaDettagli.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Login_Controller.chiudi_Strutture();
    }

    public static void chiudi_StrutturaDettagli() {
        if (StrutturaDettagli != null) {
            StrutturaDettagli.close();
        } else {
            StrutturaDettagli = null;
        }
    }
    /*----------------------------------------------------*/

    //Pulsante per uscire dall'applicativo desktop.
    public void btnEsciPressed(ActionEvent actionEvent) {
        Login_Controller.chiudi_Strutture();
    }


    //Metodo per effettuare la ricerca o veloce o per nome.
    public void btnSearchPressed(MouseEvent mouseEvent) {
        String RICERCA = etRic.getText();
        String prefisso = "";
        String ricerca = RICERCA.toLowerCase();
        String RICERCANOME = etRicNome.getText();
        String ricercanome = RICERCANOME.toLowerCase();
        if ((ricerca.isEmpty() && ricercanome.isEmpty()) || ((! ricerca.isEmpty() && ! ricercanome.isEmpty()))) {
            if ((! ricerca.isEmpty() && ! ricercanome.isEmpty())){
                JOptionPane.showMessageDialog(null, "Solo un campo deve essere compilato");
            }
            else {
                JOptionPane.showMessageDialog(null, "Campi ricerca vuoti!");
            }
        } else {
            reset = true;
            btnDettagli.setDisable(true);   //Rende cliccabile il tasto MOSTRA DETTAGLI.
            ApiFuture<QuerySnapshot> query = db.collection("Strutture").get();  //Query.

            QuerySnapshot querySnapshot = null;
            try {   //Gestione delle eccezioni.
                querySnapshot = query.get();
            } catch (InterruptedException ex) {
                ex.printStackTrace();       //Metodo della classe Throwable che serve a localizzare l'eccezione generata.
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }

            //Setta la tabella che mostrerà le strutture presenti nel database.
            Struct_Name.setCellValueFactory(new PropertyValueFactory<>("Nome"));
            Struct_Type.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
            Struct_Address.setCellValueFactory(new PropertyValueFactory<>("Indirizzo"));
            Struct_Val.setCellValueFactory(new PropertyValueFactory<>("Valutazione"));
            Struct_Tel.setCellValueFactory(new PropertyValueFactory<>("Telefono"));

            ObservableList<Modello_Struttura> observableList = FXCollections.observableArrayList();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            lista = new LinkedList<>();
            for (QueryDocumentSnapshot document : documents) {
                double voto = document.getDouble("Valutazione");
                double voto2 = (Math.floor(voto * 100)) / 100;
                String VOTO = Double.toString(voto2);
                String TIPO;
                String TipoQuery = document.getString("Tipo");
                if (TipoQuery.equals("Ris")) {
                    TIPO = "Ristorante";
                } else if (TipoQuery.equals("Hot")) {
                    TIPO = "Hotel";
                } else {
                    TIPO = "Attrazione";
                }
                String nome = document.getString("Nome");
                String NOME = nome.toLowerCase();
                String indirizzo = document.getString("Indirizzo");
                String telefono = document.getString("Telefono");
                String Riga_Totale = nome + " " + TIPO + " " + VOTO + " " + indirizzo  + " " + telefono + " " ;
                String riga_totale = Riga_Totale.toLowerCase();
                if (ricercanome.length() >=4 ) {
                     prefisso = ricercanome.substring(0, 4);
                }

                if (! ricerca.isEmpty()){
                    if (riga_totale.contains(ricerca)){
                        lista.add(document.getString("Id_Struttura"));      //Aggiunge ad una linked list gli id di tutte le strutture.
                        observableList.add(new Modello_Struttura(document.getString("Nome"), TIPO, document.getString("Indirizzo"), VOTO, document.getString("Telefono"), document.getString("Id_Struttura")));
                    }
                }
                else if(! ricercanome.isEmpty()){
                    if ((NOME.contains(ricercanome)||(NOME.contains(prefisso)))){
                        lista.add(document.getString("Id_Struttura"));      //Aggiunge ad una linked list gli id di tutte le strutture.
                        observableList.add(new Modello_Struttura(document.getString("Nome"), TIPO, document.getString("Indirizzo"), VOTO, document.getString("Telefono"), document.getString("Id_Struttura")));
                    }
                }

            }

            Struct_Table.setItems(observableList);
            Struct_Table.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    struttura_selezionata = Struct_Table.getSelectionModel().getSelectedItem();
                    riga = Struct_Table.getSelectionModel().getSelectedIndex();
                    ID = struttura_selezionata.getId_Struttura();
                    btnDettagli.setDisable(false);      //Quando si clicca su una struttura, si attiva il tasto.
                }
            });
            etRic.setText("");
            etRicNome.setText("");
            setTasto();//Se invece, non si clicca su nessuna struttura allora il tasto verrà disattivato.
        }
    }


    //Pulsante che aggiornerà la tabella.
    public void btnRefreshPressed(MouseEvent mouseEvent) {
        if (reset == true) {
            inizializzaStrutture();
            reset = false;
            etRic.setText("");
            etRicNome.setText("");
        } else {
            JOptionPane.showMessageDialog(null,"Tabella gia resettata");
        }

    }
}
