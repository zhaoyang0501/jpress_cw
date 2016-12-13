package io.jpress.jp.utils;

import io.jpress.utils.AttachmentUtils;
import io.jpress.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import sun.misc.BASE64Decoder;

import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

public class JpAttachmentUtils extends AttachmentUtils{

	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
//	static Date currTime = new Date();
//	static int month = currTime.getMonth()+1;//月
//	static String mstr=String.valueOf(month);
	/**
	 * @param uploadFile
	 * @return new file relative path
	 */
	public static String moveFile(UploadFile uploadFile,String etpId,String flag) {
		if (uploadFile == null)
			return null;

		File file = uploadFile.getFile();
		if (!file.exists()) {
			return null;
		}
		flag=flag.replace("年","").replace("月","");
		String webRoot = PathKit.getWebRootPath();

		String uuid = UUID.randomUUID().toString().replace("-", "");

		StringBuilder newFileName = new StringBuilder(webRoot).append(File.separator).append("attachment")
				.append(File.separator).append(etpId).append(File.separator).append(flag).append(File.separator)
				.append(uuid).append(".xls");

		File newfile = new File(newFileName.toString());
System.err.println("#############"+newfile);
		if (!newfile.getParentFile().exists()) {
			newfile.getParentFile().mkdirs();
		}
		
		file.renameTo(newfile);

		return newfile.getAbsolutePath().substring(webRoot.length());

	}
	
	public static String movePic(UploadFile uploadFile,BigInteger etpId ){
		if (uploadFile == null)
			return null;

		File file = uploadFile.getFile();
		if (!file.exists()) {
			return null;
		}
		String webRoot = PathKit.getWebRootPath();

		String uuid = UUID.randomUUID().toString().replace("-", "");
		
		
		StringBuilder newFileName =new StringBuilder(webRoot).append(File.separator).append("attachment")
				.append(File.separator).append(etpId.toString()).append(File.separator).append(dateFormat.format(new Date()))
				.append(File.separator).append(uuid).append(FileUtils.getSuffix(file.getName()));

		File newfile = new File(newFileName.toString());

		if (!newfile.getParentFile().exists()) {
			newfile.getParentFile().mkdirs();
		}

		file.renameTo(newfile);

		return FileUtils.removePrefix(newfile.getAbsolutePath(), webRoot);
	}

	
//	public static String generateImage(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
//		if (imgStr == null) // 图像数据为空
//			return null;
//		BASE64Decoder decoder = new BASE64Decoder();
//		try {
//			// Base64解码
//			byte[] b = decoder.decodeBuffer(imgStr);
//			for (int i = 0; i < b.length; ++i) {
//				if (b[i] < 0) {// 调整异常数据
//					b[i] += 256;
//				}
//			}
//			// 生成jpeg图片
//					
//			String webRoot = PathKit.getWebRootPath();
//
//			String uuid = UUID.randomUUID().toString().replace("-", "");
//
//			StringBuilder newFileName = new StringBuilder(webRoot).append(File.separator).append("attachment")
//					.append(File.separator).append(dateFormat.format(new Date())).append(File.separator).append(uuid)
//					
//					.append(".jpg");
//			
//			File newfile = new File(newFileName.toString());
//			if (!newfile.getParentFile().exists()) {
//				newfile.getParentFile().mkdirs();
//			}
//			
//			OutputStream out = new FileOutputStream(newFileName.toString());
//			out.write(b);
//			out.flush();
//			out.close();
//			return newfile.getAbsolutePath().substring(webRoot.length());
//		} catch (Exception e) {
//			return null;
//		}
//	}

}

