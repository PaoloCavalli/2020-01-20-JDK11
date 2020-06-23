package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<String>  getRoles(){
		final String sql = " SELECT DISTINCT role " + 
				"FROM authorship";
		List<String> result = new ArrayList<>();
		
		try {Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result.add(res.getString("role"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Artist> getNodi(String role, Map <Integer, Artist> idMap){
		String sql = "SELECT DISTINCT  ar.artist_id as id, ar.name as name " + 
				"FROM authorship a,artists ar " + 
				"WHERE role=? AND a.artist_id= ar.artist_id";
		List<Artist> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
		
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
		      if(!idMap.containsKey(res.getInt("id"))) {
		    	  Artist a = new Artist(res.getInt("id"), res.getString("name"));
		    	  idMap.put(a.getId(), a);
		    	  result.add(a);
		    	  
		      }else {
		    	  result.add(idMap.get(res.getInt("id")));
		      }
			}
			conn.close();
			return result;
			
				
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getArchi (String role, Map<Integer,Artist> idMap){
		String sql ="SELECT a1.artist_id as a1, a2.artist_id as a2, COUNT(DISTINCT   eo1.exhibition_id ) AS peso  " + 
				"FROM authorship au1, authorship au2,exhibition_objects eo1, exhibition_objects eo2, artists a1, artists a2 " + 
				"WHERE a1.artist_id = au1.artist_id AND a2.artist_id = au2.artist_id AND au1.object_id = eo1.object_id AND au2.object_id = eo2.object_id " + 
				"AND eo1.exhibition_id = eo2.exhibition_id  AND a1.artist_id> a2.artist_id AND au1.role=? AND au2.role=? " + 
				"GROUP BY a1.artist_id, a2.artist_id " ;
		List<Adiacenza> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
		
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			st.setString(2, role);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result.add(new Adiacenza(idMap.get(res.getInt("a1")),idMap.get(res.getInt("a2")), res.getInt("peso")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
