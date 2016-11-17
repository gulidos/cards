package ru.rik.cardsnew.domain;

import java.io.UnsupportedEncodingException;
//http://stackoverflow.com/questions/23102775/ussd-received-message-decoding
//http://javatalks.ru/topics/26268?page=1#130631
	
public class UssdTest {
	//+CUSD: 0,"0421043F0430044104380431043E0020043704300020043E04310440043004490435043D0438043500210020041C044B0020043D0430043F0440043004320438043C0020043E04420432043504420020043D04300020041204300448002004370430043F0440043E04410020043200200053004D0053",72
static String resp = "0421043F0430044104380431043E0020043704300020043E04310440043004490435043D0438043500210020041C044B0020043D0430043F0440043004320438043C0020043E04420432043504420020043D04300020041204300448002004370430043F0440043E04410020043200200053004D0053";
	public UssdTest() {	}
	
	public static String ucs2ToUTF8(byte[] ucs2Bytes) throws UnsupportedEncodingException{  
		  
	    String unicode = new String(ucs2Bytes, "UTF-16");  
	     
	    String utf8 = new String(unicode.getBytes("UTF-8"), "Cp1252");  
	     
	    return unicode;  
	}  
	
	

	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		Gsm7BitEncoderDecoder d = new Gsm7BitEncoderDecoder();
		String s1 = d.decode(resp);
		System.out.println(s1);
		System.out.println("======");
		String message = new String(s1.getBytes(),"US-ASCII");
		System.out.println(message);
		
		
	}

}
