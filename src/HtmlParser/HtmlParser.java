package HtmlParser;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import util.Cheat;
 
public class HtmlParser {
	
	private final static String REG_NOTAG= "<[^>]*>";
	private final static String NULL_STRING = " ";
	private final static String ESPACE_STRING = "&nbsp;";
	private final static String REG_SPEC_CHAR = "[,?;.:/\"!()]";

	private Set<String> allKeyWord;
	private final String data;

	
	public HtmlParser(String data) {
		this.allKeyWord = new HashSet<String>();
		this.data=data;
	}
	
	public String formatTag(String s) {
		return s.replaceAll(REG_NOTAG, NULL_STRING);
	}
	
	public String formatCharSpe(String s) {
		s = s.replaceAll(ESPACE_STRING, NULL_STRING);
		s = s.replaceAll(REG_SPEC_CHAR, NULL_STRING);
		return s;
	}
	
	
	
	public void parseTagP() {
		String result=this.data.replaceAll("\n", NULL_STRING);
		result=formatTag(result);
		result=formatCharSpe(result);
		
		for(String word : result.split(" ")) {
				Cheat.LOGGER.log(Level.FINEST, "WORD found: " + word);
				allKeyWord.add(this.formatCharSpe(word));
			}
	}
	
	
	public Set<String> getAllKeyWord(){
		return allKeyWord;
	}
	
	
	
