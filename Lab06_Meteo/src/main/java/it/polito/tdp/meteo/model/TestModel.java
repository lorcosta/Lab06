package it.polito.tdp.meteo.model;

import java.time.LocalDate;
import java.util.Date;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		//System.out.println(m.getUmiditaMedia(12));
		
		System.out.println(m.trovaSequenza(5));
		
		LocalDate date=LocalDate.of(2020, 04, 21);
		System.out.println(date.getMonthValue());
		
		Date data= new Date(2020,04,21);
		System.out.println(data.getMonth());
	}

}
