package test1;

import org.jsoup.select.Elements;

public class License {
	public String getLicense(Elements el) {
		String query="";
		Main.linguistics = "";
		try {
			String[] careerList = el.toString().split("</tr>");
			int y = 0, licenseCount = 0, linguisticscount = 0;
			Linguistics linguisticsClass = new Linguistics();
			for(String careerStr:careerList) {
				if(y>careerList.length-2) { break; } //for�� �ѹ� ������ ���� ����
				String[] careerTemp = careerStr.split("</td>");
				try {
					if(!Main.repl(careerTemp[1]).contains("�ڰ���")) {
						if(Main.repl(careerTemp[1]).contains("����")) {
							if(linguisticscount==0)
								linguisticsClass.setLinguistics(careerTemp[0], careerTemp[2], careerTemp[3], careerTemp[4], linguisticscount);
							linguisticscount++;
						}
						continue;
					} //if
				} catch (Exception e) { continue; } //try~catch
				licenseCount++;
				
//				System.out.println(aa.repl(careerTemp[0])); //��泯¥
//				System.out.println(aa.repl(careerTemp[1])); //�з�
//				System.out.println(aa.repl(careerTemp[2])); //�ڰ�����
//				System.out.println(aa.repl(careerTemp[3])); //�߱�ó
//				System.out.println(aa.repl(careerTemp[4])); //��������
				
				query = query.concat(
						"i:"+y+";"+
						"a:3:{"+
							"s:13:\"qualification\";"+
							"s:"+Main.repl(careerTemp[2]).getBytes().length+":\""+Main.repl(careerTemp[2])+"\";"+
							"s:12:\"public_place\";"+
							"s:"+Main.repl(careerTemp[3]).getBytes().length+":\""+Main.repl(careerTemp[3])+"\";"+
							"s:11:\"obtain_date\";"+
							"s:"+(Main.repl(careerTemp[0])+"-01").getBytes().length+":\""+Main.repl(careerTemp[0]).replace('.', '-')+"-01\";"+
						"}"
						); //concat()
					y++;
			} //for
			
			Main.linguistics = "a:"+linguisticscount+":{"+Main.linguistics;
			
			query = "a:"+(licenseCount-1)+":{" + query;
			query = query.concat("}");
			Main.linguistics = Main.linguistics + "}";
		} catch (Exception e) {System.err.println("�ڰ��� ����"); } //try~catch
		
		return query;
		
	} //main
}