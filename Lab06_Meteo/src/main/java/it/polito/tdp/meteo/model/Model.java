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
	Integer bestCosto=100000000,costo=0;
	List<String> soluzione= new ArrayList<String>();
	

	public Model() {

	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		Integer mediaGe = 0, mediaMi=0, mediaTo=0,numGe=0,numMi=0, numTo=0;//media rappresenta la somma delle umidità e num rappresenta il numero di umidità che sommo
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
	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese,String localita){
		return meteo.getAllRilevamentiLocalitaMese(mese, localita);
	}
	
	// of course you can change the String output with what you think works best
	public List<String> trovaSequenza(int mese) {
		List<Citta> citta= new ArrayList<Citta>();
		citta.add(new Citta("Torino",this.getAllRilevamentiLocalitaMese(mese, "Torino")));
		citta.add(new Citta("Genova",this.getAllRilevamentiLocalitaMese(mese, "Genova")));
		citta.add(new Citta("Milano",this.getAllRilevamentiLocalitaMese(mese, "Milano")));
		
		cerca(citta,0,0);
		
		return soluzione;
	}
	
	private void cerca(List<Citta> citta, Integer livello, Integer giorno) {
		//TODO implementare ricorsione completa
		if(livello==15) {
			//this.soluzione.add(c);
			return;
		}
		for(Citta c:citta) {
			if(giorno<=3) {
				citta.clear();
				citta.add(c);
				costo+=c.getRilevamenti().get(livello).getUmidita();
				cerca(citta,livello+1,giorno+1);
			}else if(giorno<=6 && giorno>3) {
				//A questo livello non so se sto cambiando o meno città
				costo+=c.getRilevamenti().get(livello).getUmidita();
				cerca(citta,livello+1,giorno+1);
			}else {
				citta.remove(c);
				giorno=0;
				costo+=100;
				cerca(citta,livello+1,giorno+1);
			}
			
		}
		
	}
}
/*
 * Per ogni giorno, quindi per ogni livello, devo provare ad andare in ogni citta tenendo conto del fatto che
 * non posso cambiare città per 3 giorni perciò ogni volta che cambio città vado direttamente dal livello x al 
 * livello x+3 ovviamente tenendo conto delle umidità dei tre giorni che "salto". Se non cambio città devo 
 * valutare quale città mi genera il costo minore guardando le umidità del prossimo giorno e valutando se mi
 * convenga rimanere nella città attuale o spostarmi
 * 
 * 
 * Cosa rappresenta il livello?
 * Il livello rappresenta il giorno tra i 15 per il quale sto decidendo in quale città andare
 * 
 * Come è formata una soluzione parziale?
 * Una soluzione parziale è composta da un insieme di giorno-citta, dove viene indicato in quale citta
 * mi trovo in un determinato giorno
 * 
 * Come faccio a riconoscere se una soluzione parziale è anche completa? 
 * Una soluzione parziale è anche completa se ho scelto la città nella quale devo andare
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
 * Una soluzione di livello+1 è data dalla scelta di una città nel prossimo giorno
 * 
 * Qual è la struttura dati per memorizzare una soluzione?
 * Una lista di stringhe oppure una stringa
 * 
 * Qual è la struttura dati per memorizzare lo stato della ricerca?
 * Una struttura dati per memorizzaare lo stato della ricerca è una lista di stringhe
 * */
 