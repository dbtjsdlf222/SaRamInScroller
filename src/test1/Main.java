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
		
		System.out.print(myData.select(".myname>em").text()); //�̸�
		if(myData.select(".myname>em").text().contains("�ۡ�")) {
			System.out.println("������̷¼�");
			System.out.println(url);
//			System.err.println("���� ��ȿ���� ����");
//			System.exit(999);
			return;
		}
		Elements elements = getHtml.select("#resume_print_area");
		int partIndex = 0;
		career = "0";
		while(true) {
			try {
				if(elements.select(".wrap_letterview>.section_part").get(partIndex).select("h3.title").text().contains("�ڰ���")) {
					License license = new License();
					qualifications = license.getLicense(elements.select(".wrap_letterview>.section_part").get(partIndex).select("tbody"));
					if(qualifications.length() < 1) { qualifications = "a:0:{}"; }
					
				} else if (elements.select(".wrap_letterview>.section_part").get(partIndex).select("h3.title").text().contains("�����")) {
					try {
						career = elements.select(".wrap_letterview>.section_part").get(partIndex).select("span>em").get(0).text(); //��� ù�ڸ� ������
						try { career = career+ "." +elements.select(".wrap_letterview>.section_part").get(partIndex).select("span>em").get(1).text(); } //�ι�° �ڸ��� ������ ����
						catch (Exception e) {}
						
					CareerClass career = new CareerClass();
					careeries = career.getCareer(elements.select(".wrap_letterview>.section_part").get(partIndex).select("tbody"));
					if(careeries.length() < 1) { careeries = "a:0:{}"; }
					
				} catch (Exception e) { e.printStackTrace(); }
					
				} else if (elements.select(".wrap_letterview>.section_part").get(partIndex).select("h3.title").text().contains("�з�")) {
					schoolClass schoolClass = new schoolClass();
					scholarships = schoolClass.getSchool(elements.select(".wrap_letterview>.section_part").get(partIndex).select("tbody"), url, elements.select(".wrap_letterview>.section_part").get(partIndex).select("h3.title .bar"));
					if(scholarships.length() < 1) { scholarships = "a:0:{}"; } 
				}
				partIndex++;
			}
			catch (IndexOutOfBoundsException e) { break; }
			catch (Exception e) { System.out.println("section_part �ݺ��� ���� �߻�"); }
		} // while
		
		/*!-----�з� �ڰ��� ���-----*/
		
		String userInsert = "INSERT INTO `rankup_member`(uid, kind, passwd, name, open, valid, wdate) VALUES('"+ranUid+"', 'genral', 'passwd', '"+myData.select(".myname>em").text()+"', 'yes', 'no', '"+setCalendars()+"');";
		
//		String query = "INSERT INTO `rankup_work_resume_data`"+ 
//					   "(uid,`hide_photo`,`state`,`subject`,`work_1st`,`work_2nd`,`area_1st`,`area_2nd`,`worktypes`,`pay`,`scholarship`,`scholarships`,`career`,`careeries`,`qualifications`,`linguistics`,`introduce`,`open`,`default`,`write_date`,`modify_date`,`keyword`,`more_careers`)"+	
//					   "VALUES ("+
//					   ranUid+", "+ hide_photo+", "+ state+", "+ subject+", "+ work_1st+", "+ work_2nd+", "+ area_1st+", "+ area_2nd+", "+ worktypes+", "+ pay+", "+ scholarship+", "+ scholarships+", "+ career+", "+ careeries+", "+ qualifications+", "+ linguistics+", "+ introduce+", "+ open+", "+ defaultVar+", "+ write_date+", "+ modify_date+", "+ keyword+", "+ more_careers
//					   +")";
//		
		String query_extend = "INSERT INTO `rankup_member_extend`"+ 
				"(`uid`,`birthday`,`sex`,`phone`,`hphone`,`zipcode`,`address1`,`address2`,`mailing`,`sms`,`secession`, `visit`, `no`)";	
		
