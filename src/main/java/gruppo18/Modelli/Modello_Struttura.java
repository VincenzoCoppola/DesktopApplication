//Impostazioni del modello struttura

package gruppo18.Modelli;

import javafx.beans.property.SimpleStringProperty;

public class Modello_Struttura {
    //SimpleStringProperty - Classe utilizzata per JavaFX
    private SimpleStringProperty Nome;
    private SimpleStringProperty Tipo;
    private SimpleStringProperty Indirizzo;
    private SimpleStringProperty Valutazione;
    private SimpleStringProperty Telefono;
    private SimpleStringProperty Id_Struttura;

    public Modello_Struttura(String nome,String tipo,String indirizzo,String valutazione, String telefono,String Id_struttura){
        Nome = new SimpleStringProperty(nome);
        Tipo = new SimpleStringProperty(tipo);
        Indirizzo = new SimpleStringProperty(indirizzo);
        Valutazione = new SimpleStringProperty(valutazione);
        Telefono = new SimpleStringProperty(telefono);
        Id_Struttura = new SimpleStringProperty(Id_struttura);
    }

    public String getId_Struttura() {
        return Id_Struttura.get();
    }

    public SimpleStringProperty id_StrutturaProperty() {
        return Id_Struttura;
    }

    public void setId_Struttura(String id_Struttura) {
        this.Id_Struttura.set(id_Struttura);
    }

    public String getNome() {
        return Nome.get();
    }

    public SimpleStringProperty nomeProperty() {
        return Nome;
    }

    public void setNome(String nome) {
        this.Nome.set(nome);
    }

    public String getTipo() {
        return Tipo.get();
    }

    public SimpleStringProperty tipoProperty() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        this.Tipo.set(tipo);
    }

    public String getIndirizzo() {
        return Indirizzo.get();
    }

    public SimpleStringProperty indirizzoProperty() {
        return Indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.Indirizzo.set(indirizzo);
    }

    public String getValutazione() {
        return Valutazione.get();
    }

    public SimpleStringProperty valutazioneProperty() {
        return Valutazione;
    }

    public void setValutazione(String valutazione) {
        this.Valutazione.set(valutazione);
    }

    public String getTelefono() {
        return Telefono.get();
    }

    public SimpleStringProperty telefonoProperty() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        this.Telefono.set(telefono);
    }
}
