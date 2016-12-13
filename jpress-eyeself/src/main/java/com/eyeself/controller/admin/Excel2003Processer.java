package com.eyeself.controller.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jfinal.kit.PathKit;

import io.jpress.model.Attachment;
import io.jpress.utils.FileUtils;

public class Excel2003Processer implements IPoiProcesser {

	private File excel;
	private List<HSSFSheet> sheets;
	private List<String> imgList;
	private HSSFWorkbook workbook;
	
	public Excel2003Processer(File excel) {
		this.excel = excel;
	}

	@Override
	public void init() {
		try {
			
			FileInputStream fis = new FileInputStream(excel);
			workbook = new HSSFWorkbook(fis);
			int sheetNum = workbook.getNumberOfSheets();
			sheets = new ArrayList<HSSFSheet>();
			for (int i = 0; i < sheetNum; i++) {
				sheets.add(workbook.getSheetAt(i));
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
		return sheets.get(1).getRow(0).getCell(1).getStringCellValue();
	}

	@Override
	public String getText() {
		return sheets.get(2).getRow(0).getCell(0).getStringCellValue();
	}

	@Override
	public String getProductAttachs() {
		HSSFSheet sheet = sheets.get(4);
		String table=buildTable(sheet);
		return table;
	}

	@Override
	public String getProductAttr(){
		HSSFSheet sheet = sheets.get(1);
		String table=buildTable(sheet);
		return table;
	}

	@Override
	public String getProductPara(){
		HSSFSheet sheet = sheets.get(3);
		String table=buildTable(sheet);
		return table;
	}

	@Override
	public List<String> getImages() {
		return imgList;
	}
	
	private String buildTable(HSSFSheet sheet){
		StringBuilder table = new StringBuilder("<table><tbody>");
		for (int j = 0; j <= sheet.getLastRowNum(); j++) {
			HSSFRow row = sheet.getRow(j);
			if (row != null) {
				table.append("<tr>");
				int lastCellNum = row.getLastCellNum();
				String key = "";
				String value = "";
				for (int k = 0; k < lastCellNum; k++) {
					HSSFCell cell = row.getCell(k);
					if (cell != null) {
						row.getCell(k).setCellType(cell.CELL_TYPE_STRING);
						if ((k % 2) == 0) {
							key = cell.getStringCellValue();
							table.append("<td>"+key+"</td>");
						} else {
							value = cell.getStringCellValue();
							table.append("<td>"+value+"</td>");
						}
					}
				}
				table.append("</tr>");
			}
		}
		table.append("</tbody></table>");
		return table.toString();
	}
	
	private void processSaveImg(){
		List<HSSFPictureData> pictures = workbook.getAllPictures();
		HSSFSheet sheet = sheets.get(0);
		for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {

			if (shape instanceof HSSFPicture) {
				HSSFPicture pic = (HSSFPicture) shape;
				
				int pictureIndex = pic.getPictureIndex() - 1;
				HSSFPictureData picData = pictures.get(pictureIndex);
				String ext = picData.suggestFileExtension();
				byte[] data = picData.getData();

				String webRoot = PathKit.getWebRootPath();
				String uuid = UUID.randomUUID().toString().replace("-", "");
				StringBuilder filePath = new StringBuilder(webRoot).append(File.separator).append("attachment");
				File file = new File(filePath.append(File.separator).append(uuid).toString());
				if (!file.exists()) {
					file.mkdirs();
				}
				String fileString =FileUtils.removePrefix(file.getAbsolutePath(), webRoot);
				String fs = fileString.replace("\\", "/") + "." + ext;
				Attachment atht = new Attachment();
				atht.setPath(fs);
				atht.setCreated(new Date());
				atht.saveOrUpdate();
				FileOutputStream out;
				try {
					out = new FileOutputStream(PathKit.getWebRootPath()+fs);
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
	}

	@Override
	public String getProductName() {
		 return sheets.get(1).getRow(0).getCell(1).toString();
	}

	@Override
	public String getProductSpecification() {
		 return sheets.get(1).getRow(1).getCell(1).toString();
	}

	@Override
	public String getFactory() {
		 return sheets.get(1).getRow(2).getCell(1).toString();
	}

	@Override
	public String getApprovalNumber() {
		// TODO Auto-generated method stub
		return null;
	}
}
