/**
 * 
 */
package com.edd.fileio.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author rohitkumar.patel
 *
 */
@Component
public class ExcelReaderUtils {

	@Value("${excel.file.name}")
	private String XLSX_FILE_PATH;

	@Cacheable("eddPartyIds")
	public  List<String> reader(String partyId) throws IOException, InvalidFormatException {
		Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();
		Map<String, List<String>> eddPartyIdsMap = new LinkedHashMap<String, List<String>>();
		sheet.forEach(row -> {
			row.forEach(cell -> {
				String[] cellValue = dataFormatter.formatCellValue(cell).split("\\|");
				if(cellValue.length > 0)
				{
					//System.out.println(dataFormatter.formatCellValue(cell));
					List<String> values = null;
					if(eddPartyIdsMap.get(cellValue[2]) == null)
						values = new LinkedList<String>();
					else
						values = eddPartyIdsMap.get(cellValue[2]);
					values.add(cellValue[1]);
					eddPartyIdsMap.put(cellValue[2], values);
				}
			});
		});
		System.out.println(eddPartyIdsMap);
		workbook.close();
		return eddPartyIdsMap.get(partyId);
	}
}
