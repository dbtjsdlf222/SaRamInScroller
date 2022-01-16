package test1;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HtmlLoder {
	public static void main(String[] args) throws Exception {
		
		File input = new File("D:/Desktop/»ç¶÷ÀÎ.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		
		Elements list = doc.select("#resumeList");
		int i = 0;
		while (true) {
			try {
				if(list.select("tr").get(i).select("a").attr("data-res_idx").equals("")) {
					i++;
					continue;
				}
				System.out.println("http://www.saramin.co.kr/zf_user/mandb/view?res_idx="+list.select("tr").get(i).select("a").attr("data-res_idx"));
				i++;
			} catch (Exception e) {
				break;
			}
		}
//		System.out.println(doc.select("a").next().attr("data-res_idx"));
		
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//		org.w3c.dom.Document document = dBuilder.parse(doc);
//		System.out.println(document.getDocumentElement());
	}
}
