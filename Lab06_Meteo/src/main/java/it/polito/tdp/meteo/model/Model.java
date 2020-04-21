package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private MeteoDAO meteo=new MeteoDAO();

	public Model() {

	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		Integer mediaGe = 0, mediaMi=0, mediaTo=0,numGe=0,numMi=0, numTo=0;
		List<Rilevamento> rilevamenti= meteo.getAllRilevamenti();
		for(Rilevamento r: rilevamenti) {
			if(r.getData().getMonth()+1==mese) {
				if(r.getLocalita().equals("Genova")) {
					mediaGe+=r.getUmidita();
					numGe++;
				}
				if(r.getLocalita().equals("Milano")) {
					mediaMi+=r.getUmidita();
					numMi++;
				}
				if(r.getLocalita().equals("Torino")) {
					mediaTo+=r.getUmidita();
					numTo++;
				}
			}
		}
		return "Umidita' media:\n"+"Genova: "+(mediaGe/numGe)+"\nMilano: "+(mediaMi/numMi)+"\nTorino: "+(mediaTo/numTo);
	}
	
	// of course you can change the String output with what you think works best
	public List<String> trovaSequenza(int mese) {
		List<String> soluzione= new ArrayList<String>();
		List<String> citta= new ArrayList<String>();
		citta.add("Genova");citta.add("Milano");citta.add("Torino");
		
		cerca(citta,0,0);
		
		
		return soluzione;
	}
	
	private void cerca(List<String> citta, Integer livello, Integer giorno) {
		//TODO implementare ricorsione completa
	}
}
/*
 * Cosa rappresenta il livello?
 * Il livello rappresenta la città nella quale mi trovo
 * 
 * Come è formata una soluzione parziale?
 * Una soluzione parziale è composta da una città scelta in un giorno scelto
 * 
 * Come faccio a riconoscere se una soluzione parziale è anche completa? 
 * Una soluzione parziale è anche completa se sono alla terza città considerata
 * 
 * Data una soluzione parziale come faccio a sapere se è valida o non è valida?
 * Una soluzione parziale è valida se rimango nelle città che ho visitato almeno 3 e non 
 * più di 6 giorni
 * 
 * Data una soluzione completa come faccio a sapere se è valida o meno?
 * Una soluzione completa è valida se il costo è minimo, se nelle città rimango meno 
 * di 6 e almeno 3 giorni e se ho visitato tutte le città
 * 
 * Come si genera una soluzione di livello+1 trovandomi io al livello 'livello'?
 * Una soluzione di livello+1 è data dalla scelta di una città e di un giorno, questo giorno 
 * viene scelto sulla base dell'umidità minima nei prossimi 9 (3 giorni nella città attuale+
 * 6 giorni nella prossima città) giorni a partire dal terzo giorno nel quale mi trovo in una città 
 * 
 * Qual è la struttura dati per memorizzare una soluzione?
 * Voglio memorizzare città-giorno-umidità perciò li scrivo in una stringa (per una soluzione parziale)
 * che aggiungerò dentro una lista che conterrà le tre stringhe relative alle tre città
 * 
 * Qual è la struttura dati per memorizzare lo stato della ricerca?
 * Linked list per le temperature
 * */
 