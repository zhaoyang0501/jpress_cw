package io.jpress.code.generator;

import io.jpress.core.JBaseController;
import io.jpress.jp.Consts;
import io.jpress.jp.utils.DateUtils;
import io.jpress.model.Attachment;
import io.jpress.model.Content;
import io.jpress.model.query.ContentQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.FileUtils;
import io.jpress.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.PictureData;

import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jfinal.kit.PathKit;

@RouterMapping(url = "/act", viewPath = "/templates/jp/client")
public class ExcelDatePicture extends JBaseController {

	public void execl() {

		// File file = new File("G:\\zzz1.xls");
		File file = new File("G:\\zz.xlsx");
		String fname = file.getName();
		// fname.endsWith(".xls");
		if (fname.endsWith(".xls")) {
			xls(file); //office 2003
		} else {
			xlsx(file);
		}
	}

	public void xlsx(File file) {
		Content content = ContentQuery.me().findFirstByModuleAndUserId(
				Consts.MODULE_ENTERPRISE, new BigInteger("21"));
		try {
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook xwb = new XSSFWorkbook(fis);

			int sheetNum = xwb.getNumberOfSheets();

			for (int i = 0; i <= sheetNum; i++) {
				XSSFSheet sheet = xwb.getSheetAt(i);
				switch (i) {
				case 0:
					saveXpic(xwb);
					break;
				case 1:
					saveXdate(sheet, content);
					break;
				case 2:

					for (int j = sheet.getFirstRowNum(); j < sheet
							.getPhysicalNumberOfRows(); j++) {
						XSSFRow row = sheet.getRow(j);
						if (row != null) {
							for (int k = row.getFirstCellNum(); k < row
									.getPhysicalNumberOfCells(); k++) {
								// 通过 row.getCell(j).toString() 获取单元格内容，
								String cell = row.getCell(j).toString();
								if (StringUtils.isNotBlank(cell)) {
									content.setText(cell);
									content.saveOrUpdate();
								}
							}
						}
					}

					break;
				case 3:
					saveXdate(sheet, content);
					break;
				case 4:
					saveXdate(sheet, content);
					break;

				default:
					break;
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void xls(File file) {
		Content content = ContentQuery.me().findFirstByModuleAndUserId(
				Consts.MODULE_ENTERPRISE, new BigInteger("21"));
		try {
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			List<HSSFPictureData> pictures = workbook.getAllPictures();
			// HSSFSheet sheet = workbook.getSheetAt(0);
			int sheetNum = workbook.getNumberOfSheets();
			for (int i = 0; i < sheetNum; i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				switch (i) {
				case 0:
					int pnum = 0;
					for (HSSFShape shape : sheet.getDrawingPatriarch()
							.getChildren()) {
						HSSFClientAnchor anchor = (HSSFClientAnchor) shape
								.getAnchor();

						if (shape instanceof HSSFPicture) {
							HSSFPicture pic = (HSSFPicture) shape;
							int row = anchor.getRow1();
							int pictureIndex = pic.getPictureIndex() - 1;
							HSSFPictureData picData = pictures
									.get(pictureIndex);
							savePic(row, picData);
						}
						pnum++;
					}
					break;
				case 1:
					saveDate(sheet, content);

					break;
				case 2:
					int lastRowNum = sheet.getLastRowNum();
					for (int j = 0; j <= lastRowNum; j++) {
						HSSFRow row = sheet.getRow(j);
						if (row != null) {
							int lastCellNum = row.getLastCellNum();

							for (int k = 0; k < lastCellNum; k++) {
								HSSFCell cell = row.getCell(k);
								if (cell != null) {
									row.getCell(k).setCellType(
											cell.CELL_TYPE_STRING);
									content.setText(cell.getStringCellValue());
									content.saveOrUpdate();
								}

							}
						}

					}

					break;
				case 3:
					saveDate(sheet, content);
					break;
				case 4:
					saveDate(sheet, content);
					break;
				default:
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveXpic(XSSFWorkbook xwb) {
		List<XSSFPictureData> pictures = xwb.getAllPictures();
		for (int pnum = 0; pnum < pictures.size(); pnum++) {
			XSSFPictureData pictureData = pictures.get(pnum);
			byte[] data = pictureData.getData();
			String ext = pictureData.suggestFileExtension();// 获取扩展名

			String webRoot = PathKit.getWebRootPath();
			String uuid = UUID.randomUUID().toString().replace("-", "");
			StringBuilder filePath = new StringBuilder(webRoot).append(
					File.separator).append("attachment");
			File file = new File(filePath.toString());
			if (!file.exists()) {
				file.mkdirs();
			}
			String fileString = filePath.append(File.separator).append(uuid)
					.toString();
			String fileName = fileString.replace("\\", "/") + "." + ext;
			String fs = FileUtils.removePrefix(fileName, webRoot);

			// FileOutputStream out = new FileOutputStream("G:\\zz\\" + "img_" +
			// pnum + "." + ext);

			try {
				FileOutputStream out = new FileOutputStream(fs);
				Attachment atht = new Attachment();
				atht.setPath(fs);
				atht.setCreated(new Date());
				atht.saveOrUpdate();

				out.flush();
				out.write(data);
				out.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void saveXdate(XSSFSheet sheet, Content content) {

		// 循环输出表格中的内容
		for (int i = sheet.getFirstRowNum(); i < sheet
				.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = sheet.getRow(i);
			if (row != null) {
				String key = "";
				String value = "";
				for (int j = row.getFirstCellNum(); j < row
						.getPhysicalNumberOfCells(); j++) {
					// 通过 row.getCell(j).toString() 获取单元格内容，
					// cell = row.getCell(j).toString();
					if ((j % 2) == 0) {
						key = row.getCell(j).toString();
					} else {
						value = row.getCell(j).toString();
					}
				}
				content.saveOrUpdateMetadta(key, value);
			}

		}
	}

	public void saveDate(HSSFSheet sheet, Content content) {
		int lastRowNum = sheet.getLastRowNum();
		for (int j = 0; j <= lastRowNum; j++) {
			HSSFRow row = sheet.getRow(j);
			if (row != null) {
				int lastCellNum = row.getLastCellNum();
				String key = "";
				String value = "";
				for (int k = 0; k < lastCellNum; k++) {
					HSSFCell cell = row.getCell(k);
					if (cell != null) {
						row.getCell(k).setCellType(cell.CELL_TYPE_STRING);
						if ((k % 2) == 0) {
							key = cell.getStringCellValue();
						} else {
							value = cell.getStringCellValue();
						}
					}

				}
				content.saveOrUpdateMetadta(key, value);
			}
		}
	}

	private void savePic(int i, PictureData pic) throws Exception {

		String ext = pic.suggestFileExtension();

		byte[] data = pic.getData();

		String webRoot = PathKit.getWebRootPath();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		StringBuilder filePath = new StringBuilder(webRoot).append(
				File.separator).append("attachment");
		// .append(File.separator).append(uuid);

		File file = new File(filePath.toString());
		if (!file.exists()) {
			file.mkdirs();
		}
		String fileString = filePath.append(File.separator).append(uuid)
				.toString();

		String fileName = fileString.replace("\\", "/") + "." + ext;
		String fs = FileUtils.removePrefix(fileName, webRoot);
		Attachment atht = new Attachment();
		atht.setPath(fs);
		atht.setCreated(new Date());
		atht.saveOrUpdate();
		FileOutputStream out = new FileOutputStream(fs);
		out.flush();
		out.write(data);
		out.close();
	}
}
