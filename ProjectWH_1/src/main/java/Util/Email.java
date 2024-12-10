package Util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class Email {
	//pass: worq umoj zget cpro
	//email: thienthantfb@gmail.com
	
	public static void sendEmail(String toEmail, String message) {

		final String fromEmail = "trangtfboys0811@gmail.com";
		final String pass ="psqn ozew aaza qiwj";
		//create authenticator
		Authenticator authenticator = new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication(fromEmail, pass);
			}
		};
		
		Properties pr = createProp(new Properties());
		//phiên làm việc 
		Session session = Session.getInstance(pr,authenticator);
		//gửi mail
		//Tạo một tin nhắn mới
		MimeMessage msg = new MimeMessage(session);
		try {
			System.out.println("haha");
			msg.addHeader("Content-type", "text/HTML; chartset= UTF-8");
			//người nhận
			msg.setFrom();
			//người gửi
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail,false));
			//Tiêu đề email

			msg.setSubject("ANNOUNCE","UTF-8");
			//nội dung
			msg.setText(message, "UTF-8");

			//gửi mail
			Transport.send(msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static Properties createProp(Properties prop) {
		Properties pr = prop;
		pr.put("mail.smtp.host", "smtp.gmail.com");//SMTP HOST
		pr.put("mail.smtp.port", "587");//STLS 587 SSL 465
		pr.put("mail.smtp.auth", "true");//thực hiện việc đăng nhập
		pr.put("mail.smtp.ssl.protocols", "TLSv1.2");
		pr.put("mail.smtp.starttls.enable", "true");
		return pr;
	}
//	public static void main(String[] args) {
//
//		sendEmail("trungg16122003@gmail.com", "haha");
//	}
		
	

}
