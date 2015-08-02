package com.sunzy.email;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sunzy.email.bean.FilePair;
/**
 * @author 孙振宇(510191478)
 * @version 1.0
 *
 */
public class MailSender {
	private String host = ""; // smtp服务器
	private List<String> recipientList = new ArrayList<String>(); // 收件人地址
	private List<FilePair> fileList = null;
	private int port;
	private String account = ""; // 用户名
	private String password = ""; // 密码
	private String title = ""; // 邮件标题
	private String content;

	/**
	 * 创建邮件发送服务
	 * @param host      邮件smtp服务器
	 * @param account   邮件账号
	 * @param password  邮件密码
	 */
	 public MailSender(String host,String account,String password){
		 this.host=host;
		 this.account=account;
		 this.password=password;
	 }
	 /**
	  * 创建邮件发送服务
	  * @param smtp      邮件smtp服务器
	  * @param account   邮件账号
	  * @param password  邮件密码
	  */
	public MailSender(MailHost.SMTP smtp,String account,String password){
		 this.host=smtp.getHost();
		 this.account=account;
		 this.password=password;
	}

	 public MailSender(){}
	/**
	 * 设置邮件服务器地址 smtp
	 * @param host  smtp服务器
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * 设置发件邮箱账号
	 * @param account 发件邮箱账号
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * 设置发件邮箱密码
	 * @param password 发件邮箱密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 设置邮件服务端口
	 * @param port 邮件服务端口
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * 设置邮件内容
	 * @param content 邮件内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 设置邮件标题
	 * @param title 邮件标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 添加邮件接收人
	 * @param recipient 邮件接收人
	 */
	public void addRecipient(String recipient) {
		recipientList.add(recipient);
	}

	/**
	 * 添加附件(附件重命名)
	 * @param file 附件
	 * @param name 附件重命名
	 */
	public void addAffix(File file, String name) {
		if (fileList == null)
			fileList = new ArrayList<FilePair>();
		FilePair filePair = new FilePair();
		filePair.setFile(file);
		filePair.setName(name);
		fileList.add(filePair);
	}
	/**
	 * 添加附件(附件重命名)
	 * @param file 附件
	 * @param name 附件重命名
	 */
	public void addAffix(URL url, String name) {
		if (fileList == null)
			fileList = new ArrayList<FilePair>();
		FilePair filePair = new FilePair();
		filePair.setUrl(url);
		filePair.setName(name);
		fileList.add(filePair);
	}
	/**
	 * 添加附件
	 * @param file 本地资源
	 */
	public void addAffix(File file) {
		if (fileList == null)
			fileList = new ArrayList<FilePair>();
		FilePair filePair = new FilePair();
		filePair.setFile(file);
		filePair.setName(file.getName());
		fileList.add(filePair);
	}
	/**
	 * 添加附件
	 * @param url 网络资源URL
	 */
	public void addAffix(URL url) {
		if (fileList == null)
			fileList = new ArrayList<FilePair>();
		FilePair filePair = new FilePair();
		filePair.setUrl(url);
		filePair.setName(url.getFile());
		fileList.add(filePair);
	}
	/**
	 * 以文本格式发送邮件
	 * @return boolean true发送成功 false发送失败
	 */
	public boolean sendText() {
		// 判断是否需要身份认证
		// 如果需要身份认证，则创建一个密码验证器
		MailAuthenticator authenticator = new MailAuthenticator(account,password);
		Properties pros = new Properties();    
		pros.put("mail.smtp.host", host);   
	    if(port!=0)
	    	pros.put("mail.smtp.port", port);    
	    pros.put("mail.smtp.auth", true);     
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap
				.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pros, authenticator);
		try {
			InternetAddress[] recipients = new InternetAddress[recipientList
					.size()];
			for (int i = 0; i < recipientList.size(); i++) {
				recipients[i] = new InternetAddress(recipientList.get(i));
			}
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(account);
			// 加载收件人地址
			mailMessage.addRecipients(Message.RecipientType.TO,recipients);
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 设置邮件消息的主题
			mailMessage.setSubject(title);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();
			// 设置邮件的文本内容
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setText(content);
			multipart.addBodyPart(contentPart);
			// 添加附件
			if (fileList!=null) {
				for (FilePair filePair : fileList) {
					BodyPart messageBodyPart = new MimeBodyPart();
					String affixName = filePair.getName();
					// 添加附件的内容
					if(filePair.getFile()!=null)
						messageBodyPart.setDataHandler(new DataHandler(new FileDataSource(filePair.getFile())));
					else
						messageBodyPart.setDataHandler(new DataHandler(filePair.getUrl()));
					// 添加附件的标题
					// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
					messageBodyPart.setFileName("=?UTF8?B?"+ Base64.getEncoder().encodeToString(affixName.getBytes()) + "?=");
					multipart.addBodyPart(messageBodyPart);
				}
			}
			// 将multipart对象放到message中
			mailMessage.setContent(multipart);
//			// 设置邮件消息的主要内容
//			String mailContent = content;
//			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 以HTML格式发送邮件
	 * @return boolean true发送成功 false发送失败
	 */
	public  boolean sendHtml() {
		// 判断是否需要身份认证
		MailAuthenticator authenticator = new MailAuthenticator(account,password);
		Properties pros = new Properties();    
		pros.put("mail.smtp.host", host);   
	    if(port!=0)
	    	pros.put("mail.smtp.port", port);    
	    pros.put("mail.smtp.auth", true); 
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap
				.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pros, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(account);
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			InternetAddress[] recipients = new InternetAddress[recipientList.size()];
			for (int i = 0; i < recipientList.size(); i++) {
			    recipients[i] = new InternetAddress(recipientList.get(i));
			}
			// 加载收件人地址
				mailMessage.addRecipients(Message.RecipientType.TO,recipients);
			// 设置邮件消息的主题
			mailMessage.setSubject(title);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();
			// 设置邮件的文本内容
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(content, "text/html; charset=utf-8");
			multipart.addBodyPart(html);
			// 添加附件
				if (fileList!=null) {
					for (FilePair filePair : fileList) {
						BodyPart messageBodyPart = new MimeBodyPart();
						File affix = filePair.getFile();
						String affixName = filePair.getName();
						DataSource source = new FileDataSource(affix);
						// 添加附件的内容
						messageBodyPart.setDataHandler(new DataHandler(source));
						// 添加附件的标题
						// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
						messageBodyPart.setFileName("=?UTF8?B?"+ Base64.getEncoder().encodeToString(affixName.getBytes()) + "?=");
						multipart.addBodyPart(messageBodyPart);
					}
				}
			// 将multipart对象放到message中
			mailMessage.setContent(multipart);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}
}