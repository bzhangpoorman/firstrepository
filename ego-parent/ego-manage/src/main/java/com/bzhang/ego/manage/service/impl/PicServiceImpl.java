package com.bzhang.ego.manage.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bzhang.ego.commons.utils.FtpUtil;
import com.bzhang.ego.commons.utils.IDUtils;
import com.bzhang.ego.manage.service.PicService;
import com.google.common.collect.Maps;

@Service
public class PicServiceImpl implements PicService{
	
	@Value("${ftpclient.host}")
	private String host;
	@Value("${ftpclient.port}")
	private String port;
	@Value("${ftpclient.username}")
	private String username;
	@Value("${ftpclient.password}")
	private String password;
	@Value("${ftpclient.basepath}")
	private String basePath;
	@Value("${ftpclient.filepath}")
	private String filePath;
	
	@Override
	public Map<String, Object> upload(MultipartFile file) throws IOException {
		String filename = IDUtils.genImageName()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		
		boolean res = FtpUtil.uploadFile(host, Integer.parseInt(port), username, password, basePath, filePath, filename, file.getInputStream());
		
		Map<String , Object> map=Maps.newHashMap();
		if (res) {
			map.put("error", 0);
			map.put("url", "http://"+host+"/"+filename);
		}else {
			map.put("error", 1);
			map.put("message", "图片上传失败");
		}
		return map;
		
		
	}

}
