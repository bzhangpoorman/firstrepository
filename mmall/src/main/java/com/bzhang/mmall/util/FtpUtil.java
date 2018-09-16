package com.bzhang.mmall.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FtpUtil {
	private static Logger logger =LoggerFactory.getLogger(FtpUtil.class);
	
	private static String ftpIp=PropertiesUtil.getProperty("ftp.server.ip");
	private static String ftpUser=PropertiesUtil.getProperty("ftp.user");
	private static String ftpPass=PropertiesUtil.getProperty("ftp.pass");
	
	public static boolean uploadFile(List<File> fileList) throws IOException {
		FtpUtil ftpUtil=new FtpUtil(ftpIp, ftpUser, 21,ftpPass);
		logger.info("开始上传");
		boolean res = ftpUtil.upload("img",fileList);
		logger.info("结束上传，结果为：{}",res);
		return res;
		
	}
	
	private boolean upload(String remotePath,List<File> fileList) throws IOException {
		boolean uploaded=true;
		FileInputStream fis=null;
		if (connectServer(this.ip, this.port, this.user, this.pwd)) {
			try {
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(1024);//设置缓冲大小
				ftpClient.setControlEncoding("utf-8");//设置编码
				ftpClient.setFileType( FTPClient.BINARY_FILE_TYPE);//设置文件上传格式为二进制
				ftpClient.enterLocalPassiveMode();
				for (File file : fileList) {
					fis=new FileInputStream(file);
					ftpClient.storeFile(file.getName(), fis);
				}
				
			} catch (IOException e) {
				logger.error("文件上传异常", e);
				uploaded=false;
				e.printStackTrace();
			}finally {
				fis.close();
				ftpClient.disconnect();
			}
		}
		return uploaded;
		
		
	}
	
	private boolean connectServer(String ip,int port,String user,String pwd) {
		boolean isSuccess=false;
		ftpClient=new FTPClient();
		try {
			ftpClient.connect(ip);
			isSuccess=ftpClient.login(user, pwd);
		} catch (SocketException e) {
			logger.error("ftp连接异常",e);	
			
		} catch (IOException e) {
			logger.error("ftp连接异常",e);
		}
		
		return isSuccess;
	}
	
	
	private String ip;
	private String user;
	private int port;
	private String pwd;
	private FTPClient ftpClient;
	public static String getFtpIp() {
		return ftpIp;
	}
	public static void setFtpIp(String ftpIp) {
		FtpUtil.ftpIp = ftpIp;
	}
	public static String getFtpUser() {
		return ftpUser;
	}
	public static void setFtpUser(String ftpUser) {
		FtpUtil.ftpUser = ftpUser;
	}
	public static String getFtpPass() {
		return ftpPass;
	}
	public static void setFtpPass(String ftpPass) {
		FtpUtil.ftpPass = ftpPass;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public FTPClient getFtpClient() {
		return ftpClient;
	}
	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
	public FtpUtil(String ip, String user, int port, String pwd) {
		this.ip = ip;
		this.user = user;
		this.port = port;
		this.pwd = pwd;
	}
	
}
