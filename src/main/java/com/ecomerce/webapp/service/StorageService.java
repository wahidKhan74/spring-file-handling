package com.ecomerce.webapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecomerce.webapp.exception.StorageException;

@Service
public class StorageService {
	
	@Value("${upload.path}")
	private String path;

	public void uploadFile(MultipartFile file) {
		if(file.isEmpty()) {
			throw new StorageException("Failed to Store a Empty File");
		}
		
		try {
			String fileName = file.getOriginalFilename();
			InputStream ins = file.getInputStream();			
			Files.copy(ins, Paths.get(path+fileName),StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			String msg = "Failed to upload file :"+file.getName();
			throw new StorageException(msg);
		}
	}
}
