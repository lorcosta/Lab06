package it.polito.tdp.meteo.model;

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
	public String trovaSequenza(int mese) {
		return "TODO!";
	}
	

}