//		SELECT list_name FROM `rankup_category` WHERE code="work_type" and list_name like "%���%"
//		SELECT list_name FROM `rankup_category` WHERE code="area_1st" like "%����Ư����%"
		
		//����
		String[] areaList = getHtml.select(".talent_type").select("tbody").select("td").toString().split("</td>")[3].replaceAll("<td headers=\"mycondi4\">", "").split("��")[0].split(",")[0].split("&gt;");
		
		try {
			if(areaList[1].contains(","))
				areaList[1] = areaList[1].split(",")[0].trim();	
		} catch (Exception e) {
			areaList[0] = "����" ;
			areaList[0] = "������ü" ;
		}
		area_1st = areaList[0].trim();
		area_2nd = areaList[1].trim().contains("��ü")?"": (area_html.select("#_body_>center>div>table>tbody>tr>td>table").text().contains(areaList[1])?areaList[1] : "\'\'");
		area_1st = "IFNULL((SELECT CAST(no AS CHAR) as 'area_1st' FROM rankup_category WHERE parent_no = 0 AND code='area_type' AND list_name LIKE '%"+area_1st+"%' LIMIT 1),'1')";
		area_2nd.trim();
		if(!area_2nd.isEmpty()) {
			area_2nd = "IFNULL((SELECT CAST(no AS CHAR) as 'area_2nd' FROM rankup_category WHERE parent_no != 0 AND code='area_type' AND list_name LIKE '%"+area_2nd+"%' LIMIT 1),'')";
		}		 
		if(area_2nd.length() < 1)
			area_2nd="''";
		
//		System.out.println("-----------------����----------------");
//		Elements areaElements = area_html.select("#resume_print_area");
		
		//����
		String work_1st_like="";
		String[] jobList = getHtml.select(".talent_type").select("tbody").select("td").toString().split("</td>")[0].replaceAll("<td headers=\"mycondi1\">", "").replaceAll(",", "&gt;").split("��");
		for(int j=0;j<jobList.length;j++) {
			if(j>3) { break; }
			if(jobList[j].contains("&gt;")) {
				work_1st_like = work_1st_like.concat(" or list_name like '%"+jobList[j].split("&gt;")[0].trim()+"%'");
				work_1st_like = work_1st_like.concat(" or list_name like '%"+jobList[j].split("&gt;")[1].trim()+"%'");
			} else {
				work_1st_like = work_1st_like.concat(" or list_name like '%"+jobList[j].trim()+"%'");
			}
		}
		
		work_1st = "(SELECT CAST(parent_no AS CHAR) as `work_1st` FROM `rankup_category` WHERE parent_no != 0 AND code='work_type' AND (list_name like '%ggg%'"+work_1st_like+") limit 1)"; //���ø��� or�� ������ ����
		work_2nd = "(SELECT CAST(no AS CHAR) as `work_2nd` FROM `rankup_category` WHERE parent_no != 0 AND code='work_type' AND (list_name like '%hhh%' "+work_1st_like+") limit 1)";
		
		//�̷¼� �Է� ������
		String ranDate = setCalendars();
		write_date = ranDate;
		modify_date = ranDate;
		open = "yes";
		hide_photo = "yes";
		defaultVar = "yes";
		state = ((myData.select(".birthday>span").text().substring(14,17).contains("����"))?"ready":"hold");
		
		//�̷¼� ����
		if(getHtml.select(".my_letter_view .intit").html().replace('[', ' ').replace(']', ' ').trim().length() < 1 || getHtml.select(".my_letter_view .intit").html().replace('[', ' ').replace(']', ' ').trim().equals(".")) {
			subject = myData.select(".myname>em").text()+" �̷¼�";
		} else { subject = myData.select(".myname>em").text(); }
		
		name = myData.select(".myname>em").text(); // ������ ��� �̸�
		
//		try { System.out.println("2.1:"+myData.select(".birthday>span").text().substring(0, 4)); } catch (Exception e) { System.err.println("���Ͽ���"); } //����
//		try { System.out.println("2.2:"+myData.select(".birthday>span").text().substring(7,9));} catch (Exception e) { System.err.println("���̿���"); } //����
//		try { System.out.println("2.3:"+myData.select(".birthday>span").text().substring(12,13)); } catch (Exception e) { System.err.println("��������"); } //����
//		try { System.out.println("2.4:"+myData.select(".birthday>span").text().substring(14,17)); } catch (Exception e) { System.err.println("�������ο���"); } //��������
//		System.out.println("3:"+new String(Base64.getDecoder().decode(myData.select(".mail>._res_enc_info").text()))); //����
//		
//		String tel = new String(Base64.getDecoder().decode(myData.select(".phone>._res_enc_info").text()));
//		try {
//			System.out.println("4.1:"+new String(tel.substring(tel.length()-17, tel.length()-4)).split("-")[0]); //��ȭ��ȣ1
//			System.out.println("4.2:"+new String(tel.substring(tel.length()-17, tel.length()-4)).split("-")[1]); //��ȭ��ȣ2
//			System.out.println("4.3:"+new String(tel.substring(tel.length()-17, tel.length()-4)).split("-")[2]); //��ȭ��ȣ3
//		} catch (Exception e) { System.err.println("��ȭ��ȣ ����"); }
//		
		String dashBoard = getHtml.select(".dashboard>ul").text();
