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
				String lesson_state = schoolEl.select("tr").get(index).select("td").get(1).text(); //졸업여부
				String school = schoolEl.select("tr").get(index).select("td").get(2).text().replaceAll(" ", ""); //학교명
				String lesson = schoolEl.select("tr").get(index).select("td").get(3).text().replaceAll(" ", ""); //학과
				
				schoolQuery = schoolQuery.concat("i:"+(index-degreeCount)+";");
				schoolQuery = schoolQuery.concat("a:6:{");
				schoolQuery = schoolQuery.concat("s:11:\"scholarship\";");
				schoolQuery = schoolQuery.concat("s:");
				if(schoolStr.contains("고등학교")) {
					schoolQuery = schoolQuery.concat("8:\"고등학교\";");
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
//				if(i>schoolList.length-2) { break; } //for문 한번 더도는 오류 방지
//				String[] schoolSpl = school.split("</td>"); //하나의 학력 낱개로 분할
//				
//				String schoolStr = carrer.toString().split("</em>")[0].replaceAll("<span class=\"bar\">", "").replaceAll("<em>", "(")+")";
//				
//				//<span class="bar"><em>고등학교</em>졸업</span>
//				
//			System.out.println(schoolStr.getBytes().length+":\""+schoolStr+"\"");
//				schoolQuery = schoolQuery.concat("i:"+schoolIndex+";");
//				schoolQuery = schoolQuery.concat("a:6:{");
//				schoolQuery = schoolQuery.concat("s:11:\"scholarship\";");
//				schoolQuery = schoolQuery.concat("s:");
//				if(schoolStr.contains("고등학교")) {
//					schoolQuery = schoolQuery.concat("8:\"고등학교\"");
//				} else {
//					schoolQuery = schoolQuery.concat(schoolStr.getBytes().length+":\""+schoolStr+"\";");
////					schoolQuery = schoolQuery.concat(schoolStr+"("+carrer.select("em").text()+")".getBytes().length);
////					schoolQuery = schoolQuery.concat(":\""+carrer.select(".bar").text()+"("+carrer.select("em").text()+")\"");
//				}
//				schoolQuery = schoolQuery.concat(";");
//				
////				if(aa.repl(schoolSpl[2]).contains("초등")) {				schoolQuery = schoolQuery.concat("8:\"초등학교\"");
////				} else if(aa.repl(schoolSpl[2]).contains("중학교")){		schoolQuery = schoolQuery.concat("6:\"중학교\"");
////				} else if(aa.repl(schoolSpl[2]).contains("고등학교")){   	schoolQuery = schoolQuery.concat("8:\"고등학교\"");
////				} else if(aa.repl(schoolSpl[2]).contains("전문")){ 			schoolQuery = schoolQuery.concat("13:\"대학교(2~3년)\"");
////				} else if(aa.repl(schoolSpl[2]).contains("대학교(석사)")){  schoolQuery = schoolQuery.concat("12:\"대학교(석사)\"");
////				} else if(aa.repl(schoolSpl[2]).contains("대학교(박사)")){  schoolQuery = schoolQuery.concat("12:\"대학교(박사)\""); }
////				else { schoolQuery = schoolQuery.concat(dashBoard.substring(dashBoard.indexOf("학력사항") + 5, dashBoard.indexOf("경력사항")).split(" ")[0].getBytes().length+":"+dashBoard.substring(dashBoard.indexOf("학력사항") + 5, dashBoard.indexOf("경력사항")).split(" ")[0]+"\""); }
////				schoolQuery = schoolQuery.concat("s:6:\"school\");s:학교명.lenght:학교명;");
////				schoolEl.select("tr").get(0).text()
//
//				
//				schoolQuery = schoolQuery.concat("s:6:\"school\"; s:"+aa.repl(schoolSpl[2]).getBytes().length+":\""+aa.repl(schoolSpl[2])+"\";"); //학교명
//				schoolQuery = schoolQuery.concat("s:6:\"lesson\"; s:"+aa.repl(schoolSpl[3]).getBytes().length+":\""+aa.repl(schoolSpl[3])+"\";"); //학과
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_sdate\"; s:"+aa.repl(schoolSpl[0].split("~")[0].trim().replace('.', '-').concat("-01")).getBytes().length+":\""+aa.repl(schoolSpl[0].split("~")[0].trim().replace('.', '-')).concat("-01")+"\";"); //입학날짜
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_edate\"; s:"+aa.repl(schoolSpl[0].split("~")[1].trim().replace('.', '-').concat("-01")).getBytes().length+":\""+aa.repl(schoolSpl[0].split("~")[1].trim().replace('.', '-').concat("-01"))+"\";"); //졸업날짜
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_state\"; s:"+aa.repl(schoolSpl[1]).getBytes().length+":\""+aa.repl(schoolSpl[1])+"\";");
//				schoolQuery = schoolQuery.concat("}");
//				i++;
//			}
//		} catch (Exception e) { System.err.println("학력오류"); e.printStackTrace();}
		
		schoolQuery = schoolQuery.concat("}");
		schoolQuery = "a:"+(index-degreeCount)+":{"+schoolQuery;
//		System.out.println(schoolQuery.trim().replaceAll(" ", ""));
		return schoolQuery.trim().replaceAll(" ", "");
	} //main
} //class
