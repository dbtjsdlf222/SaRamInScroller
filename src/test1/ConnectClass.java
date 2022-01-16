package test1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ConnectClass {
	public Document getHtmlString (String url) throws Exception {
		return Jsoup.connect(url)
		.cookie("PHPSESSID", "j4q0qjq0saguesvht38ou8dkp643r46f")
		.get();
	} //getHtml()
} //Class
