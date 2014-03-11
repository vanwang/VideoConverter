package com.commov.video.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.commov.video.util.DateUtils;
import com.commov.video.util.FileUtils;
import com.commov.video.videoconverter.ThreadTransCode;

/**
 * 前台展示网站入口 controller
 * 
 * @author van
 * @version 1
 */
@Controller
@RequestMapping("/")
public class SiteController extends BaseController {

	@RequestMapping(value = "/converter", method = RequestMethod.POST)  
	public @ResponseBody Map<String, List<Map<String,Object>>> uploadMediaTmp(HttpServletRequest req, HttpServletResponse res, String userId) {
		
//		String userId = SecurityUtils.getAuthedMember().getUserId();
		String uploadRelativePath       = "upload"+File.separator+userId+File.separator+ DateUtils.dateToString(new Date(), "yyyyMMdd")+File.separator;
		String urlUploadRelativePath    = "upload"+"/"+userId+"/"+ DateUtils.dateToString(new Date(), "yyyyMMdd")+"/";
		String savePath = req.getSession().getServletContext().getRealPath(File.separator+uploadRelativePath)+File.separator;
		String urlPath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath()+ "/" + urlUploadRelativePath;
		
		//String savePath = FileManagerConstant.getUserFilePath(userId);
		
		MultipartHttpServletRequest mul = (MultipartHttpServletRequest) req;
		Iterator<String> it = mul.getFileNames();
		List<Map<String,Object>> fileList = new ArrayList<Map<String, Object>>();
		
		
		while(it.hasNext()){
			String name = it.next();
			List<MultipartFile> files = mul.getFiles(name);
			try {
				for (int i = 0; i < files.size(); i++) {
					CommonsMultipartFile mf = (CommonsMultipartFile) files.get(i);
					String fileName = mf.getOriginalFilename();
					long fileSize = mf.getSize();
					File file = FileUtils.checkExist(savePath + fileName);
					mf.getFileItem().write(file);
					
					String url = urlPath + fileName; 
					//String url = bcsService.fileUpload(file, userId);
					//System.out.println(url);
					String fileExtension = FilenameUtils.getExtension(fileName);
					boolean isVideo = FileUtils.VIDEO_TYPE_MAP.containsKey(fileExtension);
					boolean isMp4 = fileExtension.equalsIgnoreCase("mp4");
					String imagePath = "";
					if(isVideo && !isMp4){
						// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
						String videoPath = file.getAbsolutePath();
				        String targetPath = FilenameUtils.getFullPath(videoPath)+FilenameUtils.getBaseName(videoPath)+".mp4";
				        String localImagePath = FilenameUtils.getFullPath(videoPath)+FilenameUtils.getBaseName(videoPath)+".jpg";
				        
				        ThreadTransCode trans = new ThreadTransCode(videoPath, targetPath, localImagePath);
				        trans.run();
				        url = urlPath + FilenameUtils.getBaseName(videoPath)+".mp4";
				        imagePath = urlPath + FilenameUtils.getBaseName(videoPath)+".jpg";
					}
					
					Map<String, Object> fileMap = new HashMap<String, Object>();
					fileMap.put("name", fileName);
					fileMap.put("size", String.valueOf(fileSize));
					fileMap.put("url", url);
					fileMap.put("thumbnailUrl", imagePath);
					fileList.add(fileMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		res.setStatus(HttpServletResponse.SC_OK);
		
		Map<String, List<Map<String,Object>>> map = new HashMap<String, List<Map<String,Object>>>();
		map.put("files", fileList);
		return map;
	} 
	
}
