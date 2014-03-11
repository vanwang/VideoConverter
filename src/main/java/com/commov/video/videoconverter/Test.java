package com.commov.video.videoconverter;

public class Test {

	public static void main(String[] args) {
		String videoPath = "C:\\Users\\vanwang\\Desktop\\aaaa.mp4";
        String targetPath = "C:\\Users\\vanwang\\Desktop\\cccc.mov";
        String imagePath = "C:\\Users\\vanwang\\Desktop\\cccc.jpg";
        
		//ThreadPool.trans(videoPath, targetPath);
        ThreadTransCode trans = new ThreadTransCode(videoPath, targetPath, imagePath);
        trans.run();
	}
}
