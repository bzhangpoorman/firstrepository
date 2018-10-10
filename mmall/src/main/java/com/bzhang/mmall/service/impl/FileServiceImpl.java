package com.bzhang.mmall.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bzhang.mmall.service.FileService;
import com.bzhang.mmall.util.FtpUtil;
import com.google.common.collect.Lists;

@Service("fileService")
public class FileServiceImpl implements FileService{
	private Logger logger=LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String  upload(String path, MultipartFile file) {
		String filename = file.getOriginalFilename();
		String fileExtensionName=filename.substring(filename.lastIndexOf("."));
		String uploadFileName=UUID.randomUUID().toString()+fileExtensionName;
		logger.info("开始上传，文件名为：{}，路径：{}，新文件名：{}", filename,path,uploadFileName);
		File fileDir=new File(path);
		if (!fileDir.exists()) {
			fileDir.setWritable(true);
			fileDir.mkdirs();
		}
		File targetFile = new File(path+"/"+uploadFileName);
		try {
			
			//可用file.transferTo()
			FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
			
			FtpUtil.uploadFile(Lists.newArrayList(targetFile));
			
			targetFile.delete();
			
			
			
			
		} catch (IOException e) {
			logger.error("上传文件异常",e);
			return null;
		}
		
		return targetFile.getName();
	}

}
