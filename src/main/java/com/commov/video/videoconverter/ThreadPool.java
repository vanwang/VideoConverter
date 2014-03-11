package com.commov.video.videoconverter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

	public static ExecutorService exec = Executors.newFixedThreadPool(3);

	public static synchronized void trans(String videoPath, String targetPath, String imagePath) {
		ThreadTransCode trans = new ThreadTransCode(videoPath, targetPath, imagePath);
		exec.execute(trans);
	}
}