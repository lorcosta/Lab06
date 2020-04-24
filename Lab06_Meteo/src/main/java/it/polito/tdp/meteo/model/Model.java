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
	private Integer bestCosto=Integer.MAX_VALUE,costo=0;
	List<Citta> citta= new ArrayList<Citta>();
	List<List<Citta>> soluzione= new ArrayList<List<Citta>>();//soluzione contiene varie combinazioni di citta
	

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
	/**
	 * Genera tutte le sequenze di città 
	 * @param mese
	 * @return lista ordinata delle città da visitare
	 */
	// of course you can change the String output with what you think works best
	public List<List<Citta>> trovaSequenza(int mese) {
		List<Citta> parziale= new ArrayList<Citta>();
		citta.add(new Citta("Torino",this.getAllRilevamentiLocalitaMese(mese, "Torino")));
		citta.add(new Citta("Genova",this.getAllRilevamentiLocalitaMese(mese, "Genova")));
		citta.add(new Citta("Milano",this.getAllRilevamentiLocalitaMese(mese, "Milano")));
		
		cerca(parziale,0,0);
		
		return soluzione;
	}
	
	private void cerca(List<Citta> parziale, Integer livello, Integer giorno) {
		//TODO implementare ricorsione completa
		
		if(livello==15) {
			//Sono all'ultimo livello e dovrei aver trovato una soluzione
			if(costo<bestCosto) {
				bestCosto=costo;
				this.soluzione.add(parziale);
				System.out.println(parziale+" "+bestCosto);
			}
			return;
		}
		//caso generale, provare ad aggiungere a 'parziale' tutte le città presenti
		for(Citta c:citta) {
			//provo a mettere c nella posizione 'livello' della soluzione parziale
			parziale.add(c);
			//condizioni di filtro--> decido cosa fare in base allo stato in cui si trova parziale
			//scegleire tra filtro sul giorno o filtro sulla citta precedente
			// se c!=c-1 allora vado direttamente a livello+3 e giorno+3
			if(livello!=0 && !parziale.get(livello-1).equals(c) && giorno<3) {
				cerca(parziale,livello+3,giorno+3);
			}
			// se c==c-1 allora vado a livello+1 e giorno+1 con qualsiasi città fino a che giorno<6
			else if (livello!=0 && parziale.get(livello-1).equals(c) && giorno>3 && giorno<=6) {
				//I giorni sono compresi fra 3 e 6, devo decidere dove andare in base al costo minore
				cerca(parziale,livello+1,giorno+1);
			}else {
				//I giorni sono più di 6 perciò azzero il contatore e cambio la città
				giorno=0;
				cerca(parziale,livello+1,giorno+1);
			}
			
			//backtracking, rimuovo ultimo risultato appena aggiunto
			parziale.remove(parziale.size()-1);	
		}
		
	}

	/*private Citta cercaCostoMinore(Integer livello) {
		Integer confronto=101;
		Citta daRitornare = null;
		for(Citta c:citta) {
			if(c.getRilevamenti().get(livello+1).getUmidita()<confronto) {
				confronto=c.getRilevamenti().get(livello+1).getUmidita();
				daRitornare=c;
			}
		}
		return daRitornare;
	}*/
}
/*
 * Dato di partenza:un insieme di città da dividere nei 15 giorni
 * Soluzione parziale:una parte della sequenza delle città nei giorni già decisa
 * Livello: numero di giorni di cui è composta la soluzione parziale, numero di giorni 
 * 			per i quali ho già deciso in che città andare
 * Soluzione finale:soluzione di lunghezza 15, caso terminale
 * Caso terminale: salvare la soluzione trovata
 * Generazione delle nuove soluzioni:provare a aggiungere una città scegliendola tra-->
 * Se sono in una città da più di 6 giorni scegliere tra le città in cui non mi trovo al momento
 * Se sono in una città da meno di 3 giorni scegliere la città in cui sono per i restanti giorni
 * Se sono in una città da più di 3 e meno di 6 giorni scegliere quella che mi genera il costo minore
 * 
 * se c!=c-1 allora vado direttamente a livello+3 e giorno+3
 * se c==c-1 allora vado a livello+1 e giorno+1 con qualsiasi città fino a che giorno<6
 * 
 * Per ogni giorno, quindi per ogni livello, devo provare ad andare in ogni citta tenendo conto del fatto che
 * non posso cambiare città per 3 giorni perciò ogni volta che cambio città vado direttamente dal livello x al 
 * livello x+3 ovviamente tenendo conto delle umidità dei tre giorni che "salto". Se non cambio città devo 
 * valutare quale città mi genera il costo minore guardando le umidità del prossimo giorno e valutando se mi
 * convenga rimanere nella città attuale o spostarmi
 * 
 * 
 * Cosa rappresenta il livello?
 * Il livello rappresenta il giorno tra i 15 per il quale sto decidendo in quale città andare,
 * mi dice in quale giorno sono e per questo giorno che considero devo decidere la città
 * 
 * Come è formata una soluzione parziale?
 * Una soluzione parziale è una parte di soluzione ovvero è una parte di lista di città
 * E' un insieme di città dove ho già deciso che andrò
 * 
 * Come faccio a riconoscere se una soluzione parziale è anche completa? 
 * Una soluzione parziale è anche completa se ho scelto la città nella quale devo andare per 
 * tutti e 15 i giorni
 * 
 * Data una soluzione parziale come faccio a sapere se è valida o non è valida?
 * Una soluzione parziale è valida se rimango nelle città che ho visitato almeno 3 e non 
 * più di 6 giorni e ho visitato tutte le città
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
 