package test1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Main {

	public static String name,hide_photo,state,subject,work_1st,work_2nd,work_3rd,area_1st,area_2nd,worktypes,pay,scholarship,scholarships,career="",careeries="",qualifications="",linguistics="",introduce,open,defaultVar,write_date,modify_date,keyword,more_careers;
	
	public static void main(String[] args) throws Exception {
		
		try {
//			 BufferedReader bufReader = new BufferedReader(new FileReader(new File("D:\\Desktop\\urlList.txt")));
////			 BufferedReader bufReader = new BufferedReader(new FileReader(new File("D:\\Desktop\\queryErrList.txt")));
			 try { FileWriter fw = new FileWriter("D:\\Desktop\\query.txt"); fw.write(""); fw.close(); } 
			 catch (IOException e) { e.printStackTrace(); }
			 
			File input = new File("D:/Desktop/1.html");
			Document doc = Jsoup.parse(input, "UTF-8");
			Elements list = doc.select("#resumeList");
			
			int i = 0;
			String line = "";
			
			while (true) {
				try {
					if(list.select("tr").get(i).select("a").attr("data-res_idx").equals("")) { i++; continue; }
					line = "http://www.saramin.co.kr/zf_user/mandb/view?res_idx="+list.select("tr").get(i).select("a").attr("data-res_idx");
					new Main().operator(line);
					i++;
				} catch (Exception e) { break; }
			} //while
			 
//			 while((line = bufReader.readLine()) != null) {
//				 if(line.length() < 1) continue;
//				 new Main().operator(line);
//			}
 			 System.out.println("-END-");
//			 bufReader.close();
			} 
			catch (FileNotFoundException e) { e.printStackTrace(); } 
			catch(IOException e){ e.printStackTrace(); }
	} //main
	
	public void operator(String url) throws Exception {
		StringBuffer ranUid = new StringBuffer();
		Random rnd = new Random();

		try {
		for (int i = 0; i < 20; i++) {
		    int rIndex = rnd.nextInt(3);
		    switch (rIndex) {
			    case 0: ranUid.append((char) ((int) (rnd.nextInt(26)) + 97)); break;
			    case 1: ranUid.append((char) ((int) (rnd.nextInt(26)) + 65)); break;
			    case 2: ranUid.append((rnd.nextInt(10))); break;
		    }
		}
		
		ConnectClass connect = new ConnectClass();
		Document getHtml = connect.getHtmlString(url);
		Document area_html = Jsoup.connect("http://sbjob.co.kr/work/employ_index.html?mode=area").get();
		Elements myData = getHtml.select(".my_data");
		
		System.out.print(myData.select(".myname>em").text()); //이름
		if(myData.select(".myname>em").text().contains("○○")) {
			System.out.println("비공개이력서");
			System.out.println(url);
//			System.err.println("세션 유효하지 않음");
//			System.exit(999);
			return;
		}
		Elements elements = getHtml.select("#resume_print_area");
		int partIndex = 0;
		career = "0";
		while(true) {
			try {
				if(elements.select(".wrap_letterview>.section_part").get(partIndex).select("h3.title").text().contains("자격증")) {
					License license = new License();
					qualifications = license.getLicense(elements.select(".wrap_letterview>.section_part").get(partIndex).select("tbody"));
					if(qualifications.length() < 1) { qualifications = "a:0:{}"; }
					
				} else if (elements.select(".wrap_letterview>.section_part").get(partIndex).select("h3.title").text().contains("경력총")) {
					try {
						career = elements.select(".wrap_letterview>.section_part").get(partIndex).select("span>em").get(0).text(); //경력 첫자리 가져옴
						try { career = career+ "." +elements.select(".wrap_letterview>.section_part").get(partIndex).select("span>em").get(1).text(); } //두번째 자리가 있으면 붙힘
						catch (Exception e) {}
						
					CareerClass career = new CareerClass();
					careeries = career.getCareer(elements.select(".wrap_letterview>.section_part").get(partIndex).select("tbody"));
					if(careeries.length() < 1) { careeries = "a:0:{}"; }
					
				} catch (Exception e) { e.printStackTrace(); }
					
				} else if (elements.select(".wrap_letterview>.section_part").get(partIndex).select("h3.title").text().contains("학력")) {
					schoolClass schoolClass = new schoolClass();
					scholarships = schoolClass.getSchool(elements.select(".wrap_letterview>.section_part").get(partIndex).select("tbody"), url, elements.select(".wrap_letterview>.section_part").get(partIndex).select("h3.title .bar"));
					if(scholarships.length() < 1) { scholarships = "a:0:{}"; } 
				}
				partIndex++;
			}
			catch (IndexOutOfBoundsException e) { break; }
			catch (Exception e) { System.out.println("section_part 반복문 오류 발생"); }
		} // while
		
		/*!-----학력 자격증 경력-----*/
		
		String userInsert = "INSERT INTO `rankup_member`(uid, kind, passwd, name, open, valid, wdate) VALUES('"+ranUid+"', 'genral', 'passwd', '"+myData.select(".myname>em").text()+"', 'yes', 'no', '"+setCalendars()+"');";
		
//		String query = "INSERT INTO `rankup_work_resume_data`"+ 
//					   "(uid,`hide_photo`,`state`,`subject`,`work_1st`,`work_2nd`,`area_1st`,`area_2nd`,`worktypes`,`pay`,`scholarship`,`scholarships`,`career`,`careeries`,`qualifications`,`linguistics`,`introduce`,`open`,`default`,`write_date`,`modify_date`,`keyword`,`more_careers`)"+	
//					   "VALUES ("+
//					   ranUid+", "+ hide_photo+", "+ state+", "+ subject+", "+ work_1st+", "+ work_2nd+", "+ area_1st+", "+ area_2nd+", "+ worktypes+", "+ pay+", "+ scholarship+", "+ scholarships+", "+ career+", "+ careeries+", "+ qualifications+", "+ linguistics+", "+ introduce+", "+ open+", "+ defaultVar+", "+ write_date+", "+ modify_date+", "+ keyword+", "+ more_careers
//					   +")";
//		
		String query_extend = "INSERT INTO `rankup_member_extend`"+ 
				"(`uid`,`birthday`,`sex`,`phone`,`hphone`,`zipcode`,`address1`,`address2`,`mailing`,`sms`,`secession`, `visit`, `no`)";	
		
//		SELECT list_name FROM `rankup_category` WHERE code="work_type" and list_name like "%운송%"
//		SELECT list_name FROM `rankup_category` WHERE code="area_1st" like "%서울특별시%"
		
		//지역
		String[] areaList = getHtml.select(".talent_type").select("tbody").select("td").toString().split("</td>")[3].replaceAll("<td headers=\"mycondi4\">", "").split("·")[0].split(",")[0].split("&gt;");
		
		try {
			if(areaList[1].contains(","))
				areaList[1] = areaList[1].split(",")[0].trim();	
		} catch (Exception e) {
			areaList[0] = "서울" ;
			areaList[0] = "서울전체" ;
		}
		area_1st = areaList[0].trim();
		area_2nd = areaList[1].trim().contains("전체")?"": (area_html.select("#_body_>center>div>table>tbody>tr>td>table").text().contains(areaList[1])?areaList[1] : "\'\'");
		area_1st = "IFNULL((SELECT CAST(no AS CHAR) as 'area_1st' FROM rankup_category WHERE parent_no = 0 AND code='area_type' AND list_name LIKE '%"+area_1st+"%' LIMIT 1),'1')";
		area_2nd.trim();
		if(!area_2nd.isEmpty()) {
			area_2nd = "IFNULL((SELECT CAST(no AS CHAR) as 'area_2nd' FROM rankup_category WHERE parent_no != 0 AND code='area_type' AND list_name LIKE '%"+area_2nd+"%' LIMIT 1),'')";
		}		 
		if(area_2nd.length() < 1)
			area_2nd="''";
		
//		System.out.println("-----------------직업----------------");
//		Elements areaElements = area_html.select("#resume_print_area");
		
		//직업
		String work_1st_like="";
		String[] jobList = getHtml.select(".talent_type").select("tbody").select("td").toString().split("</td>")[0].replaceAll("<td headers=\"mycondi1\">", "").replaceAll(",", "&gt;").split("·");
		for(int j=0;j<jobList.length;j++) {
			if(j>3) { break; }
			if(jobList[j].contains("&gt;")) {
				work_1st_like = work_1st_like.concat(" or list_name like '%"+jobList[j].split("&gt;")[0].trim()+"%'");
				work_1st_like = work_1st_like.concat(" or list_name like '%"+jobList[j].split("&gt;")[1].trim()+"%'");
			} else {
				work_1st_like = work_1st_like.concat(" or list_name like '%"+jobList[j].trim()+"%'");
			}
		}
		
		work_1st = "(SELECT CAST(parent_no AS CHAR) as `work_1st` FROM `rankup_category` WHERE parent_no != 0 AND code='work_type' AND (list_name like '%ggg%'"+work_1st_like+") limit 1)"; //스플릿한 or문 붙혀서 실행
		work_2nd = "(SELECT CAST(no AS CHAR) as `work_2nd` FROM `rankup_category` WHERE parent_no != 0 AND code='work_type' AND (list_name like '%hhh%' "+work_1st_like+") limit 1)";
		
		//이력서 입력 변수들
		String ranDate = setCalendars();
		write_date = ranDate;
		modify_date = ranDate;
		open = "yes";
		hide_photo = "yes";
		defaultVar = "yes";
		state = ((myData.select(".birthday>span").text().substring(14,17).contains("구직"))?"ready":"hold");
		
		//이력서 제목
		if(getHtml.select(".my_letter_view .intit").html().replace('[', ' ').replace(']', ' ').trim().length() < 1 || getHtml.select(".my_letter_view .intit").html().replace('[', ' ').replace(']', ' ').trim().equals(".")) {
			subject = myData.select(".myname>em").text()+" 이력서";
		} else { subject = myData.select(".myname>em").text(); }
		
		name = myData.select(".myname>em").text(); // 오류시 출력 이름
		
//		try { System.out.println("2.1:"+myData.select(".birthday>span").text().substring(0, 4)); } catch (Exception e) { System.err.println("생일오류"); } //생일
//		try { System.out.println("2.2:"+myData.select(".birthday>span").text().substring(7,9));} catch (Exception e) { System.err.println("나이오류"); } //나이
//		try { System.out.println("2.3:"+myData.select(".birthday>span").text().substring(12,13)); } catch (Exception e) { System.err.println("성별오류"); } //성별
//		try { System.out.println("2.4:"+myData.select(".birthday>span").text().substring(14,17)); } catch (Exception e) { System.err.println("구직여부오류"); } //구직여부
//		System.out.println("3:"+new String(Base64.getDecoder().decode(myData.select(".mail>._res_enc_info").text()))); //메일
//		
//		String tel = new String(Base64.getDecoder().decode(myData.select(".phone>._res_enc_info").text()));
//		try {
//			System.out.println("4.1:"+new String(tel.substring(tel.length()-17, tel.length()-4)).split("-")[0]); //전화번호1
//			System.out.println("4.2:"+new String(tel.substring(tel.length()-17, tel.length()-4)).split("-")[1]); //전화번호2
//			System.out.println("4.3:"+new String(tel.substring(tel.length()-17, tel.length()-4)).split("-")[2]); //전화번호3
//		} catch (Exception e) { System.err.println("전화번호 오류"); }
//		
		String dashBoard = getHtml.select(".dashboard>ul").text();
//		System.out.println(dashBoard.substring(dashBoard.indexOf("학력사항") + 5, dashBoard.indexOf("경력사항")).split(" ")[0]); //최종학력
//		System.out.println(dashBoard.substring(dashBoard.indexOf("학력사항") + 5, dashBoard.indexOf("경력사항")).split(" ")[1]); //졸업여부
		
		String career1 = dashBoard.substring(dashBoard.indexOf("경력사항") + 5, dashBoard.indexOf("희망연봉/근무형태")); //경력 년월
		
		String finalSchool = dashBoard.substring(dashBoard.indexOf("학력사항") + 5, dashBoard.indexOf("경력사항")).split(" ")[1];
		
		switch(dashBoard.substring(dashBoard.indexOf("학력사항") + 5, dashBoard.indexOf("경력사항")).split(" ")[0]){
			case "초등학교": scholarship = "1"; break;
			case "중학교": scholarship = "2"; break;
			case "고등학교": scholarship = "3"; break; 
			case "대학교(2,3년)":
				switch(finalSchool) {
					case "편입/졸업":
					case "졸업": scholarship = "5"; break; 
					case "재학중":
					case "휴학":
					case "휴학중":
					case "졸업예정": scholarship = "4"; break;
					case "퇴학": scholarship = "10"; break;
					default: scholarship ="4";
				}
				break;
			case "대학교(4년)":
				switch(finalSchool) {
					case "수료":
					case "편입/졸업":
					case "졸업": scholarship = "7"; break;
					case "재학중":
					case "휴학":
					case "휴학중":
					case "졸업예정": scholarship = "6"; break;
					case "퇴학": scholarship = "11"; break;
					default: scholarship ="7";	
				}
			break;
			case "대학원(석사)":
				switch(finalSchool) {
					case "수료":
					case "편입/졸업":
					case "졸업": scholarship = "8"; break;
					case "재학중":
					case "휴학":
					case "휴학중":
					case "졸업예정": scholarship = "12"; break;
					default: scholarship ="8";
				}
				break;
			case "대학원(박사)": 
				switch(finalSchool) {
					case "수료":
					case "편입/졸업":
					case "졸업": scholarship = "9"; break;
					case "재학중":
					case "휴학":
					case "휴학중":
					case "졸업예정": scholarship = "13"; break;
					default: scholarship ="9";
				}
				break;
		}
		
		if(career1.contains("경력")) {
//			System.out.println("경력"); //경력
//			System.out.println(getHtml.select(".dashboard>ul>li").get(1).select(".txt").text().replaceAll("경력", "").replaceAll("개월", "").replaceAll("년",".").trim());
//			System.out.println(career1.split(" ")[1].substring(0, career1.split(" ")[1].length()-1)+'.'+career1.split(" ")[2].substring(0, career1.split(" ")[2].length()-2)); //년
//				career = career1.split(" ")[1].substring(0, career1.split(" ")[1].length()-1)+'.'+career1.split(" ")[2].substring(0, career1.split(" ")[2].length()-2);
		} else {
//			career="0"; //신입일경우
//			System.out.println("신입");
		}
//		System.out.println(dashBoard.substring(dashBoard.indexOf("희망연봉/근무형태") + 10, dashBoard.indexOf("희망근무지")).split("/")[0]); //희망연봉
		
//		try {
//			System.out.println(getHtml.select(".dashboard>ul>li").get(2).select(".txt").text().toString().split("/")[1]);
//		} catch (Exception e) {
//			System.err.println("근무형태 오류");
//		}
		
		try {
//			pay = dashBoard.substring(dashBoard.indexOf("희망연봉/근무형태") + 10, dashBoard.indexOf("희망근무지")).split("/")[0];
			pay = getHtml.select(".dashboard>ul>li").get(2).select(".txt").text().split("/")[0];
			worktypes = getHtml.select(".dashboard>ul>li").get(2).select(".txt").html().split("/")[1].split("<br>")[0].trim();
		} catch (Exception e) {
			System.err.println("희망연봉");
		}
		
//		System.out.println(getHtml.select(".inpart_view>tbody").toString().split("</tbody>"));
//		String[] list = getHtml.select(".inpart_view>tbody").toString()
				//.replaceAll("<td class=\"lineup_center\" rowspan=\"1\">", "").replaceAll("<td>", "#").replaceAll("</td>", "").replaceAll("<tr>", "").replaceAll("<td class=\"lineup_center\">", "").replaceAll("</tr>", "").replaceAll("<tbody>", "")
//				.trim().split("</tbody>");
		
//		System.out.println(list.length+"----------------학력--------------------");
		
//		try {
//			String[] schoolList = list[0].split("</tr>");
//			list[list.length-1] = null;
//			
//			int i = 0;
//			int schoolIndex=0;
//			String schoolQuery = "a:"+(schoolList.length-1)+":{";
//			
//			for(String school : schoolList) {
//				if(i>schoolList.length-2) { break; } //for문 한번 더도는 오류 방지
//				String[] schoolSpl = school.split("</td>"); //하나의 학력 낱개로 분할
				
//				schoolQuery = schoolQuery.concat("i:"+schoolIndex+";");
//				schoolQuery = schoolQuery.concat("a:6:{");
//				schoolQuery = schoolQuery.concat("s:11:\"scholarship\";");
//				schoolQuery = schoolQuery.concat("s:");
//				if(repl(schoolSpl[2]).contains("초등")) {				schoolQuery = schoolQuery.concat("8:\"초등학교\"");	
//				}else if(repl(schoolSpl[2]).contains("중학교")){		schoolQuery = schoolQuery.concat("6:\"중학교\"");
//				}else if(repl(schoolSpl[2]).contains("고등학교")){   	schoolQuery = schoolQuery.concat("8:\"고등학교\"");
//				}else if(repl(schoolSpl[2]).contains("전문대")){ schoolQuery = schoolQuery.concat("13:\"대학교(2~3년)\"");
//				}else if(repl(schoolSpl[2]).contains("대학교(석사)")){  schoolQuery = schoolQuery.concat("12:\"대학교(석사)\"");
//				}else if(repl(schoolSpl[2]).contains("대학교(박사)")){  schoolQuery = schoolQuery.concat("12:\"대학교(박사)\""); }
//				else { schoolQuery = schoolQuery.concat(dashBoard.substring(dashBoard.indexOf("학력사항") + 5, dashBoard.indexOf("경력사항")).split(" ")[0].getBytes().length+":"+dashBoard.substring(dashBoard.indexOf("학력사항") + 5, dashBoard.indexOf("경력사항")).split(" ")[0]+"\""); }
//				schoolQuery = schoolQuery.concat(";");
				
//				schoolQuery = schoolQuery.concat("s:6:\"school\"; s:"+repl(schoolSpl[2]).getBytes().length+":\""+repl(schoolSpl[2])+"\";"); //학교명
//				schoolQuery = schoolQuery.concat("s:6:\"lesson\"; s:"+repl(schoolSpl[3]).getBytes().length+":\""+repl(schoolSpl[3])+"\";"); //학과
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_sdate\"; s:"+repl(schoolSpl[0].split("~")[0].trim().replace('.', '-').concat("-01")).getBytes().length+":\""+repl(schoolSpl[0].split("~")[0].trim().replace('.', '-')).concat("-01")+"\";"); //입학날짜
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_edate\"; s:"+repl(schoolSpl[0].split("~")[1].trim().replace('.', '-').concat("-01")).getBytes().length+":\""+repl(schoolSpl[0].split("~")[1].trim().replace('.', '-').concat("-01"))+"\";"); //졸업날짜
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_state\"; s:"+repl(schoolSpl[1]).getBytes().length+":\""+repl(schoolSpl[1])+"\";");
//				schoolQuery = schoolQuery.concat("}}");
//				System.out.print(repl(schoolSpl[0].split("~")[0].trim().replace('.', '-').concat("-01 00:00:00"))+" "); //입학년도
//				System.out.println(repl(schoolSpl[0].split("~")[1].trim().replace('.', '-').concat("-01 00:00:00"))); //졸업년도
//				System.out.print(repl(schoolSpl[2]+" ")); //대학명
//				System.out.print(repl(schoolSpl[3]+" ")); //학과
//				System.out.println(repl(schoolSpl[1]+" ")); //졸업여부
//				i++;
//			}
//		} catch (Exception e) { System.err.println("학력오류"); e.printStackTrace(); }
		
		
//		System.out.println(list.length+"---------------경력---------------");
//		if(career!="0") {
//			int z = 0;
//			try {
//				String[] careers = list[1].split("</p> </td>");
//				for(String careersStr : careers) {
//					if(z > careers.length-2) { break; } //for문 한번 더도는 오류 방지
//					String[] careersSpl = careersStr.split("</td>");
//					System.out.println(repl(careersSpl[0]).split("~")[0].trim().concat("-01 00:00:00").replace('.', '-')); //근무기간
//					System.out.println(repl(careersSpl[0]).split("~")[1].trim().substring(0, repl(careersSpl[0])
//							.split("~")[1].trim().indexOf('(')).concat("-01 00:00:00").replace('.', '-')); //근무기간
//					System.out.println(repl(careersSpl[1])); //회사명
//		//				System.out.println(repl(careersSpl[2])); //직급
//		//				System.out.println(repl(careersSpl[3])); //지역
//					System.out.println(repl(careersSpl[4])); //연봉
//					System.out.println(repl(careersSpl[5].split("</p>")[0].substring(0, careersSpl[5].indexOf("<br>"))).replaceAll("담당업무", "")); //담당업무
//					
//					z++;
//				}
//			} catch (Exception e) { System.err.println("경력오류"); }
//		}
		