	public static void main(String[] args) {	
		
		String page = "<html><head>\n" + 
				"<title>Jean-Philippe BARRALIS - Culture Numérique...</title>\n" + 
				"<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\">\n" + 
				"<link media=\"screen\" href=\"fichiers/style.css\" type=\"text/css\" rel=\"stylesheet\">\n" + 
				"<link rel=\"shortcut icon\" href=\"http://jp.barralis.com/ico-barralis.ico\">\n" + 
				"<style></style></head>\n" + 
				"<body>\n" + 
				"<div id=\"page\">\n" + 
				"<div id=\"top\">\n" + 
				"<div id=\"topin\">\n" + 
				"<h1>Culture Numérique </h1>\n" + 
				"</div></div>\n" + 
				"<div id=\"main\">\n" + 
				"<div id=\"content\">\n" + 
				"<div class=\"post\">\n" + 
				"<p class=\"day-date\">&nbsp;</p>\n" + 
				"<h2 class=\"post-title\" id=\"p3\">Culture Numérique... </h2>\n" + 
				"<div class=\"post-content\" lang=\"fr\">\n" + 
				"<p> Cette section regroupe une <strong>sélection d'articles de qualité</strong> qui m'ont servi dans mes travaux et recherches au fil du temps. Ces articles ont eu un impact fort sur ma manière de voir et de comprendre l'informatique contemporaine. \n" + 
				"  Les thèmes traités sont : <strong>Linux &amp; Windows</strong>, le <strong>mouvement OpenSource</strong> et les <strong>nouvelles technologies de l'information et de la communication</strong>.<br>\n" + 
				"  <br>\n" + 
				"  Il est à noter que les textes, propos et concepts appartiennent à leurs auteurs respectifs. Ils en restent pleinement propriétaires. \n" + 
				" Cette page ne fait que compiler les papiers que je juge les plus intéressants et sera mis à jour régulièrement.<br>\n" + 
				" Si vous connaissez d'autres articles dans le <strong>même esprit</strong> que vous voulez voir apparaitre ici, n'hésitez pas à <a href=\"contact.php\">m'en faire part</a>.<br>\n" + 
				" <br>\n" + 
				"</p>\n" + 
				"          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" + 
				"            <tbody><tr> \n" + 
				"              <td width=\"4%\" bgcolor=\"#7DBE30\">&nbsp;</td>\n" + 
				"              <td width=\"96%\">&nbsp;<strong>Titre </strong>: <a href=\"articles/Web2.0.pdf\" target=\"_blank\"><strong>Qu'est-ce \n" + 
				"                que le Web 2.0 ?</strong></a></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Auteur</strong> : <em>Tim O'Reilly</em> &nbsp;&nbsp;&nbsp;&nbsp;<br> \n" + 
				"                <strong>Année de publication</strong> : <em>2005</em>&nbsp;&nbsp;&nbsp;&nbsp;<strong>langue</strong> \n" + 
				"                : <em>français</em> </td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Court résumé</strong> : Le texte fondateur \n" + 
				"                du concept de <em>WEB 2.0</em> vu par <a href=\"http://www.oreilly.fr\" target=\"_blank\">l'éditeur \n" + 
				"                Tim O'reilly</a>. <br>\n" + 
				"                Tim O'reilly explique ici, pour la première fois, le principe \n" + 
				"                et la portée de ce qui va devenir l'enjeu du Web d'aujourd'hui. \n" + 
				"                <br>\n" + 
				"                Un texte déjà considéré, par beaucoup, \n" + 
				"                comme étant une référence en la matière. \n" + 
				"                A lire absolument.</td>\n" + 
				"            </tr>\n" + 
				"            <tr>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td> <div align=\"center\">\n" + 
				"                  <script type=\"text/javascript\"><!--\n" + 
				"google_ad_client = \"pub-3923891397005798\";\n" + 
				"google_alternate_color = \"ffffff\";\n" + 
				"google_ad_width = 468;\n" + 
				"google_ad_height = 60;\n" + 
				"google_ad_format = \"468x60_as\";\n" + 
				"google_ad_type = \"text\";\n" + 
				"google_ad_channel = \"\";\n" + 
				"google_color_border = \"FFFFFF\";\n" + 
				"google_color_bg = \"FFFFFF\";\n" + 
				"google_color_link = \"7DBE30\";\n" + 
				"google_color_text = \"000000\";\n" + 
				"google_color_url = \"7DBE30\";\n" + 
				"//--></script>\n" + 
				"                  <script type=\"text/javascript\" src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\"></script>\n" + 
				"                </div></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td width=\"4%\" bgcolor=\"#7DBE30\">&nbsp;</td>\n" + 
				"              <td width=\"96%\">&nbsp;<strong>Titre </strong>: <a href=\"articles/Gordon_Moore_1965_Article.pdf\" target=\"_blank\"><strong>Cramming \n" + 
				"                more components onto integrated circuits</strong></a></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Auteur</strong> : <em>Gordon Moore</em> &nbsp;&nbsp;&nbsp;&nbsp;<br> \n" + 
				"                <strong>Année de publication</strong> : <em>1965</em>&nbsp;&nbsp;&nbsp;&nbsp;<strong>langue</strong> \n" + 
				"                : <em>anglais</em> </td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Court résumé</strong> : Loi sur l'évolution \n" + 
				"                de la fréquence des microprocesseurs.</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td bgcolor=\"#7DBE30\">&nbsp;</td>\n" + 
				"              <td>&nbsp;<strong>Titre </strong>: <a href=\"articles/ledTanenbaum.pdf\" target=\"_blank\"><strong>Le \n" + 
				"                débat Tanenbaum-Torvalds</strong></a></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Auteur</strong> : <em>Inconnu</em> &nbsp;&nbsp;&nbsp;&nbsp;<strong><br>\n" + 
				"                Année de publication</strong> : <em>inconnu</em>&nbsp;&nbsp;&nbsp;&nbsp;<strong>langue</strong> \n" + 
				"                : <em>français</em></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Court résumé</strong> : Le débat \n" + 
				"                qui opposa au début des années 90 Linus Torvalds \n" + 
				"                (créateur de Linux) et Andrew Tanenbaum (professeur d'informatique \n" + 
				"                mondialement connu). </td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><div align=\"center\"> \n" + 
				"                  <script type=\"text/javascript\"><!--\n" + 
				"google_ad_client = \"pub-3923891397005798\";\n" + 
				"google_alternate_color = \"ffffff\";\n" + 
				"google_ad_width = 468;\n" + 
				"google_ad_height = 60;\n" + 
				"google_ad_format = \"468x60_as\";\n" + 
				"google_ad_type = \"text\";\n" + 
				"google_ad_channel = \"\";\n" + 
				"google_color_border = \"FFFFFF\";\n" + 
				"google_color_bg = \"FFFFFF\";\n" + 
				"google_color_link = \"7DBE30\";\n" + 
				"google_color_text = \"000000\";\n" + 
				"google_color_url = \"7DBE30\";\n" + 
				"//--></script>\n" + 
				"                  <script type=\"text/javascript\" src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\"></script>\n" + 
				"                </div></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td bgcolor=\"#7DBE30\">&nbsp;</td>\n" + 
				"              <td>&nbsp;<strong>Titre </strong>: <a href=\"articles/recoveryShells.pdf\" target=\"_blank\"><strong>A \n" + 
				"                recovery Mechanism for Shells</strong></a></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Auteurs</strong> : <em>Ian HOLYER, Huseyin PEHLIVAN</em> \n" + 
				"                &nbsp;&nbsp;&nbsp;&nbsp;<br> <strong>Année de publication</strong> \n" + 
				"                : <em>1998</em>&nbsp;&nbsp;&nbsp;&nbsp;<strong>langue</strong> \n" + 
				"                : <em>anglais</em></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Court résumé</strong> : Travail intéressant \n" + 
				"                sur le mode de fonctionnement des Shells contemporains.</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td bgcolor=\"#7DBE30\">&nbsp;</td>\n" + 
				"              <td>&nbsp;<strong>Titre </strong>: <a href=\"articles/culturedudon.pdf\" target=\"_blank\"><strong>Culture \n" + 
				"                du don dans le logiciel libre</strong></a></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Auteur</strong> : Matthias Studer&nbsp;&nbsp;&nbsp;&nbsp;<strong><br>\n" + 
				"                Année de publication</strong> : <em>2004</em>&nbsp;&nbsp;&nbsp;&nbsp;<strong>langue</strong> \n" + 
				"                : <em>français</em></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Court résumé</strong> : Comment fonctionne \n" + 
				"                la communauté du logiciel libre ? Quelles sont les règles \n" + 
				"                fédératrices de l'OpenSource ? </td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td bgcolor=\"#7DBE30\">&nbsp;</td>\n" + 
				"              <td>&nbsp;<strong>Titre </strong>: <strong><a href=\"articles/freeasabeer.pdf\" target=\"_blank\">Logiciels \n" + 
				"                libres : free as a beer</a></strong></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Auteurs</strong> : B. LEMAIRE, B. DECROOCQ&nbsp;&nbsp;&nbsp;&nbsp;<strong></strong><strong><br>\n" + 
				"                Année de publication</strong> : <em>2004</em><strong>&nbsp;&nbsp;&nbsp;&nbsp;langue</strong> \n" + 
				"                : <em>français</em></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Court résumé</strong> : Valeurs du libre, \n" + 
				"                valeurs de l’entreprise : Une hybridation impossible ?</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><div align=\"center\"> \n" + 
				"                  <script type=\"text/javascript\"><!--\n" + 
				"google_ad_client = \"pub-3923891397005798\";\n" + 
				"google_alternate_color = \"ffffff\";\n" + 
				"google_ad_width = 468;\n" + 
				"google_ad_height = 60;\n" + 
				"google_ad_format = \"468x60_as\";\n" + 
				"google_ad_type = \"text\";\n" + 
				"google_ad_channel = \"\";\n" + 
				"google_color_border = \"FFFFFF\";\n" + 
				"google_color_bg = \"FFFFFF\";\n" + 
				"google_color_link = \"7DBE30\";\n" + 
				"google_color_text = \"000000\";\n" + 
				"google_color_url = \"7DBE30\";\n" + 
				"//--></script>\n" + 
				"                  <script type=\"text/javascript\" src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\"></script>\n" + 
				"                </div></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td bgcolor=\"#7DBE30\">&nbsp;</td>\n" + 
				"              <td>&nbsp;<strong>Titre</strong> : <a href=\"articles/Piegedanslecyberespace.pdf\" target=\"_blank\"><strong>Piège \n" + 
				"                dans le cyberespace</strong></a></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Auteurs</strong> : Roberto Di COSMO&nbsp;&nbsp;&nbsp;&nbsp;<strong></strong><strong><br>\n" + 
				"                Année de publication</strong> : 1998<strong>&nbsp;&nbsp;&nbsp;&nbsp;langue</strong> \n" + 
				"                : <em>français</em></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Court résumé</strong> : Une autre vision \n" + 
				"                des logiciels libre comparée au mode de fonctionnement \n" + 
				"                de Microsoft.</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td bgcolor=\"#7DBE30\">&nbsp;</td>\n" + 
				"              <td>&nbsp;<strong>Titre </strong>: <a href=\"articles/purposemacrogenerator.pdf\" target=\"_blank\"><strong>A \n" + 
				"                general purpose Macrogenerator</strong></a> </td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Auteurs</strong> : C. STRACHEY &nbsp;&nbsp;&nbsp;&nbsp;<br> \n" + 
				"                <strong></strong><strong>Année de publication</strong> \n" + 
				"                : dans les années 1960 <strong>&nbsp;&nbsp;&nbsp;&nbsp;langue</strong> \n" + 
				"                : <em>anglais</em></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Court résumé</strong> : Les explications \n" + 
				"                de fonctionnement du \"Shell contemporain\" ??? </td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td bgcolor=\"#7DBE30\">&nbsp;</td>\n" + 
				"              <td>&nbsp;<strong>Titre</strong>: <a href=\"articles/denotionalsemantics.pdf\" target=\"_blank\"><strong>Denotional \n" + 
				"                Semantics of a command interpreter and their implementation in \n" + 
				"                Standard ML</strong></a></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Auteurs</strong> : C. McDonald, L. ALLISON &nbsp;&nbsp;&nbsp;&nbsp;<strong></strong><strong><br>\n" + 
				"                Année de publication</strong> : 1988 <strong>&nbsp;&nbsp;&nbsp;&nbsp;langue</strong> \n" + 
				"                : <em>anglais</em></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Court résumé</strong> : Un excellent article \n" + 
				"                sur le mode de focntionnement des interpréteurs de commandes. \n" + 
				"              </td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td bgcolor=\"#7DBE30\">&nbsp;</td>\n" + 
				"              <td>&nbsp;<strong>Titre </strong>: <a href=\"articles/Microsoftprisdanslatoile.pdf\" target=\"_blank\"><strong>Microsoft \n" + 
				"                pris dans la toile… chronique d’une mort annoncée \n" + 
				"                ?</strong></a></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Auteurs</strong> : B. LEMAIRE, B. DECROOCQ&nbsp;&nbsp;&nbsp;&nbsp;<strong></strong><strong><br>\n" + 
				"                Année de publication</strong> : 2004 <strong>&nbsp;&nbsp;&nbsp;&nbsp;langue</strong> \n" + 
				"                : <em>français</em></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><strong>Court résumé</strong> : Une excellente \n" + 
				"                vision du mode de fonctionnement de Microsoft et de ses probables \n" + 
				"                conséquences. Très intéressant.</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td><div align=\"center\">\n" + 
				"                  <script type=\"text/javascript\"><!--\n" + 
				"google_ad_client = \"pub-3923891397005798\";\n" + 
				"google_alternate_color = \"ffffff\";\n" + 
				"google_ad_width = 468;\n" + 
				"google_ad_height = 60;\n" + 
				"google_ad_format = \"468x60_as\";\n" + 
				"google_ad_type = \"text\";\n" + 
				"google_ad_channel = \"\";\n" + 
				"google_color_border = \"FFFFFF\";\n" + 
				"google_color_bg = \"FFFFFF\";\n" + 
				"google_color_link = \"7DBE30\";\n" + 
				"google_color_text = \"000000\";\n" + 
				"google_color_url = \"7DBE30\";\n" + 
				"//--></script>\n" + 
				"                  <script type=\"text/javascript\" src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\"></script>\n" + 
				"                </div></td>\n" + 
				"            </tr>\n" + 
				"            <tr> \n" + 
				"              <td>&nbsp;</td>\n" + 
				"              <td>&nbsp;</td>\n" + 
				"            </tr>\n" + 
				"          </tbody></table>\n" + 
				"<p>    <br>\n" + 
				"    <br>\n" + 
				"</p>\n" + 
				"</div>\n" + 
				"</div>\n" + 
				"</div>\n" + 
				"</div>\n" + 
				"<div id=\"sidebar\">\n" + 
				"<div id=\"links\">\n" + 
				"<h2>&nbsp;</h2>\n" + 
				"<ul>\n" + 
				"  <li><a href=\"http://jp.barralis.com/\"><b>Home</b></a>\n" + 
				"  </li><li><a href=\"http://jp.barralis.com/culture-numerique.php\"><b>Culture Numérique</b></a>\n" + 
				"  </li><li><a href=\"http://jp.barralis.com/scripting-shell.php\"><b>Scripting Shell</b></a>\n" + 
				"  </li><li><a href=\"http://jp.barralis.com/linux-man/\"><b>Linux : MAN en français</b></a>\n" + 
				"  </li><li><a href=\"http://jp.barralis.com/linux-man/result.php\" style=\"color: #BA4444\"><b>NEW : Rechercher un MAN !</b></a>\n" + 
				"  </li><li><a href=\"http://jp.barralis.com/howto/linux/index.php\"><b>Linux² : HOWTO en français</b></a>\n" + 
				"  </li><li><a href=\"http://jp.barralis.com/wishlist.php\"><b>Faire un don Paypal...</b></a>\n" + 
				"    </li><li>\n" + 
				"</li></ul>\n" + 
				"<hr size=\"1\"><br>\n" + 
				"<div align=\"center\">\n" + 
				"<form action=\"https://www.paypal.com/cgi-bin/webscr\" method=\"post\">\n" + 
				"<input type=\"hidden\" name=\"cmd\" value=\"_s-xclick\">\n" + 
				"<input type=\"hidden\" name=\"encrypted\" value=\"-----BEGIN PKCS7-----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-----END PKCS7-----\n" + 
				"\">\n" + 
				"<input type=\"image\" src=\"https://www.paypal.com/fr_FR/FR/i/btn/btn_donate_SM.gif\" border=\"0\" name=\"submit\" alt=\"Vous trouvez ce site utile ? Faites un don paypal !\" title=\"Vous trouvez ce site utile ? Faites un don paypal !\">\n" + 
				"<img alt=\"\" border=\"0\" src=\"https://www.paypal.com/fr_FR/i/scr/pixel.gif\" width=\"1\" height=\"1\">\n" + 
				"</form>\n" + 
				"</div><br>\n" + 
				"<div align=\"center\">\n" + 
				"<script type=\"text/javascript\"><!--\n" + 
				"google_ad_client = \"pub-3923891397005798\";\n" + 
				"google_ad_width = 120;\n" + 
				"google_ad_height = 600;\n" + 
				"google_ad_format = \"120x600_as\";\n" + 
				"google_ad_type = \"text_image\";\n" + 
				"google_ad_channel =\"\";\n" + 
				"google_color_border = \"FFFFFF\";\n" + 
				"google_color_bg = \"FFFFFF\";\n" + 
				"google_color_link = \"7DBE30\";\n" + 
				"google_color_url = \"7DBE30\";\n" + 
				"google_color_text = \"000000\";\n" + 
				"//--></script>\n" + 
				"<script type=\"text/javascript\" src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\">\n" + 
				"</script>\n" + 
				"</div>\n" + 
				"</div></div>\n" + 
				"<p id=\"footer\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://jp.barralis.com\">home</a> \n" + 
				"  - <a href=\"http://jp.barralis.com/utilisation.php\">conditions d'utilisation</a> - <a href=\"http://jp.barralis.com/contact.php\">contact</a> - <a href=\"http://jp.barralis.com/wishlist.php\">Faire un don Paypal !</a></p><a href=\"http://jp.barralis.com/wishlist.php\">\n" + 
				"<script type=\"text/javascript\" src=\"http://track.mybloglog.com/js/jsserv.php?mblID=2008061400421560\"></script></a></div><a href=\"http://jp.barralis.com/wishlist.php\">\n" + 
				"<div id=\"pied\"><img height=\"8\" alt=\"bottom.png\" src=\"fichiers/bottom.png\" width=\"846\"></div>\n" + 
				"<script src=\"http://www.google-analytics.com/urchin.js\" type=\"text/javascript\">\n" + 
				"</script>\n" + 
				"<script type=\"text/javascript\">\n" + 
				"_uacct = \"UA-68412-1\";\n" + 
				"urchinTracker();\n" + 
				"</script>\n" +  
				"</a></body></html>";
		
		HtmlParser hparse = new HtmlParser(page);
		hparse.parseTagP();
		
		System.out.println(hparse.allKeyWord);
	}
}

