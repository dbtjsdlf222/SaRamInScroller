package test1;
import org.jsoup.select.Elements;

public class schoolClass {
	
	public String getSchool(Elements schoolEl,String url,Elements carrer) throws Exception {
		
		String[] schoolList = schoolEl.toString().split("</tr>");
		String schoolQuery = "";
		String schoolStr = carrer.toString().split("</em>")[0].replaceAll("<span class=\"bar\">", "").replaceAll("<em>", "(")+")";
		
		int index = 0;
		int degreeCount = 0;
		while(true) {
			try {
				if(schoolEl.select("tr").get(index).toString().contains("colspan=\"3\"")) { index++; degreeCount++; continue; }
				String lesson_sdate = schoolEl.select("tr").get(index).select("td").get(0).text().split(" ~ ")[0].replace('.', '-')+"-01";
				String lesson_edate = schoolEl.select("tr").get(index).select("td").get(0).text().split(" ~ ")[1].replace('.', '-')+"-01";
				String lesson_state = schoolEl.select("tr").get(index).select("td").get(1).text(); //��������
				String school = schoolEl.select("tr").get(index).select("td").get(2).text().replaceAll(" ", ""); //�б���
				String lesson = schoolEl.select("tr").get(index).select("td").get(3).text().replaceAll(" ", ""); //�а�
				
				schoolQuery = schoolQuery.concat("i:"+(index-degreeCount)+";");
				schoolQuery = schoolQuery.concat("a:6:{");
				schoolQuery = schoolQuery.concat("s:11:\"scholarship\";");
				schoolQuery = schoolQuery.concat("s:");
				if(schoolStr.contains("����б�")) {
					schoolQuery = schoolQuery.concat("8:\"����б�\";");
				} else {
					schoolQuery = schoolQuery.concat(schoolStr.getBytes().length+":\""+schoolStr+"\";");
//					schoolQuery = schoolQuery.concat(schoolStr+"("+carrer.select("em").text()+")".getBytes().length);
//					schoolQuery = schoolQuery.concat(":\""+carrer.select(".bar").text()+"("+carrer.select("em").text()+")\"");
				}
				
				schoolQuery = schoolQuery.concat("s:6:\"school\"; s:"+school.getBytes().length+":\""+school+"\";");
				schoolQuery = schoolQuery.concat("s:6:\"lesson\"; s:"+lesson.getBytes().length+":\""+lesson+"\";");
				schoolQuery = schoolQuery.concat("s:12:\"lesson_sdate\"; s:"+lesson_sdate.getBytes().length+":\""+lesson_sdate+"\";");
				schoolQuery = schoolQuery.concat("s:12:\"lesson_edate\"; s:"+lesson_edate.getBytes().length+":\""+lesson_edate+"\";");
				schoolQuery = schoolQuery.concat("s:12:\"lesson_state\"; s:"+lesson_state.getBytes().length+":\""+lesson_state+"\";");
				schoolQuery = schoolQuery.concat("}");
				
				index++;
			} catch (Exception e) { break; }
		}
		
//		try {
//			for(String school : schoolList) {
//				if(school.contains("colspan=\"3\"")) { continue; }
//				if(i>schoolList.length-2) { break; } //for�� �ѹ� ������ ���� ����
//				String[] schoolSpl = school.split("</td>"); //�ϳ��� �з� ������ ����
//				
//				String schoolStr = carrer.toString().split("</em>")[0].replaceAll("<span class=\"bar\">", "").replaceAll("<em>", "(")+")";
//				
//				//<span class="bar"><em>����б�</em>����</span>
//				
//			System.out.println(schoolStr.getBytes().length+":\""+schoolStr+"\"");
//				schoolQuery = schoolQuery.concat("i:"+schoolIndex+";");
//				schoolQuery = schoolQuery.concat("a:6:{");
//				schoolQuery = schoolQuery.concat("s:11:\"scholarship\";");
//				schoolQuery = schoolQuery.concat("s:");
//				if(schoolStr.contains("����б�")) {
//					schoolQuery = schoolQuery.concat("8:\"����б�\"");
//				} else {
//					schoolQuery = schoolQuery.concat(schoolStr.getBytes().length+":\""+schoolStr+"\";");
////					schoolQuery = schoolQuery.concat(schoolStr+"("+carrer.select("em").text()+")".getBytes().length);
////					schoolQuery = schoolQuery.concat(":\""+carrer.select(".bar").text()+"("+carrer.select("em").text()+")\"");
//				}
//				schoolQuery = schoolQuery.concat(";");
//				
////				if(aa.repl(schoolSpl[2]).contains("�ʵ�")) {				schoolQuery = schoolQuery.concat("8:\"�ʵ��б�\"");
////				} else if(aa.repl(schoolSpl[2]).contains("���б�")){		schoolQuery = schoolQuery.concat("6:\"���б�\"");
////				} else if(aa.repl(schoolSpl[2]).contains("����б�")){   	schoolQuery = schoolQuery.concat("8:\"����б�\"");
////				} else if(aa.repl(schoolSpl[2]).contains("����")){ 			schoolQuery = schoolQuery.concat("13:\"���б�(2~3��)\"");
////				} else if(aa.repl(schoolSpl[2]).contains("���б�(����)")){  schoolQuery = schoolQuery.concat("12:\"���б�(����)\"");
////				} else if(aa.repl(schoolSpl[2]).contains("���б�(�ڻ�)")){  schoolQuery = schoolQuery.concat("12:\"���б�(�ڻ�)\""); }
////				else { schoolQuery = schoolQuery.concat(dashBoard.substring(dashBoard.indexOf("�з»���") + 5, dashBoard.indexOf("��»���")).split(" ")[0].getBytes().length+":"+dashBoard.substring(dashBoard.indexOf("�з»���") + 5, dashBoard.indexOf("��»���")).split(" ")[0]+"\""); }
////				schoolQuery = schoolQuery.concat("s:6:\"school\");s:�б���.lenght:�б���;");
////				schoolEl.select("tr").get(0).text()
//
//				
//				schoolQuery = schoolQuery.concat("s:6:\"school\"; s:"+aa.repl(schoolSpl[2]).getBytes().length+":\""+aa.repl(schoolSpl[2])+"\";"); //�б���
//				schoolQuery = schoolQuery.concat("s:6:\"lesson\"; s:"+aa.repl(schoolSpl[3]).getBytes().length+":\""+aa.repl(schoolSpl[3])+"\";"); //�а�
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_sdate\"; s:"+aa.repl(schoolSpl[0].split("~")[0].trim().replace('.', '-').concat("-01")).getBytes().length+":\""+aa.repl(schoolSpl[0].split("~")[0].trim().replace('.', '-')).concat("-01")+"\";"); //���г�¥
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_edate\"; s:"+aa.repl(schoolSpl[0].split("~")[1].trim().replace('.', '-').concat("-01")).getBytes().length+":\""+aa.repl(schoolSpl[0].split("~")[1].trim().replace('.', '-').concat("-01"))+"\";"); //������¥
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_state\"; s:"+aa.repl(schoolSpl[1]).getBytes().length+":\""+aa.repl(schoolSpl[1])+"\";");
//				schoolQuery = schoolQuery.concat("}");
//				i++;
//			}
//		} catch (Exception e) { System.err.println("�з¿���"); e.printStackTrace();}
		
		schoolQuery = schoolQuery.concat("}");
		schoolQuery = "a:"+(index-degreeCount)+":{"+schoolQuery;
//		System.out.println(schoolQuery.trim().replaceAll(" ", ""));
		return schoolQuery.trim().replaceAll(" ", "");
	} //main
} //class