//		System.out.println(list.length+"----------------자격증--------------------");
//		
//		try {
//			String[] careerList = list[2].split("</tr>");
//			int y = 0;
//			for(String careerStr:careerList) {
//				if(y>careerList.length-2) { break; } //for문 한번 더도는 오류 방지
//				String[] careerTemp = careerStr.split("</td>");
//				System.out.println(repl(careerTemp[0])); //취득날짜
//				System.out.println(repl(careerTemp[2])); //자격증명
//				System.out.println(repl(careerTemp[3])); //발급처
//				y++;
//				System.out.println("---------------------");
//			}
//		} catch (Exception e) { System.err.println("자격증 오류"); }
//		
//		System.out.println("-----------------자소서---------------------");
		if(getHtml.select(".my_letter_view .intit").html().replace('[', ' ').replace(']', ' ').trim().length() < 1||(getHtml.select(".my_letter_view .intit").html().replace('[', ' ').replace(']', ' ').trim()+"").equals(".")) {
			subject = myData.select(".myname>em").text()+" 이력서";
		} else {
			subject = getHtml.select(".my_letter_view .intit").html().replace('[', ' ').replace(']', ' ').trim();
		}
		try { introduce = getHtml.select(".my_letter_view").html().substring(getHtml.select(".my_letter_view").html().indexOf("<div class=\"intxt\">")); } catch (Exception e) { introduce=""; } //자소서 내용
		
