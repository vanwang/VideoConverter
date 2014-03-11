package com.commov.video.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileUtils {

	/*public static void main(String[] args) {
		List arrayList = FileUtil.listSubFiles("d:/");
		for (Iterator i = arrayList.iterator(); i.hasNext();)
	    {
		     File temp = (File) i.next();
		     System.out.println(temp.getName()+":"+temp.isFile());
	    }
	}*/
	
	public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();    
	public final static Map<String, String> IMAGE_TYPE_MAP = new HashMap<String, String>(){
		private static final long serialVersionUID = -498660538343189862L;

		{
		put("jpg", "FFD8FF"); //JPEG (jpg)    
		put("png", "89504E47");  //PNG (png)  
		put("bmp", "424D"); //Windows Bitmap (bmp)    
		put("gif", "47494638");  //GIF (gif)    
		}
	};   
	public final static Map<String, String> VIDEO_TYPE_MAP = new HashMap<String, String>(){
		private static final long serialVersionUID = 3289410924927541835L;

		{//asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv
		put("avi", "41564920");    
		put("mpg", "000001BA");  //    
		put("mp4", "000001BB"); 
		put("wmv", "000001BD"); 
		put("flv", "000001BC"); 
		put("3gp", "000001BE"); 
		put("mov", "6D6F6F76");  //Quicktime (mov)    
		}
	};
	
	private static void getAllFileType()    
    {    
        FILE_TYPE_MAP.put("jpg", "FFD8FF"); //JPEG (jpg)    
        FILE_TYPE_MAP.put("png", "89504E47");  //PNG (png)    
        FILE_TYPE_MAP.put("gif", "47494638");  //GIF (gif)    
        FILE_TYPE_MAP.put("tif", "49492A00");  //TIFF (tif)    
        FILE_TYPE_MAP.put("bmp", "424D"); //Windows Bitmap (bmp)    
        FILE_TYPE_MAP.put("dwg", "41433130"); //CAD (dwg)    
        FILE_TYPE_MAP.put("html", "68746D6C3E");  //HTML (html)    
        FILE_TYPE_MAP.put("rtf", "7B5C727466");  //Rich Text Format (rtf)    
        FILE_TYPE_MAP.put("xml", "3C3F786D6C");    
        FILE_TYPE_MAP.put("zip", "504B0304");    
        FILE_TYPE_MAP.put("rar", "52617221");    
        FILE_TYPE_MAP.put("psd", "38425053");  //Photoshop (psd)    
        FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)    
        FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)    
        FILE_TYPE_MAP.put("pst", "2142444E");  //Outlook (pst)    
        FILE_TYPE_MAP.put("xls", "D0CF11E0");  //MS Word    
        FILE_TYPE_MAP.put("doc", "D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样    
        FILE_TYPE_MAP.put("mdb", "5374616E64617264204A");  //MS Access (mdb)    
        FILE_TYPE_MAP.put("wpd", "FF575043"); //WordPerfect (wpd)     
        FILE_TYPE_MAP.put("eps", "252150532D41646F6265");    
        FILE_TYPE_MAP.put("ps", "252150532D41646F6265");    
        FILE_TYPE_MAP.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)    
        FILE_TYPE_MAP.put("qdf", "AC9EBD8F");  //Quicken (qdf)    
        FILE_TYPE_MAP.put("pwl", "E3828596");  //Windows Password (pwl)    
        FILE_TYPE_MAP.put("wav", "57415645");  //Wave (wav)    
        FILE_TYPE_MAP.put("avi", "41564920");    
        FILE_TYPE_MAP.put("ram", "2E7261FD");  //Real Audio (ram)    
        FILE_TYPE_MAP.put("rm", "2E524D46");  //Real Media (rm)    
        FILE_TYPE_MAP.put("mpg", "000001BA");  //    
        FILE_TYPE_MAP.put("mov", "6D6F6F76");  //Quicktime (mov)    
        FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); //Windows Media (asf)    
        FILE_TYPE_MAP.put("mid", "4D546864");  //MIDI (mid)    
    }  
	
	/**  
     * Created on 2010-7-1   
     * <p>Discription:[getImageFileType,获取图片文件实际类型,若不是图片则返回null]</p>  
     * @param File  
     * @return fileType  
     * @author:[shixing_11@sina.com]  
     */    
    public final static String getImageFileType(File f)    
    {    
        if (isImage(f))  
        {  
            try  
            {  
                ImageInputStream iis = ImageIO.createImageInputStream(f);  
                Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);  
                if (!iter.hasNext())  
                {  
                    return null;  
                }  
                ImageReader reader = iter.next();  
                iis.close();  
                return reader.getFormatName();  
            }  
            catch (IOException e)  
            {  
                return null;  
            }  
            catch (Exception e)  
            {  
                return null;  
            }  
        }  
        return null;  
    }    
    
    /**  
     * Created on 2010-7-1   
     * <p>Discription:[getFileByFile,获取文件类型,包括图片,若格式不是已配置的,则返回null]</p>  
     * @param file  
     * @return fileType  
     * @author:[shixing_11@sina.com]  
     */    
    public final static String getFileTypeByFile(File file)    
    {    
        String filetype = null;    
        byte[] b = new byte[50];    
        try    
        {    
            InputStream is = new FileInputStream(file);    
            is.read(b);    
            filetype = getFileTypeByStream(b);    
            is.close();    
        }    
        catch (FileNotFoundException e)    
        {    
            e.printStackTrace();    
        }    
        catch (IOException e)    
        {    
            e.printStackTrace();    
        }    
        return filetype;    
    }    
        
    /**  
     * Created on 2010-7-1   
     * <p>Discription:[getFileTypeByStream]</p>  
     * @param b  
     * @return fileType  
     * @author:[shixing_11@sina.com]  
     */    
    public final static String getFileTypeByStream(byte[] b)    
    {    
        String filetypeHex = String.valueOf(getFileHexString(b));    
        Iterator<Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();    
        while (entryiterator.hasNext()) {    
            Entry<String,String> entry =  entryiterator.next();    
            String fileTypeHexValue = entry.getValue();    
            if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {    
                return entry.getKey();    
            }    
        }    
        return null;    
    }    
        
    /** 
     * Created on 2010-7-2  
     * <p>Discription:[isImage,判断文件是否为图片]</p> 
     * @param file 
     * @return true 是 | false 否 
     * @author:[shixing_11@sina.com] 
     */  
    public static final boolean isImage(File file){  
        boolean flag = false;  
        try  
        {  
            BufferedImage bufreader = ImageIO.read(file);  
            int width = bufreader.getWidth();  
            int height = bufreader.getHeight();  
            if(width==0 || height==0){  
                flag = false;  
            }else {  
                flag = true;  
            }  
        }  
        catch (IOException e)  
        {  
            flag = false;  
        }catch (Exception e) {  
            flag = false;  
        }  
        return flag;  
    }  
      
    /**  
     * Created on 2010-7-1   
     * <p>Discription:[getFileHexString]</p>  
     * @param b  
     * @return fileTypeHex  
     * @author:[shixing_11@sina.com]  
     */    
    public final static String getFileHexString(byte[] b)    
    {    
        StringBuilder stringBuilder = new StringBuilder();    
        if (b == null || b.length <= 0)    
        {    
            return null;    
        }    
        for (int i = 0; i < b.length; i++)    
        {    
            int v = b[i] & 0xFF;    
            String hv = Integer.toHexString(v);    
            if (hv.length() < 2)    
            {    
                stringBuilder.append(0);    
            }    
            stringBuilder.append(hv);    
        }    
        return stringBuilder.toString();    
    }  
	public static void installFullPathTemplate(String sourceFilePath, File destFile,
            Map<String, String> placeholderMap) {

        try {
            //BufferedWriter out = new BufferedWriter(new FileWriter(destFile));
        	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile),"UTF-8"));
            //BufferedReader in = new BufferedReader(new FileReader(sourceFilePath));
        	BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFilePath),"UTF-8"));
            String line;

            while ((line = in.readLine()) != null) {
                if (placeholderMap != null) {
                    for (Map.Entry<String, String> entry : placeholderMap.entrySet()) {
                        line = line.replace(entry.getKey(), entry.getValue());
                    }
                }
                out.write(line);
                out.newLine();
            }

            out.close();
            in.close();
        } catch (Exception e) {
        }

    }
	public static File checkExist(String filepath) throws Exception{

        File file=new File(filepath);
        if (file.exists()) {//判断文件目录的存在
            System.out.println("文件夹存在！");
            if(file.isDirectory()){//判断文件的存在性      
                  System.out.println("文件存在！");      
              }else{
               file.createNewFile();//创建文件
                System.out.println("文件不存在，创建文件成功！"   );      
              }
        }else {
            System.out.println("文件夹不存在！");
            File file2=new File(file.getParent());
            file2.mkdirs();
            System.out.println("创建文件夹成功！");
            if(file.isDirectory()){      
                  System.out.println("文件存在！");       
              }else{      
               file.createNewFile();//创建文件 
                System.out.println("文件不存在，创建文件成功！"   );      
              }
        }
        return file;
     }
	public static List<File> listSubFiles(String path){
		List<File> fileList = new ArrayList<File>();
		
		File f = new File(path);
		if (f.isDirectory())
		{
			File[] t = f.listFiles();
			for (int i = 0; i < t.length; i++)
		    {
				fileList.add(t[i]);
		    }
		}
		return fileList;
	}
	/**
	*
	* @param path 文件路径
	* @param suffix 后缀名
	* @param isdepth 是否遍历子目录
	* @return
	*/
	public static List<String> getListFiles(String path, String suffix, boolean isdepth)
	{
	   File file = new File(path);
	   return listFile(file ,suffix, isdepth);
	}

	public static List<String> listFile(File f, String suffix, boolean isdepth)
	{
		List<String> fileList = new ArrayList<String>();

	   //是目录，同时需要遍历子目录
	   if (f.isDirectory() && isdepth == true)
	   {
	    File[] t = f.listFiles();
	    for (int i = 0; i < t.length; i++)
	    {
	     listFile(t[i], suffix, isdepth);
	    }
	   }
	   else
	   {
	    String filePath = f.getAbsolutePath();
	    System.out.println(filePath);
	    if(suffix !=null)
	    {
	     int begIndex = filePath.lastIndexOf(".");//最后一个.(即后缀名前面的.)的索引
	     String tempsuffix = "";
	    
	     if(begIndex != -1)//防止是文件但却没有后缀名结束的文件
	     {
	      tempsuffix = filePath.substring(begIndex + 1, filePath.length());
	     }
	    
	     if(tempsuffix.equals(suffix))
	     {
	      fileList.add(filePath);
	     }
	    }
	    else
	    {
	     //后缀名为null则为所有文件
	     fileList.add(filePath);
	    }
	   
	   }
	  
	   return fileList;
	}
	
	/**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static void readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            System.out.println("以字节为单位读取文件内容，一次读一个字节：");
            // 一次读一个字节
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(fileName);
            showAvailableBytes(in);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            System.out.println("以字符为单位读取文件内容，一次读一个字节：");
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("以字符为单位读取文件内容，一次读多个字节：");
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        //File file = new File(fileName);
        String result = "";
        String line_sep = System.getProperty("line.separator");
        BufferedReader reader = null;
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            //reader = new BufferedReader(new FileReader(file));
        	reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
            String tempString = null;
            @SuppressWarnings("unused")
			int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
            	result += tempString + line_sep;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return result;
    }

    /**
     * 随机读取文件内容
     */
    public static void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            System.out.println("随机读取一段文件内容：");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(fileName, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 读文件的起始位置
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
            // 将一次读取的字节数赋给byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 显示输入流中还剩的字节数
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("当前字节输入流中的字节数为:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * A方法追加文件：使用RandomAccessFile
     */
    public static void appendMethodA(String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * B方法追加文件：使用FileWriter
     */
    public static void appendMethodB(String fileName, String content) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 使用FileWriter写入文件
     */
    public static void writeToFile(String fileName, String content) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
        	 FileOutputStream fos = new FileOutputStream(fileName);
        	 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8" );
        	 osw.write(content);
        	 osw.flush(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	public static String read(String fileName, String encoding) {
		StringBuffer fileContent = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(fileName);
			InputStreamReader isr = new InputStreamReader(fis, encoding);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				fileContent.append(line);
				fileContent.append(System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent.toString();
	}

	public static void write(String fileContent, String fileName,
			String encoding) throws Exception {
			FileOutputStream fos = new FileOutputStream(fileName);
			OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
			osw.write(fileContent);
			osw.flush();
	}
	
	public static File copyFile(String sourceFileString, String targetFileString) throws IOException {
    	File sourceFile = new File(sourceFileString);
    	File targetFile = new File(targetFileString);
    	copyFile(sourceFile, targetFile);
    	return targetFile;
    }
    
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } finally {
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        (new File(targetDir)).mkdirs();
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                File sourceFile = file[i];
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                String dir1 = sourceDir + "/" + file[i].getName();
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * 
     * @param srcFileName
     * @param destFileName
     * @param srcCoding
     * @param destCoding
     * @throws IOException
     */
    public static void copyFile(File srcFileName, File destFileName, String srcCoding, String destCoding) throws IOException {// ���ļ�ת��ΪGBK�ļ�
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFileName), srcCoding));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFileName), destCoding));
            char[] cbuf = new char[1024 * 5];
            int len = cbuf.length;
            int off = 0;
            int ret = 0;
            while ((ret = br.read(cbuf, off, len)) > 0) {
                off += ret;
                len -= ret;
            }
            bw.write(cbuf, 0, off);
            bw.flush();
        } finally {
            if (br != null)
                br.close();
            if (bw != null)
                bw.close();
        }
    }

    /**
     * 删除目录下的文件，不删除主目录
     * @param filepath
     * @throws IOException
     */
    public static void del(String filepath) throws IOException {
        File f = new File(filepath);
        if (f.exists() && f.isDirectory()) {
            if (f.listFiles().length == 0) {
                f.delete();
            } else {
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());
                    }
                    delFile[j].delete();
                }
            }
        }
    }
    
    public static boolean deleteFolder(String sPath) {  
        boolean flag = false;  
        File file = new File(sPath);  
        // 判断目录或文件是否存在  
        if (!file.exists()) {  // 不存在返回 false  
            return flag;  
        } else {  
            // 判断是否为文件  
            if (file.isFile()) {  // 为文件时调用删除文件方法  
                return deleteFile(sPath);  
            } else {  // 为目录时调用删除目录方法  
                return deleteDirectory(sPath);  
            }  
        }  
    }  
    
    public static boolean deleteFile(String sPath) {  
    	boolean flag = false;  
        File file = new File(sPath);  
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
            flag = true;  
        }  
        return flag;  
    } 
    
    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param   sPath 被删除目录的文件路径 
     * @return  目录删除成功返回true，否则返回false 
     */  
    public static boolean deleteDirectory(String sPath) {  
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {  
            sPath = sPath + File.separator;  
        }  
        File dirFile = new File(sPath);  
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {  
            return false;  
        }  
        boolean flag = true;  
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            //删除子文件  
            if (files[i].isFile()) {  
                flag = deleteFile(files[i].getAbsolutePath());  
                if (!flag) break;  
            } //删除子目录  
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag) break;  
            }  
        }  
        if (!flag) return false;  
        //删除当前目录  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
    
    
    /**
     * BASE64����
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64����
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * ��ȡ·���������ļ���
     * 
     * @param path
     * @return
     */
    public static String[] getFile(String path) {
        File file = new File(path);
        String[] name = file.list();
        return name;
    }

    /**
     * ��ȡ�ļ�������
     * 
     * @param path
     * @return
     * @throws IOException
     */
    public static String readFileToString(String path) throws IOException {
        String resultStr = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            byte[] inBuf = new byte[2000];
            int len = inBuf.length;
            int off = 0;
            int ret = 0;
            while ((ret = fis.read(inBuf, off, len)) > 0) {
                off += ret;
                len -= ret;
            }
            resultStr = new String(new String(inBuf, 0, off, "gbk").getBytes());
        } finally {
            if (fis != null)
                fis.close();
        }
        return resultStr;
    }

    /**
     * �ļ�ת���ֽ�����
     * 
     * @param path
     * @return
     * @throws IOException
     */
    public static byte[] readFileToBytes(String path) throws IOException {
        byte[] b = null;
        InputStream is = null;
        File f = new File(path);
        try {
            is = new FileInputStream(f);
            b = new byte[(int) f.length()];
            is.read(b);
        } finally {
            if (is != null)
                is.close();
        }
        return b;
    }

    /**
     * ��byteд���ļ���
     * 
     * @param fileByte
     * @param filePath
     * @throws IOException
     */
    public static void byteToFile(byte[] fileByte, String filePath) throws IOException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(filePath));
            os.write(fileByte);
            os.flush();
        } finally {
            if (os != null)
                os.close();
        }
    }

    /**
     * �п��ִ�
     * 
     * @param str
     * @return Ϊ��true
     */
    public static boolean strIsNull(String str) {
        return str == null || str.equals("");
    }

    /**
     * �۷�����
     * 
     * @param ary
     * @param subSize
     * @return
     */
    public static List<List<Object>> splitAry(Object[] ary, int subSize) {
        int count = ary.length % subSize == 0 ? ary.length / subSize : ary.length / subSize + 1;

        List<List<Object>> subAryList = new ArrayList<List<Object>>();

        for (int i = 0; i < count; i++) {
            int index = i * subSize;

            List<Object> list = new ArrayList<Object>();
            int j = 0;
            while (j < subSize && index < ary.length) {
                list.add(ary[index++]);
                j++;
            }

            subAryList.add(list);
        }

        return subAryList;
    }

    /**
     * @param mobile
     * @return
     */
    public static String ArrayToString(Object[] mobile) {
        String destId = "";
        for (Object phone : mobile) {
            destId += " " + (String) phone;
        }
        return destId.trim();
    }
}
