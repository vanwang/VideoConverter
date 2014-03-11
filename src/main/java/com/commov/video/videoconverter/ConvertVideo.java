package com.commov.video.videoconverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ConvertVideo {

	private String ffmpegLocation = "ffmpeg.exe";
	// 原始文件
	private String videoPath;
	// 目标文件
	private String targetPath;
	// 图片文件
	private String imagePath;
	
	public void setFfmpegLocation(String ffmpegLocation) {
		this.ffmpegLocation = ffmpegLocation;
	}

	public ConvertVideo(String videoPath, String targetPath, String imagePath) {
		this.videoPath = videoPath;
		this.targetPath = targetPath;
		this.imagePath = imagePath;
	}

	public void process() {
		int type = checkContentType();
		if (type == 0) {
			this.ffmpegTransVideo();
			this.ffmpegTransImage();
		} else if (type == 1) {
			this.mencoderTransVideo();
		}
	}

	public int checkContentType() {
		String type = videoPath.substring(videoPath.lastIndexOf(".") + 1,
				videoPath.length()).toLowerCase();
		// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
		if (type.equals("avi")) {
			return 0;
		} else if (type.equals("mpg")) {
			return 0;
		} else if (type.equals("wmv")) {
			return 0;
		} else if (type.equals("3gp")) {
			return 0;
		} else if (type.equals("mov")) {
			return 0;
		} else if (type.equals("mp4")) {
			return 0;
		} else if (type.equals("asf")) {
			return 0;
		} else if (type.equals("asx")) {
			return 0;
		} else if (type.equals("flv")) {
			return 0;
		}
		// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
		// 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
		else if (type.equals("wmv9")) {
			return 1;
		} else if (type.equals("rm")) {
			return 1;
		} else if (type.equals("rmvb")) {
			return 1;
		}
		return 9;
	}

	public synchronized static boolean checkfile(String path) {
		File file = new File(path);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}

	/**
	 * 使用mencoder转码
	 * 
	 * @param videoPath
	 *            源路径 — 要转换的视频文件
	 * @param targetPath
	 *            目标路径 — 转换后的视频flv
	 * @return 返回目标路径
	 */
	public String mencoderTransVideo() {
		List<String> commend = new java.util.ArrayList<String>();
		commend.add("d:\\flv\\MediaCoder\\codecs\\mencoder.exe");
		commend.add(videoPath);
		// 音频采用mp3编码
		commend.add("-oac");
		commend.add("mp3lame");
		// 采用高质DivX视频编码，视频码率为112kbps
		commend.add("-ovc");
		commend.add("lavc");
		commend.add("-lavcopts");
		commend.add("vcodec=flv:vbitrate=500:mbd=2:mv0:trell:v4mv:cbp:last_pred=3:dia=-1:cmp=3:vb_strategy=1");
		commend.add("-lameopts");
		commend.add("abr:br=56");
		// 声音采样频率设置，现为22K
		commend.add("-srate");
		commend.add("22050");
		// -sws就是用来设置品质的，默认值为2
		commend.add("-sws");
		commend.add("3");
		// 宽度为208，高度自动调整保持比例；
		// -vf scale=-3:176宽度自动调整保持比例，高度为176；如果想保持原来的大小可以不要这个参数
		commend.add("-vf");
		commend.add("scale=512:-3");
		// 帧速率设置
		commend.add("-ofps");
		commend.add("18");
		/*
		 * mode=3:cbr:br=24单声道 音频码率为24kbps;-lameopts
		 * mode=0:cbr:br=24立体声，音频码率为24kbps; 还可设置音量，-lameopts
		 * mode=3:cbr:br=32:vol=1，设置范置为1~10，但不宜设得太高
		 */
		commend.add("-lameopts");
		commend.add("vbr=3:br=128");
		commend.add("-o");
		commend.add(targetPath);
		// 控制台显示执行的命令
		// System.out.println(commend);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.start();
			return targetPath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String ffmpegTransVideo() {
		// if (!checkfile(videoPath)) {
		// System.out.println(videoPath + " is not file aaa");
		// return false;
		// }
		List<String> commend = new java.util.ArrayList<String>();
		commend.add(ffmpegLocation);
		commend.add("-i");
		commend.add(videoPath);
		commend.add("-ab");
		commend.add("64");
		// commend.add(" -acodec ");
		// commend.add("codec");
		commend.add("-ac");
		commend.add("2");
		commend.add("-ar");
		commend.add("22050");
		// 清晰度 -qscale 4 为最好可是文件大, -qscale 6就可以了
		commend.add("-qscale");
		commend.add("6");
		// commend.add("-b");
		// commend.add("768");
		// commend.add("230");
		// commend.add("-s");
		// commend.add("352×240");
		// commend.add("-r");
		// commend.add("29.97");
		commend.add("-y");
		commend.add(targetPath);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			Process process = builder.start();
			 InputStream stderr = process.getErrorStream();
             InputStreamReader isr = new InputStreamReader(stderr);
             BufferedReader br = new BufferedReader(isr);
             String line = null;

             while ( (line = br.readLine()) != null)
                 System.out.println(line);

             int exitVal = process.waitFor();
             System.out.println("Process exitValue: " + exitVal);
			return targetPath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 生成图片 参数String newfilename, String newimg
	public boolean ffmpegTransImage() {
		List<String> commend = new java.util.ArrayList<String>();
		commend.add(ffmpegLocation);
		commend.add("-i");
		commend.add(videoPath);
		commend.add("-y");
		commend.add("-f");
		commend.add("image2");
		commend.add("-ss");
		commend.add("8");
		commend.add("-t");
		commend.add("0.001");
		//commend.add("-s");
		//commend.add("320×240");
		commend.add(imagePath);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			Process process = builder.start();
			 InputStream stderr = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ( (line = br.readLine()) != null)
                System.out.println(line);

            int exitVal = process.waitFor();
            System.out.println("Process exitValue: " + exitVal);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}