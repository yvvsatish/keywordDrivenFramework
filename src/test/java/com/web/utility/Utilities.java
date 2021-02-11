package com.web.utility;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.web.config.ActionKeywords;

public class Utilities {

	public String takeScreenShot(String fileName){
		try {
			String path = System.getProperty("user.dir")+ File.separator+"Reports"+File.separator+"Screenshots";
			new File(path).mkdir();
			TakesScreenshot ts = (TakesScreenshot)ActionKeywords.driver;
			File srcFile = ts.getScreenshotAs(OutputType.FILE);
			String destination = path+"/"+fileName+".jpg";
			FileUtils.copyFile(srcFile, new File(destination));
			return destination;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void deleteFiles(File file) {
		if(file.exists()) {
			File[] files = file.listFiles();
			for(int i=0; i<files.length;i++) {
				if(files[i].isDirectory()) {
					deleteFiles(files[i]);
				}else {
					files[i].delete();
				}
			}
		}
	}
}
