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
	private Double bestCosto=Double.MAX_VALUE;
	List<Citta> citta= new ArrayList<Citta>();
	List<Citta> soluzione;//soluzione contiene la migliore parziale possibile
	

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
	public String trovaSequenza(int mese) {
		List<Citta> parziale= new ArrayList<Citta>();
		citta.clear();
		citta.add(new Citta("Torino",this.getAllRilevamentiLocalitaMese(mese, "Torino")));
		citta.add(new Citta("Genova",this.getAllRilevamentiLocalitaMese(mese, "Genova")));
		citta.add(new Citta("Milano",this.getAllRilevamentiLocalitaMese(mese, "Milano")));
		this.soluzione=null;
		cerca(parziale,0);
		String ritorno="";
		if(soluzione!=null) {
			for(int i=0;i<this.NUMERO_GIORNI_TOTALI;i++) {
				ritorno+=soluzione.get(i).getRilevamenti().get(i).getData()+" "+soluzione.get(i).getNome()+" "+soluzione.get(i).getRilevamenti().get(i)+" "+"\n";
			}
			ritorno+="Costo totale: "+bestCosto;
		}
		return ritorno;
	}
	
	private void cerca(List<Citta> parziale, Integer livello) {
		//TODO implementare ricorsione completa
		
		if(livello==this.NUMERO_GIORNI_TOTALI) {
			//Sono all'ultimo livello e dovrei aver trovato una soluzione
			Double costo=calcolaCosto(parziale);
			if(soluzione==null || costo<bestCosto) {
				bestCosto=costo;
				this.soluzione= new ArrayList<Citta>(parziale);
				System.out.println(soluzione+" "+bestCosto);
			}
			return;
		}
		//caso generale, provare ad aggiungere a 'parziale' tutte le città presenti
		for(Citta c:citta) {
			//condizioni di filtro--> decido cosa fare in base allo stato in cui si trova parziale
			//if(la città c è valida tenendo conto delle città ad essa precedenti in parziale)
			if(this.isCittaValida(c, parziale)) {
				//provo a mettere c nella posizione 'livello' della soluzione parziale
				parziale.add(c);
				cerca(parziale,livello+1);
				parziale.remove(parziale.size()-1);
				//backtracking, rimuovo ultimo risultato appena aggiunto
			}
		}
		
	}
	/**
	 * Definisce se la città che voglio inserire nella soluzione parziale è valida o meno
	 * @param c la città per cui verificare la validità
	 * @param parziale la soluzione parziale alla quale voglio aggiungere c
	 * @return {@code true} se la citta può essere inserita, {@code false} altrimenti
	 */
	private boolean isCittaValida(Citta c, List<Citta> parziale) {
		//Una citta è valida se le precedenti sono uguali alla stessa e minori di 6
		//infatti una citta non può apparire per più di 6 volte nella sequenza
		Integer conta=0;
		for(Citta daContare:parziale) {
			if(daContare.equals(c))
				conta++;
		}
		if(conta>=this.NUMERO_GIORNI_CITTA_MAX) return false;//ritorno false perchè la citta è già apparsa il numero massimo di volte nella sequenza
		if(parziale.size()==0) {
			//non è stata inserita ancora alcuna citta perciò la città che voglio inserire è di sicuro valida
			return true;
		}
		if(parziale.size()==1 || parziale.size()==2) {
			return c.equals(parziale.get(parziale.size()-1));
		}//se mi trovo al secondo o al terzo giorno ritorno true solo se la città c che voglio inserire è la stessa del primo giorno
		/*
		int i=parziale.size(),consecutivi=0;//conta i giorni consecutivi in cui una città è uguale a quella che voglio inserire
		//Il while parte dalla fine di parziale e va a ritroso a guardare quante volte appare la città che voglio inserire
		while(c.equals(parziale.get(i)) && i>=0) {
			consecutivi++;
			i--;
		}
		if(consecutivi<3 && c.equals(parziale.get(parziale.size()-1))) return true;
		else if( consecutivi>=3 && consecutivi<6) return true;
		else if(consecutivi>6) return false;
		Questo modo non funziona perchè io pensavo soltanto a sequenze dove le città apparivano "in blocco"
		ma potenzialmente una sequenza potrebbe essere T-T-T-M-M-M-G-G-G-T-T-T-M-M-M e nel secondo inserimento di 
		Torino devo accorgermi che torino è già stata inserita e non potrò inserirla altre 6 volte ma solo 3*/
		//sono più avanti del livello 3 e sono sicuro di non aver inserito la città più di 6 volte perciò se 
		//la città da inserire è la stessa in cui già sono presente posso rimanere accertandomi che la città precedente sia la stessa che voglio inserire
		if(c.equals(parziale.get(parziale.size()-1)))
			return true;
		//se invece la città che voglio inserire è differente da quella in cui sono stato in precedenza, mi devo 
		//assicurare di essere stato almeno tre giorni nella scorsa città frequentata
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && 
				parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;//nella scorsa città ci sono rimasto tre giorni e posso cambiarla
		return false;
	}
	/**
	 * Calcola il costo di una soluzione parziale completa
	 * @param parziale una soluzione parziale completa fino all'ultimo livello
	 * @return costo il costo della soluzione passata come parametro
	 */
	private double calcolaCosto(List<Citta> parziale) {
		double costo=0.0;
		for(int i=2;i<=this.NUMERO_GIORNI_TOTALI;i++) {
			if(!parziale.get(i-1).equals(parziale.get(i-2))) {
				costo+=this.COST;
			}
		}
		for(int i=0;i<this.NUMERO_GIORNI_TOTALI;i++) {
			costo+=parziale.get(i).getRilevamenti().get(i).getUmidita();
		}
		return costo;
	}
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
 