package com.commov.video.videoconverter;
public class ThreadTransCode implements Runnable {

	private String ffmpegLocation = "c:\\ffmpeg.exe";
	// 原始文件
	private String videoPath;
	// 目标文件
	private String targetPath;
	// 图片文件
	private String imagePath;

	public ThreadTransCode(String videoPath, String targetPath, String imagePath) {
		this.videoPath = videoPath;
		this.targetPath = targetPath;
		this.imagePath = imagePath;
	}

	@Override
	public void run() {
			System.out.println("转码开始…………..");
			ConvertVideo cv = new ConvertVideo(videoPath, targetPath, imagePath);
			cv.setFfmpegLocation(ffmpegLocation);
			cv.process();
			System.out.println("转码结束…………..");
	}
}