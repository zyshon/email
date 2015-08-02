package com.sunzy.email;

public class MailHost {
	public enum SMTP{
		TENCENT_QQ("smtp.qq.com","腾讯QQ邮箱"),
		TENCENT_EXMAIL("smtp.exmail.qq.com","腾讯企业邮箱"),
		NTES_126("smtp.126.com","网易126邮箱"),
		NTES_163("smtp.163.com","网易163邮箱"),
		NTES_VIP_163("smtp.vip.163.com","网易VIP163邮箱"),
		NTES_188("smtp.188.com","网易188财富邮"),
		NTES_YEAH("smtp.yeah.net","网易yeah.net邮箱"),
		NTES_NETEASE("smtp.netease.com","网易netease.com邮箱");
		private String host;
		private String name;
		private SMTP(String host,String name) {
			this.host=host;
			this.name=name;
		}
		public String getHost(){
			return this.host;
		}
	}
}