//		System.out.println(dashBoard.substring(dashBoard.indexOf("�з»���") + 5, dashBoard.indexOf("��»���")).split(" ")[0]); //�����з�
//		System.out.println(dashBoard.substring(dashBoard.indexOf("�з»���") + 5, dashBoard.indexOf("��»���")).split(" ")[1]); //��������
		
		String career1 = dashBoard.substring(dashBoard.indexOf("��»���") + 5, dashBoard.indexOf("�������/�ٹ�����")); //��� ���
		
		String finalSchool = dashBoard.substring(dashBoard.indexOf("�з»���") + 5, dashBoard.indexOf("��»���")).split(" ")[1];
		
		switch(dashBoard.substring(dashBoard.indexOf("�з»���") + 5, dashBoard.indexOf("��»���")).split(" ")[0]){
			case "�ʵ��б�": scholarship = "1"; break;
			case "���б�": scholarship = "2"; break;
			case "����б�": scholarship = "3"; break; 
			case "���б�(2,3��)":
				switch(finalSchool) {
					case "����/����":
					case "����": scholarship = "5"; break; 
					case "������":
					case "����":
					case "������":
					case "��������": scholarship = "4"; break;
					case "����": scholarship = "10"; break;
					default: scholarship ="4";
				}
				break;
			case "���б�(4��)":
				switch(finalSchool) {
					case "����":
					case "����/����":
					case "����": scholarship = "7"; break;
					case "������":
					case "����":
					case "������":
					case "��������": scholarship = "6"; break;
					case "����": scholarship = "11"; break;
					default: scholarship ="7";	
				}
			break;
			case "���п�(����)":
				switch(finalSchool) {
					case "����":
					case "����/����":
					case "����": scholarship = "8"; break;
					case "������":
					case "����":
					case "������":
					case "��������": scholarship = "12"; break;
					default: scholarship ="8";
				}
				break;
			case "���п�(�ڻ�)": 
				switch(finalSchool) {
					case "����":
					case "����/����":
					case "����": scholarship = "9"; break;
					case "������":
					case "����":
					case "������":
					case "��������": scholarship = "13"; break;
					default: scholarship ="9";
				}
				break;
		}
		
		if(career1.contains("���")) {
//			System.out.println("���"); //���
//			System.out.println(getHtml.select(".dashboard>ul>li").get(1).select(".txt").text().replaceAll("���", "").replaceAll("����", "").replaceAll("��",".").trim());
//			System.out.println(career1.split(" ")[1].substring(0, career1.split(" ")[1].length()-1)+'.'+career1.split(" ")[2].substring(0, career1.split(" ")[2].length()-2)); //��
//				career = career1.split(" ")[1].substring(0, career1.split(" ")[1].length()-1)+'.'+career1.split(" ")[2].substring(0, career1.split(" ")[2].length()-2);
		} else {
//			career="0"; //�����ϰ��
//			System.out.println("����");
		}
//		System.out.println(dashBoard.substring(dashBoard.indexOf("�������/�ٹ�����") + 10, dashBoard.indexOf("����ٹ���")).split("/")[0]); //�������
		
//		try {
//			System.out.println(getHtml.select(".dashboard>ul>li").get(2).select(".txt").text().toString().split("/")[1]);
//		} catch (Exception e) {
//			System.err.println("�ٹ����� ����");
//		}
		
		try {
//			pay = dashBoard.substring(dashBoard.indexOf("�������/�ٹ�����") + 10, dashBoard.indexOf("����ٹ���")).split("/")[0];
			pay = getHtml.select(".dashboard>ul>li").get(2).select(".txt").text().split("/")[0];
			worktypes = getHtml.select(".dashboard>ul>li").get(2).select(".txt").html().split("/")[1].split("<br>")[0].trim();
		} catch (Exception e) {
			System.err.println("�������");
		}
		
//		System.out.println(getHtml.select(".inpart_view>tbody").toString().split("</tbody>"));
//		String[] list = getHtml.select(".inpart_view>tbody").toString()
				//.replaceAll("<td class=\"lineup_center\" rowspan=\"1\">", "").replaceAll("<td>", "#").replaceAll("</td>", "").replaceAll("<tr>", "").replaceAll("<td class=\"lineup_center\">", "").replaceAll("</tr>", "").replaceAll("<tbody>", "")
//				.trim().split("</tbody>");
		
//		System.out.println(list.length+"----------------�з�--------------------");
		
