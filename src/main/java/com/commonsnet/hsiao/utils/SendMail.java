package com.commonsnet.hsiao.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;

public class SendMail {
	private static InternetAddress[] sendGoalMails = new InternetAddress[1]; //¦¬¥ó¤H
	/**
	 * ¥Á°êÂà¦è¤¸
	 * ¤é´ÁÂà´«
	 * @param taiwanDateString
	 * @return
	 * @throws ParseException
	 */
	public static  Date taiwanDateToAD(String taiwanDateString)throws ParseException{
		String yyyy = String.valueOf((Integer.parseInt(taiwanDateString.substring(0, 3))+1911));
		String MM = taiwanDateString.substring(3, 5);
		String dd = taiwanDateString.substring(5, 7);
		
		return new SimpleDateFormat("yyyyMMdd").parse(yyyy+MM+dd);
	}
	
	/**
	 * ¥Á°êÂà¦è¤¸
	 * ¤é´Á®É¶¡Âà´« 
	 * @param taiwanDateString
	 * @return
	 * @throws ParseException
	 */
	public static Date taiwanDateToAD(String dateString, String timeString)throws ParseException{
		String yyyy = String.valueOf((Integer.parseInt(dateString.substring(0, 3))+1911));
		String MM = dateString.substring(3, 5);
		String dd = dateString.substring(5, 7);
		
		String adDateString = yyyy+"/"+MM+"/"+dd;
		
		String  HH = StringUtils.left(timeString, 2);
		String  mm = StringUtils.right(StringUtils.left(timeString, 4), 2) ;
		String ss  = StringUtils.right(timeString, 2);
		String adTimeString = HH+":"+mm+":"+ss;
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(adDateString +" "+ adTimeString);
	}
	
	/**
	 * ¤ÀªR¤¤¤å¥þ§Î¥b§Îªø«× 
	 * °Ñ¼Æ(¤ÀªR¦r¦ê¡B¶}©l¦ì¸m¡Bªø«×)
	 * @param parseString
	 * @param startNum
	 * @return
	 */
	public static  String parseStringLength(String parseString,int startPoint,int endPoint){
		
		int point = 0;
		
		StringBuffer composStr = new StringBuffer();
		
		char[] chars = parseString.toCharArray();
		
		for(char c: chars){
			
			point  = point + String.valueOf(c).getBytes().length;
			if(point> startPoint && point <= endPoint){
				composStr.append(String.valueOf(c));	
			}
		}
		return composStr.toString();
	}
	public static void sendGmail(String subject,String context){
		 final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	  Properties props = System.getProperties();
	  props.setProperty("mail.smtp.host", "smtp.gmail.com");
	  props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
	  props.setProperty("mail.smtp.socketFactory.fallback", "false");
	  props.setProperty("mail.smtp.port", "465");
	  props.setProperty("mail.smtp.socketFactory.port", "465");
	  props.put("mail.smtp.auth", "true");
	  final String username = Òsender_usernameÓ;
	  final String password = Òsender_passowrdÓ;
	  Session session = Session.getDefaultInstance(props, new Authenticator(){
	      protected PasswordAuthentication getPasswordAuthentication() {
	          return new PasswordAuthentication(username, password);
	      }});
	 
	  // -- Create a new message --
	  Message msg = new MimeMessage(session);
	  	 try {
	  		  sendGoalMails[0] = new InternetAddress("receive@gmail.com"); //¦¬¥ó¤H1
			  
	  		  msg.setFrom(new InternetAddress(username + "@gmail.com"));
	    	  msg.setRecipients(Message.RecipientType.TO,sendGoalMails);
	    	  msg.setSubject(subject);
	    	  msg.setText(context);
	    	  msg.setSentDate(new Date());
	    	  Transport.send(msg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/**
	 * ¨ú±o·í¤Ñ©Ò­n³B²zªºÀÉ®×¦WºÙ(«e¤@¤Ñ)
	 * @return
	 */
	public static String todayFileName(){
		String yyyy="";
		String mm ="";
		String dd ="";
		String simpleDateFormat = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()-(1000*60*60*24)));
		NumberFormat formatter = new DecimalFormat("000");
		yyyy = formatter.format(Integer.parseInt(simpleDateFormat.substring(0, 4))-1911);
		formatter = new DecimalFormat("00");
		mm = formatter.format(Integer.parseInt(simpleDateFormat.substring(4, 6)));
		dd = formatter.format(Integer.parseInt(simpleDateFormat.substring(6, 8)));
		return yyyy+mm+dd;
	}
}