//		System.out.println("-----------------경력기술서-----------------");
		try {
			more_careers = getHtml.select(".my_letter_view").html().split("<p class=\"intit\">")[0];
		} catch (Exception e) { System.err.println("경력기술서 오류"); }
		
//		try {
//			System.out.println(getHtml.select(".talent_type").select("tbody").select("td").toString().split("</td>")[0]);
//		} catch (Exception e) { System.err.println("모르겠지만 오류"); }
		open = "yes";
		hide_photo="yes";
		defaultVar="yes";
//		query = "INSERT INTO `rankup_work_resume_data`"+ 
//				   "(`uid`,`hide_photo`,`state`,`subject`,`work_1st`,`work_2nd`,`area_1st`,`area_2nd`,`worktypes`,`pay`,`scholarship`,`scholarships`,`career`,`careeries`,`qualifications`,`linguistics`,`introduce`,`open`,`default`,`write_date`,`modify_date`,`keyword`,`more_careers`)"+	
//				   "VALUES (\""+
//				   ranUid+"\",\""+hide_photo+"\",\""+ state+"\",\""+ subject+"\","+ work_1st +","+ work_2nd +","+ area_1st +","+ area_2nd +",\""+ worktypes +"\",\""+ pay +"\",\""+ scholarship+"\",\""+ scholarships+"\","+ career+",\""+ careeries+"\",\""+ qualifications+"\",\""+ linguistics+"\",\""+ introduce+"\",\""+ open+"\",\""+ defaultVar+"\",\""+ write_date+"\",\""+ modify_date+"\",\""+ keyword+"\",\""+ more_careers+"\")";
//		System.out.println(query.replaceAll("\"", "`"));
		
		//CAST(parent_no AS CHAR CHARACTER SET utf8)
		
		int hitnum = 0;
		
		hitnum = new Random().nextInt(200);
		
		if(linguistics.length()<1||linguistics.equals("0}")) linguistics = "a:0:{}";
		
	String query2 = "insert into `rankup_work_resume_data`"+
	        "(`uid`,"+
	        "`hide_photo`,"+
	         "`state`,"+
	         "`subject`,"+
	         "`work_1st`,"+
	         "`work_2nd`,"+
	         "`area_1st`,"+
	         "`area_2nd`,".trim()+
	         "`worktypes`,"+
	         "`pay`,"+
	         "`scholarship`,"+
	         "`scholarships`,"+
	         "`career`,"+
	         "`careeries`,"+
	         "`qualifications`,"+
	         "`linguistics`,"+
	         "`introduce`,"+
	         "`open`,"+
	         "`default`,"+
	         "`write_date`,"+
	         "`modify_date`,"+
	         "`hit_num`,"+
	         "`admin`,"+
	         "`more_careers`)"+
	         "values('"+ranUid+"',"+
	          "'"+hide_photo+"',"+
	          "'"+state+"',"+
	          "'"+getHtml.select(".area_title>h3").get(0).text().replaceAll("\'", "\"")+"',"+
	          	  work_1st+","+
	          	  work_2nd+","+
	              area_1st+","+
          		  area_2nd+","+
	          "'"+worktypes+"',"+
	          "'"+pay+"',"+
	          "'"+scholarship+"',"+
	          "'"+scholarships+"',"+
	          	  career+","+
	 		  "'"+careeries.toString().replaceAll("\'", "")+"',"+
	          "'"+qualifications+"',"+
	          "'"+linguistics +"',"+
	          "'"+introduce.toString().replaceAll("\'", "\"").replace('\'', '\"')+"',"+
	          "'"+open+"',"+
	          "'"+defaultVar+"',"+
	          "'"+write_date+"',"+
	          "'"+modify_date+"',"+
	          	hitnum+
	          ",`no`,"+
	          "'"+more_careers.toString().replaceAll("\'", "\"").replace('\'', '\"')+"');";
	
	String uid = "\'\'",birthday = "\'\'",sex= "\'\'",phone= "\'\'",hphone= "\'\'",zipcode = "\'\'",address1 = "\'\'",address2= "\'\'",mailing= "\'\'",sms= "\'\'",secession= "\'\'", visit= "\'\'", email= "\'\'";
			
			uid = ranUid+"";
			String ranBirthday = setCalendars();
			ranBirthday = setCalendars().substring(4, ranBirthday.length());
			birthday = getHtml.select(".birthday>span").get(0).text().split("년")[0]+ranBirthday;
			
			try { sex = getHtml.select(".birthday>span").get(1).text().equals("남") ? "male":"female";}
			catch (Exception e) { e.printStackTrace(); System.err.println("성별오류"); }
			
			try {
				phone = new String(Base64.getDecoder().decode(getHtml.select(".phone>._res_enc_info").text())).replaceAll("<a resume-action=\"sendSMS\" data-track_event=\"\">", "").replaceAll("</a>","");
				if(phone.length()<1) {
					phone = "비공개";
				} //if
			}catch (Exception e) { phone = "비공개"; e.printStackTrace(); System.err.println("핸드폰 오류"); }
			
			try { 
				hphone = new String(Base64.getDecoder().decode(getHtml.select(".tel>span").text()));
				if(hphone.length()<1) {
					hphone = "비공개";
				} //if
			}
			catch (Exception e) {  System.err.println("핸드폰 없음"); }
			
			try { zipcode = getHtml.select(".address").text().split(" ")[0].replace('(', ' ').replace(')', ' ').trim(); }
			catch (Exception e) { System.err.println("zipcode 오류"); }
			
			try { 
				address1 = getHtml.select(".address").toString().substring(getHtml.select(".address").text().toString().indexOf(" "), getHtml.select(".address").toString().length()-6);
				if(address1.length()<=1) {
					address1 = "비공개";
				} //if
			} catch (Exception e) { address1="비공개"; System.err.println("주소1번 오류"); }
			
			try { email = new String(Base64.getDecoder().decode(getHtml.select(".mail span").text())); }
			catch (Exception e) { System.err.println("메일 없음"); }
			
			address2 = "";
			mailing = "no";
			sms = "no";
			secession = "no";
			visit = hitnum + "";
			
	query_extend = "INSERT INTO `rankup_member_extend`"+ 
			"(`uid`,"
			+ "`birthday`,"
			+ "`sex`,"
			+ "`phone`,"
			+ "`hphone`,"
			+ "`zipcode`,"
			+ "`address1`,"
			+ "`address2`,"
			+ "`mailing`,"
			+ "`sms`,"
			+ "`secession`,"
			+ "`email`,"
			+ "`visit`)"	
			+ "VALUES ("
			+"'"+uid+"',"
			+"'"+birthday+"',"
			+"'"+sex+"',"
			+"'"+phone+"',"
			+"'"+hphone+"',"
			+"'"+zipcode+"',"
			+"'"+address1.toString().replaceAll("ss=\"address\">", "").replaceAll("=\"address\">", "")+"',"			
			+"'"+address2+"',"
			+"'"+mailing+"',"
			+"'"+sms+"',"
			+"'"+secession+"',"
			+"'"+email+"',"
			+"'"+visit+"');";
