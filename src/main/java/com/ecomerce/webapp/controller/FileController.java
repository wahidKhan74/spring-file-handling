package com.ecomerce.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecomerce.webapp.exception.StorageException;
import com.ecomerce.webapp.service.StorageService;

@Controller
public class FileController {
	
	@Autowired
	StorageService storageService;
	
	@RequestMapping("/")
	public String index() {
		return "index.html";
	}
	
	@RequestMapping(value="/do-upload", method = RequestMethod.POST,consumes= {"multipart/form-data"} )
	public String upload(@RequestParam MultipartFile file) {
		storageService.uploadFile(file);
		return "redirect:/success.html";
	}
	
	@ExceptionHandler(StorageException.class)
	public String handelStrorageException() {
		return "redirect:/failure.html";
	}
	
	@RequestMapping(value="/download", method=RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadFile() throws IOException{
		// find file path
		String fileName ="static/dump.txt";
		// create a class load 
		ClassLoader classLoader = new FileController().getClass().getClassLoader();
		// find a file by using class loader
		File file = new File(classLoader.getResource(fileName).getFile());
		// create stram from file
		InputStreamResource resource = new InputStreamResource( new FileInputStream(file));
		
		// create header as meta info for file download
		HttpHeaders headers = new HttpHeaders();		
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		
		ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok().headers(headers)
				.contentLength(file.length()).contentType(MediaType.parseMediaType("application/text")).body(resource);
		
		return responseEntity;
	}
}
