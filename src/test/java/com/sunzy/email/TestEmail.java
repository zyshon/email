package com.sunzy.email;

import java.io.File;
import java.net.MalformedURLException;

import org.junit.Test;

public class TestEmail {
	@Test
	public void sendText(){
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
