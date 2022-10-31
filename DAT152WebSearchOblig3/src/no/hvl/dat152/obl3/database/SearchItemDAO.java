package no.hvl.dat152.obl3.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchItemDAO {

	public List<SearchItem> getSearchHistoryLastFive() {
		String sql = "SELECT * FROM SecOblig.History ORDER BY datetime DESC";
		// LIMIT 5
		// Derby lacks LIMIT
		return getSearchItemList(sql, 5);
	}

	public List<SearchItem> getSearchHistoryForUser(String username) {
		String sql = "SELECT * FROM SecOblig.History " + "WHERE username = '" + username + "' ORDER BY datetime DESC";
		// LIMIT 50
		// Derby lacks LIMIT
		return getSearchItemList(sql, 50);
	}

	public List<SearchItem> getSearchHistoryForUser(String username, String sortkey) {
		String sql = "SELECT * FROM SecOblig.History " + "WHERE username = '" + username + "' ORDER BY " + sortkey
				+ " ASC";
		// LIMIT 50
		// Derby lacks LIMIT
		return getSearchItemList(sql, 50);
	}

	private List<SearchItem> getSearchItemList(String sql, Integer limit) {

		List<SearchItem> result = new ArrayList<SearchItem>();

		Connection c = null;
		Statement s = null;
		ResultSet r = null;

		Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
		
		try {
			c = DatabaseHelper.getConnection();
			s = c.createStatement();
			if (limit > 0)
				s.setMaxRows(limit);
			r = s.executeQuery(sql);

			while (r.next()) {
				SearchItem item = new SearchItem(r.getTimestamp("datetime"), r.getString("username"),
						r.getString("searchkey"));
				Matcher match = pattern.matcher(item.getSearchkey());
				boolean IsCleanSearch = match.matches();
				if(IsCleanSearch) {
					result.add(item);					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e);
		} finally {
			DatabaseHelper.closeConnection(r, s, c);
		}
		

		return result;
	}

	public void saveSearch(SearchItem search) {

		Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher match = pattern.matcher(search.getSearchkey());
		boolean IsSearchClean = match.matches();

		if (IsSearchClean) {
			String sql = "INSERT INTO SecOblig.History VALUES (" + "'" + search.getDatetime() + "', " + "'"
					+ search.getUsername() + "', " + "'" + search.getSearchkey() + "')";

			Connection c = null;
			Statement s = null;
			ResultSet r = null;

			try {
				c = DatabaseHelper.getConnection();
				s = c.createStatement();
				s.executeUpdate(sql);

			} catch (Exception e) {
				System.out.println(e);
			} finally {
				DatabaseHelper.closeConnection(r, s, c);
			}
		}else {
			System.out.println("Deez");
		}
		
	}

}
