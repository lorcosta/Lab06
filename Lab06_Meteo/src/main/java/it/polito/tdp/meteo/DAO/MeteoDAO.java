package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Restituisce 15 rilevamenti del mese scelto nella località fornita
	 * @param mese un intero compreso tra 01 e 12
	 * @param localita una località fra Torino, Milano e Genova
	 * @return List<{@link Rilevamento}>
	 */
	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		final String sql="SELECT Localita, Data, Umidita FROM situazione " + 
				"WHERE Localita=? AND data LIKE ? ORDER BY data ASC " + 
				"LIMIT 15 ";
		String data;
		if(mese<10)
			data="2013-0"+mese+"-%";
		else data="2013-"+mese+"-%";
		List<Rilevamento> rilevamenti=new ArrayList<Rilevamento>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);
			st.setString(2, data);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Rilevamento r= new Rilevamento(rs.getString("Localita"), rs.getDate("Data"),rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;
			
		}catch (SQLException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


}