//		System.out.println(userInsert);
//		System.out.println(query_extend);
//		System.out.println(query2);

		try{
			System.out.print(":완료\n");	
			File file = new File("D:\\Desktop\\query.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			PrintWriter pw = new PrintWriter(bw,true);
			pw.write(userInsert+"\n"+query_extend+"\n"+query2+"\n");
			pw.write("\n");
			pw.flush();
			pw.close(); 
		} catch (Exception e) { e.printStackTrace();}
//			 if(file.isFile() && file.canWrite()){
//				 bufferedWriter.write(userInsert);
//				 bufferedWriter.newLine();
//				 bufferedWriter.write(query_extend);
//				 bufferedWriter.newLine();
//				 bufferedWriter.write(query2);
//				 bufferedWriter.newLine();
//				 bufferedWriter.write("UID: "+uid);
//				 bufferedWriter.write("제목"+": "+getHtml.select(".area_title>h3").get(0).text().replaceAll("\'", "\""));
//				 bufferedWriter.close();
//			 }
		 } catch (Exception e) {
			File file = new File("D:\\Desktop\\queryErrList.txt");
//			File file = new File("D:\\Desktop\\urlList.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			PrintWriter pw = new PrintWriter(bw, true);
			pw.write(url+"\n");
			pw.flush();
			pw.close();
			System.out.print(":오류\n");
			e.printStackTrace();
		 }
	} // main
	
	public static String setCalendars() {
		int[] maxDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        
        int iMinMonth = 1;
        int iMaxMonth = 8;

        int iRandomYear = (int)(Math.random() * 3)+2016;
        int iRandomMonth = (int)(Math.random() * iMaxMonth - iMinMonth + 1) + iMinMonth;
        int iRandomDay = (int)(Math.random() * (maxDays[iRandomMonth-1] -2) + 1);
        
        String strMonth = "" + iRandomMonth;
        String strDay = "" + iRandomDay;
        
        if(iRandomMonth <= 9) { strMonth = "0" + iRandomMonth; }
        if(iRandomDay <= 9) { strDay = "0" + iRandomDay; }
        return iRandomYear+"-"+strMonth+"-"+strDay+" 00:00:00";
	}
    
	public static String repl(String a) {
		return a.replaceAll("<td class=\"lineup_center\" rowspan=\"1\">", "")
				.replaceAll("<td class=\"lineup_center\" rowspan=\"2\">", "")
				.replaceAll("<td>", "").replaceAll("</td>", "").replaceAll("<tr>", "")
				.replaceAll("<td class=\"lineup_center\">", "").replaceAll("</tr>", "")
				.replaceAll("<tbody>", "").replaceAll("<br>", "").replaceAll("<td colspan=\"4\">","")
				.replaceAll("<p class=\"box_point\"><span class=\"ico_point\">", "")
				.replaceAll("</span>", "").replaceAll("&gt;", "")
				.replaceAll("&nbsp;", "").replaceAll("</p>", "")
				.replaceAll("&amp;", "")
				.trim();
	}
	public static String[] repl(String[] a) {
		String[] stringList = null;
		int i=0;
		for(String temp : a) {  
			a[i] = temp.replaceAll("<td class=\"lineup_center\" rowspan=\"1\">", "").replaceAll("<td>", "").replaceAll("</td>", "").replaceAll("<tr>", "").replaceAll("<td class=\"lineup_center\">", "").replaceAll("</tr>", "").replaceAll("<tbody>", "")
			.trim();
			i++;
		}
		return stringList;
	}
}