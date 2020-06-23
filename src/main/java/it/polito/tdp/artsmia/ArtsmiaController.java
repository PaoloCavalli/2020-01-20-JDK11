package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	List<Adiacenza> adiacenze = this.model.getConnessi();
    	for(Adiacenza a : adiacenze) {
    		txtResult.appendText(a.getA1().toString()+"-"+ a.getA2().toString()+"-"+a.getPeso()+"\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	Integer id ;
    	try {
    		id = Integer.parseInt(this.txtArtista.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("Inserisci un numero");
    	     return;
    	}
    	Artist artista = this.model.getArtista(id);
    	if(artista == null) {
    		txtResult.appendText("Artista non presente nel Grafo!");
    		return;
    	}
    	List<Artist> percorso = this.model.trovaPercorso(id);
    	txtResult.appendText("TROVATO PERCORSO DI LUNGHEZZA: "+percorso.size()+"\n");
    	for(Artist a: percorso) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	  String role = this.boxRuolo.getValue();
    	     if(role== null ) {
    	    	 txtResult.appendText("Seleziona un ruolo");
    	    	 return;
    	     }
    	this.model.creaGrafo(role);
    	
    	txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi", this.model.nVertici(), this.model.nArchi()));
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(this.model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
