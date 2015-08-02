package com.sunzy.email.bean;
import java.io.File;
import java.net.URL;


public class FilePair {
	private File file;//附件资源
	private String name;//附件名
	private URL url;
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
}
