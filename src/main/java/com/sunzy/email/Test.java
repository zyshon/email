package com.sunzy.email;

import java.io.File;
import java.net.MalformedURLException;

public class Test {
	public static void main(String[] args) throws MalformedURLException{   
        //这个类主要来发送邮件   
     //SimpleMailSender sms = new SimpleMailSender();   
         //sms.sendTextMail(mailInfo);//发送文体格式    
       // sms.sendHtmlMail(mailInfo);//发送html格式
     MailSender cn = new MailSender(MailHost.SMTP.TENCENT_EXMAIL, "yuncai@huo.so", "yunhuo2014");
		//设置邮件标题
		cn.setTitle("快看美女");
		cn.setContent("32323");
		// 添加收件人
		cn.addRecipient("510191478@qq.com");
		cn.addAffix(new File("/Users/sunzhenyu/Downloads/流米流量分发平台-产品介绍-2015.pdf"));
		cn.sendText();
   }  
}
