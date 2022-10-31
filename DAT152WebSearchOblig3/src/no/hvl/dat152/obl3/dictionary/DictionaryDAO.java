package no.hvl.dat152.obl3.dictionary;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DictionaryDAO {

	private String opted_root;
	
	public DictionaryDAO(String dicturl) {
		opted_root = dicturl;
	}

	public List<String> findEntries(String word) throws Exception {
		
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher match = pattern.matcher(word);
		boolean SearchIsClean = match.matches();
		
		List<String> search_results = null;
		
		if(SearchIsClean) {
			
		String searchword = opted_root + dictFile(word.toLowerCase().charAt(0));	
		String page = null;
		try {
			page = FileReaderUtil.getWebFile(searchword);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		DictionaryParser parser = new DictionaryParser(page);
		search_results = parser.findMatchingEntries(word);
		}
		
		return search_results;
	}

	private String dictFile(char firstLetter) {
		return "wb1913_" + firstLetter + ".html";
	}

}
