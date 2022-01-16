package test1;

import java.util.Random;

public class Linguistics {
	
	public void setLinguistics(String getDate, String examination, String language, String point, int index) throws Exception {
		Random ranNum = new Random();
		String level = "중";

		if(ranNum.nextInt(100)%2 == 1) 
			level = "상";
				
				Main.linguistics = Main.linguistics +
				"i:"+index+";"
					+ "a:5:{"
					+ "s:8:\"language\";"
					+ "s:"+Main.repl(language).getBytes().length+":\""+Main.repl(language)+"\";"
					+ "s:5:\"level\";"
					+ "s:"+Main.repl(level).getBytes().length+":\""+Main.repl(level)+"\";"
					+ "s:11:\"examination\";"
					+ "s:"+Main.repl(examination).getBytes().length+":\""+Main.repl(examination)+"\";"
					+ "s:5:\"point\";"
					+ "s:"+Main.repl(point).getBytes().length+":\""+Main.repl(point)+"\";"
					+ "s:11:\"obtain_date\";"
					+ "s:"+Main.repl(getDate).replace('.', '-').concat("-01").getBytes().length+":\""+Main.repl(getDate).replace('.', '-').concat("-01")+"\";"
					+ "}";
		
//		aa.linguistics.concat(
//		"a:1:{"
//		"i:\""+""+"\";"+
//		"a:5:{"
//			s:8:"language";
//			s:4:"영어";
//			s:5:"level";
//			s:2:"중";
//			s:11:"examination";
//			s:4:"토익";
//			s:5:"point";
//			s:3:"500";
//			s:11:"obtain_date";
//			s:10:"2008-11-06";
//		"}"
//			)
	} //main
} //class
