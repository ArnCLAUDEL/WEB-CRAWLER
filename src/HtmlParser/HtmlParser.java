package HtmlParser;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import util.Cheat;
 
public class HtmlParser {
	
	private final static String REG_NOTAG= "<[^>]*>";
	private final static String REG_NOSTYLETAG= "<style(.+?)</style>";
	private final static String REG_NOSCRYPTAG= "<script(.+?)</script>";

	private final static String REG_NOLINKTAG= "<link [^>]*/>";
	private final static String REG_NOMETATAG= "<meta [^>]*/>";

	private final static String NULL_STRING = " ";
	private final static String ESPACE_STRING = "&nbsp;";
	private final static String REG_SPEC_CHAR = "[,?;.:/\"!(),*+]";
	private final static String REG_SPEC_CHAR2 = "(&agrave|&copy|&eacute|&quot|&shy)";


	private Set<String> allKeyWord;
	private final String data;

	
	public HtmlParser(String data) {
		this.allKeyWord = new HashSet<String>();
		this.data=data;
	}
	
	public String formatTag(String s){
		s= s.replaceAll(REG_NOMETATAG, NULL_STRING);
		s= s.replaceAll(REG_NOSTYLETAG, NULL_STRING);
		s= s.replaceAll(REG_NOSCRYPTAG, NULL_STRING);
		s= s.replaceAll(REG_NOLINKTAG, NULL_STRING);
		s= s.replaceAll(REG_NOTAG, NULL_STRING);
		return s;
	}
	
	public String formatCharSpe(String s) {
		s = s.replaceAll(ESPACE_STRING, NULL_STRING);
		s = s.replaceAll(REG_SPEC_CHAR, NULL_STRING);
		s = s.replaceAll(REG_SPEC_CHAR2, NULL_STRING);
		return s;
	}
	
	
	
	public Set<String> parseTagP(){
		System.out.println(data.length());
		String result=this.data.replaceAll("\n", NULL_STRING);
		result=formatTag(result);
		result=formatCharSpe(result);
		
		for(String word : result.split(" ")) {
				Cheat.LOGGER.log(Level.FINEST, "WORD found: " + word);
				word=word.trim();
				allKeyWord.add(this.formatCharSpe(word.trim()));
			}
		return allKeyWord;
	}
	
}

