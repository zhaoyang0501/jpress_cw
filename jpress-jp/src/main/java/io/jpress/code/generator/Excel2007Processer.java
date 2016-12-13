package io.jpress.code.generator;

import io.jpress.model.Attachment;
import io.jpress.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jfinal.kit.PathKit;

public class Excel2007Processer implements IPoiProcesser {

	private File excel;
	private List<XSSFSheet> sheets;
	private List<String> imgList;
	private XSSFWorkbook xwb;
	
	public Excel2007Processer(File excel){
		this.excel = excel;
	}
	
	@Override
	public void init() {
		try {
			FileInputStream fis = new FileInputStream(excel);
			xwb = new XSSFWorkbook(fis);
			int sheetNum = xwb.getNumberOfSheets();
			sheets = new ArrayList<XSSFSheet>();
			for (int i = 0; i < sheetNum; i++){
				sheets.add(xwb.getSheetAt(i));
			}
			imgList = new ArrayList<String>();
			
			processSaveImg();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTitle() {
		return sheets.get(1).getRow(0).getCell(1).toString();
	}

	@Override
	public String getText() {
		 return sheets.get(2).getRow(0).getCell(0).toString();
	}

	
	private void processSaveImg() {
		List<XSSFPictureData> pictures = xwb.getAllPictures();
		for (int pnum = 0; pnum < pictures.size(); pnum++) {
			XSSFPictureData pictureData = pictures.get(pnum);
			byte[] data = pictureData.getData();
			String ext = pictureData.suggestFileExtension();// 获取扩展名

			String webRoot = PathKit.getWebRootPath();
			String uuid = UUID.randomUUID().toString().replace("-", "");
			StringBuilder filePath = new StringBuilder(webRoot).append(
					File.separator).append("attachment");
			File file = new File(filePath.append(File.separator).append(uuid).toString());
			if (!file.exists()) {
				file.mkdirs();
			}
			
			String fileString =FileUtils.removePrefix(file.getAbsolutePath(), webRoot);
			String fs = fileString.replace("\\", "/") + "." + ext;
			try {
				FileOutputStream out = new FileOutputStream(PathKit.getWebRootPath()+fs);
				Attachment atht = new Attachment();
				atht.setPath(fs);
				atht.setCreated(new Date());
				atht.saveOrUpdate();
				out.flush();
				out.write(data);
				out.close();
				imgList.add(fs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getProductAttr(){
		XSSFSheet sheet = sheets.get(1);
		String table=buildTable(sheet);
		return table;
	}
	
	@Override
	public String getProductAttachs() {
		XSSFSheet sheet = sheets.get(4);
		String table=buildTable(sheet);
		return table;
	}

	@Override
	public String getProductPara() {
		XSSFSheet sheet = sheets.get(3);
		String table=buildTable(sheet);
		return table;
	}
	
	
	private String buildTable(XSSFSheet sheet){
		StringBuilder table = new StringBuilder("<table><tbody>");
		for (int i = sheet.getFirstRowNum(); i < sheet
				.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = sheet.getRow(i);
			if (row != null) {
				table.append("<tr>");
				String key = "";
				String value = "";
				for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
					if ((j % 2) == 0) {
						key = row.getCell(j).toString();
						table.append("<td>"+key+"</td>");
					} else {
						value = row.getCell(j).toString();
						table.append("<td>"+value+"</td>");
					}
				}
				table.append("</tr>");
			}
		}
		table.append("</tbody></table>");
		return table.toString();
	}

	@Override
	public List<String> getImages() {
		return imgList;
	}
	
}
