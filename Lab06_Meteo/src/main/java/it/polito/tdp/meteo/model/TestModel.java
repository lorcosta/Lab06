package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		System.out.println(m.getUmiditaMedia(12));
		
		System.out.println(m.trovaSequenza(1));

		//for(Rilevamento r:m.getAllRilevamentiLocalitaMese(1,"Genova")) {
		//	System.out.println(r.toString());
		//}
	}

}
