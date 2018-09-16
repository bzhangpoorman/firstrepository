package com.bzhang.mmall.service;

import org.springframework.web.multipart.MultipartFile;

import com.bzhang.mmall.common.ServerResponse;

public interface FileService {
	String upload(String path, MultipartFile file);
}