//		try {
//			String[] schoolList = list[0].split("</tr>");
//			list[list.length-1] = null;
//			
//			int i = 0;
//			int schoolIndex=0;
//			String schoolQuery = "a:"+(schoolList.length-1)+":{";
//			
//			for(String school : schoolList) {
//				if(i>schoolList.length-2) { break; } //for�� �ѹ� ������ ���� ����
//				String[] schoolSpl = school.split("</td>"); //�ϳ��� �з� ������ ����
				
//				schoolQuery = schoolQuery.concat("i:"+schoolIndex+";");
//				schoolQuery = schoolQuery.concat("a:6:{");
//				schoolQuery = schoolQuery.concat("s:11:\"scholarship\";");
//				schoolQuery = schoolQuery.concat("s:");
//				if(repl(schoolSpl[2]).contains("�ʵ�")) {				schoolQuery = schoolQuery.concat("8:\"�ʵ��б�\"");	
//				}else if(repl(schoolSpl[2]).contains("���б�")){		schoolQuery = schoolQuery.concat("6:\"���б�\"");
//				}else if(repl(schoolSpl[2]).contains("����б�")){   	schoolQuery = schoolQuery.concat("8:\"����б�\"");
//				}else if(repl(schoolSpl[2]).contains("������")){ schoolQuery = schoolQuery.concat("13:\"���б�(2~3��)\"");
//				}else if(repl(schoolSpl[2]).contains("���б�(����)")){  schoolQuery = schoolQuery.concat("12:\"���б�(����)\"");
//				}else if(repl(schoolSpl[2]).contains("���б�(�ڻ�)")){  schoolQuery = schoolQuery.concat("12:\"���б�(�ڻ�)\""); }
//				else { schoolQuery = schoolQuery.concat(dashBoard.substring(dashBoard.indexOf("�з»���") + 5, dashBoard.indexOf("��»���")).split(" ")[0].getBytes().length+":"+dashBoard.substring(dashBoard.indexOf("�з»���") + 5, dashBoard.indexOf("��»���")).split(" ")[0]+"\""); }
//				schoolQuery = schoolQuery.concat(";");
				
//				schoolQuery = schoolQuery.concat("s:6:\"school\"; s:"+repl(schoolSpl[2]).getBytes().length+":\""+repl(schoolSpl[2])+"\";"); //�б���
//				schoolQuery = schoolQuery.concat("s:6:\"lesson\"; s:"+repl(schoolSpl[3]).getBytes().length+":\""+repl(schoolSpl[3])+"\";"); //�а�
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_sdate\"; s:"+repl(schoolSpl[0].split("~")[0].trim().replace('.', '-').concat("-01")).getBytes().length+":\""+repl(schoolSpl[0].split("~")[0].trim().replace('.', '-')).concat("-01")+"\";"); //���г�¥
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_edate\"; s:"+repl(schoolSpl[0].split("~")[1].trim().replace('.', '-').concat("-01")).getBytes().length+":\""+repl(schoolSpl[0].split("~")[1].trim().replace('.', '-').concat("-01"))+"\";"); //������¥
//				schoolQuery = schoolQuery.concat("s:12:\"lesson_state\"; s:"+repl(schoolSpl[1]).getBytes().length+":\""+repl(schoolSpl[1])+"\";");
//				schoolQuery = schoolQuery.concat("}}");
//				System.out.print(repl(schoolSpl[0].split("~")[0].trim().replace('.', '-').concat("-01 00:00:00"))+" "); //���г⵵
//				System.out.println(repl(schoolSpl[0].split("~")[1].trim().replace('.', '-').concat("-01 00:00:00"))); //�����⵵
//				System.out.print(repl(schoolSpl[2]+" ")); //���и�
//				System.out.print(repl(schoolSpl[3]+" ")); //�а�
//				System.out.println(repl(schoolSpl[1]+" ")); //��������
//				i++;
//			}
//		} catch (Exception e) { System.err.println("�з¿���"); e.printStackTrace(); }
		
		
//		System.out.println(list.length+"---------------���---------------");
//		if(career!="0") {
//			int z = 0;
//			try {
//				String[] careers = list[1].split("</p> </td>");
//				for(String careersStr : careers) {
//					if(z > careers.length-2) { break; } //for�� �ѹ� ������ ���� ����
//					String[] careersSpl = careersStr.split("</td>");
//					System.out.println(repl(careersSpl[0]).split("~")[0].trim().concat("-01 00:00:00").replace('.', '-')); //�ٹ��Ⱓ
//					System.out.println(repl(careersSpl[0]).split("~")[1].trim().substring(0, repl(careersSpl[0])
//							.split("~")[1].trim().indexOf('(')).concat("-01 00:00:00").replace('.', '-')); //�ٹ��Ⱓ
//					System.out.println(repl(careersSpl[1])); //ȸ���
//		//				System.out.println(repl(careersSpl[2])); //����
//		//				System.out.println(repl(careersSpl[3])); //����
//					System.out.println(repl(careersSpl[4])); //����
//					System.out.println(repl(careersSpl[5].split("</p>")[0].substring(0, careersSpl[5].indexOf("<br>"))).replaceAll("������", "")); //������
//					
//					z++;
//				}
//			} catch (Exception e) { System.err.println("��¿���"); }
//		}
		
