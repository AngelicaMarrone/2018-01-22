package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Season> getVertex(Team t, Map<Integer, Season> idMap) {
		String sql = "SELECT DISTINCT s.season, s.description " + 
				"FROM matches m, seasons s " + 
				"WHERE m.Season=s.season " + 
				"AND (m.HomeTeam=? OR m.AwayTeam=?) ";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, t.getTeam());
			st.setString(2, t.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Season s= new Season(res.getInt("season"), res.getString("description"));
				result.add(s);
				idMap.put(s.getSeason(), s);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public int getPunteggio(Team t, Season s) {
		String sql = "SELECT m.HomeTeam, m.AwayTeam, m.FTR " + 
				"FROM matches m " + 
				"WHERE m.Season=? AND (m.HomeTeam=? OR m.AwayTeam=? ) ";
		int peso=0;
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, s.getSeason());
			st.setString(2, t.getTeam());
			st.setString(3, t.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				String h= res.getString("m.HomeTeam");
				String a= res.getString("m.AwayTeam");
				String ris= res.getString("m.FTR");
				
				if (h.equals(t.getTeam()) && ris.equals("H"))
				{
					peso+=3;
				}
				
				if (a.equals(t.getTeam()) && ris.equals("A"))
				{
					peso+=3;
				}
				if (ris.equals("D"))
				{
					peso+=1;
				}
			}

			conn.close();
			return peso;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return 0;
	}

}
