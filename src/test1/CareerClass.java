package test1;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.select.Elements;
public class CareerClass {
	
	public String getCareer(Elements el) {
		String query = "";
		if(Main.career!="0") {
			int z = 0;
//			try {
					  
				String[] careers = el.toString().split("</p> </td>");
				for(String careersStr : careers) {
					if(z > careers.length-2) { break; } //for문 한번 더도는 오류 방지
					String[] careersSpl = careersStr.split("</td>");
//					System.out.println(careersSpl[5]);	
//					aa.repl(careersSpl[0]).split("~")[0].trim().concat("-01").replace('.', '-'); //근무기간
//					aa.repl(careersSpl[0]).split("~")[1].trim().substring(0, aa.repl(careersSpl[0])
//							.split("~")[1].trim().indexOf('(')).concat("-01").replace('.', '-'); //근무기간
//					aa.repl(careersSpl[1]); //회사명
//		//				aa.repl(careersSpl[2])); //직급
//		//				aa.repl(careersSpl[3])); //지역
//					aa.repl(careersSpl[4]); //연봉
					
					try {
						if(careersSpl[5].split("</p>")[0].substring(0, careersSpl[5].indexOf("<br>")).contains("담당업무")) {
							Main.repl(careersSpl[5].split("</p>")[0].substring(0, careersSpl[5].indexOf("<br>"))).replaceAll("담당업무", ""); //담당업무 많을경우 엔터 기준 첫줄
						}else {
							
						}
					} catch (Exception e) {
						try {
//							System.out.println(el.select(".box_point").toString().split("</span>")[1].split("</p>")[0]);
						} catch (Exception e2) {
//							System.out.println(el.select(".box_point").toString().split("</span>")[1]);
						}
					}
					query = query+
						"i:"+z+";"+
						"a:5:{"+
							"s:7:\"company\";"+
							"s:"+Main.repl(careersSpl[1]).getBytes().length+":\""+Main.repl(careersSpl[1])+"\";"+
							"s:8:\"business\";";
					try {
						if(Main.repl(careersSpl[5].split("</p>")[0].substring(0, careersSpl[5].indexOf("<br>"))).contains("담당업무")) {
							query = query + "s:"+Main.repl(careersSpl[5].split("</p>")[0].substring(0, careersSpl[5].indexOf("<br>"))).replaceAll("담당업무", "").replaceAll("\'", "").replaceAll("\"", "").getBytes().length+":\""+Main.repl(careersSpl[5].split("</p>")[0].substring(0, careersSpl[5].indexOf("<br>"))).replaceAll("담당업무", "").replaceAll("\'", "").replaceAll("\"", "")+"\";";
						} else {
							query = query + "s:"+0+":\"\")+\";";
						}
					} catch (Exception e) {
						query = query + "s:"+el.select(".box_point").toString().split("</span>")[1].split("</p>")[0].replaceAll("'", "\"").getBytes().length+":\""+el.select(".box_point").toString().split("</span>")[1].split("</p>")[0].replaceAll("'", "\"").replaceAll("</p>", "")+"\";";
					}
					try {
						query = query.concat(
							"s:10:\"hold_sdate\";"+
							"s:"+Main.repl(careersSpl[0]).split("~")[0].trim().concat("-01 00:00:00").replace('.', '-').getBytes().length+":\""+Main.repl(careersSpl[0]).split("~")[0].trim().concat("-01 00:00:00").replace('.', '-')+"\";"+
							"s:10:\"hold_edate\";");
					} catch (Exception e) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String now = sdf.format(cal.getTime());
						query = query + "s:10:\"hold_sdate\":"+
									    "s:"+now.getBytes().length+":\""+now+"\";"+
									    "s:10:\"hold_edate\":";
					}
					try {
						query = query+
							"s:"+Main.repl(careersSpl[0]).split("~")[1].trim().substring(0, Main.repl(careersSpl[0])
								.split("~")[1].trim().indexOf('(')).concat("-01").replace('.', '-').getBytes().length+":\""+Main.repl(careersSpl[0]).split("~")[1].trim().substring(0, Main.repl(careersSpl[0])
								.split("~")[1].trim().indexOf('(')).concat("-01").replace('.', '-')+"\";";
					} catch (Exception e) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String now = sdf.format(cal.getTime());
						query = query +
								"s:"+now.getBytes().length+":\""+now+"\";";
					}
					query = query.concat(
							"s:8:\"hold_pay\";"+
							"s:"+Main.repl(careersSpl[4]).getBytes().length+":\""+Main.repl(careersSpl[4])+"\";"+"}");
					z++;
				}
				int careersCount = careers.length;
				
				query = "a:"+(careersCount<1? careersCount:careersCount-1)+":{"+query;
				query = query+"}";
			// } catch (Exception e) { System.err.println(e); }
		}
//		System.out.println(query);
		return query;
	}
}