//		System.out.println(list.length+"----------------�ڰ���--------------------");
//		
//		try {
//			String[] careerList = list[2].split("</tr>");
//			int y = 0;
//			for(String careerStr:careerList) {
//				if(y>careerList.length-2) { break; } //for�� �ѹ� ������ ���� ����
//				String[] careerTemp = careerStr.split("</td>");
//				System.out.println(repl(careerTemp[0])); //��泯¥
//				System.out.println(repl(careerTemp[2])); //�ڰ�����
//				System.out.println(repl(careerTemp[3])); //�߱�ó
//				y++;
//				System.out.println("---------------------");
//			}
//		} catch (Exception e) { System.err.println("�ڰ��� ����"); }
//		
//		System.out.println("-----------------�ڼҼ�---------------------");
		if(getHtml.select(".my_letter_view .intit").html().replace('[', ' ').replace(']', ' ').trim().length() < 1||(getHtml.select(".my_letter_view .intit").html().replace('[', ' ').replace(']', ' ').trim()+"").equals(".")) {
			subject = myData.select(".myname>em").text()+" �̷¼�";
		} else {
			subject = getHtml.select(".my_letter_view .intit").html().replace('[', ' ').replace(']', ' ').trim();
		}
		try { introduce = getHtml.select(".my_letter_view").html().substring(getHtml.select(".my_letter_view").html().indexOf("<div class=\"intxt\">")); } catch (Exception e) { introduce=""; } //�ڼҼ� ����
		
//		System.out.println("-----------------��±����-----------------");
		try {
			more_careers = getHtml.select(".my_letter_view").html().split("<p class=\"intit\">")[0];
		} catch (Exception e) { System.err.println("��±���� ����"); }
		
//		try {
//			System.out.println(getHtml.select(".talent_type").select("tbody").select("td").toString().split("</td>")[0]);
//		} catch (Exception e) { System.err.println("�𸣰����� ����"); }
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
			birthday = getHtml.select(".birthday>span").get(0).text().split("��")[0]+ranBirthday;
			
			try { sex = getHtml.select(".birthday>span").get(1).text().equals("��") ? "male":"female";}
			catch (Exception e) { e.printStackTrace(); System.err.println("��������"); }
			
			try {
				phone = new String(Base64.getDecoder().decode(getHtml.select(".phone>._res_enc_info").text())).replaceAll("<a resume-action=\"sendSMS\" data-track_event=\"\">", "").replaceAll("</a>","");
				if(phone.length()<1) {
					phone = "�����";
				} //if
			}catch (Exception e) { phone = "�����"; e.printStackTrace(); System.err.println("�ڵ��� ����"); }
			
			try { 
				hphone = new String(Base64.getDecoder().decode(getHtml.select(".tel>span").text()));
				if(hphone.length()<1) {
					hphone = "�����";
				} //if
			}
			catch (Exception e) {  System.err.println("�ڵ��� ����"); }
			
			try { zipcode = getHtml.select(".address").text().split(" ")[0].replace('(', ' ').replace(')', ' ').trim(); }
			catch (Exception e) { System.err.println("zipcode ����"); }
			
			try { 
				address1 = getHtml.select(".address").toString().substring(getHtml.select(".address").text().toString().indexOf(" "), getHtml.select(".address").toString().length()-6);
				if(address1.length()<=1) {
					address1 = "�����";
				} //if
			} catch (Exception e) { address1="�����"; System.err.println("�ּ�1�� ����"); }
			
			try { email = new String(Base64.getDecoder().decode(getHtml.select(".mail span").text())); }
			catch (Exception e) { System.err.println("���� ����"); }
			
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
			System.out.print(":�Ϸ�\n");	
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
//				 bufferedWriter.write("����"+": "+getHtml.select(".area_title>h3").get(0).text().replaceAll("\'", "\""));
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
			System.out.print(":����\n");
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