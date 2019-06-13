/**
 * 
 */
package com.edd.fileio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.edd.fileio.utils.EddFolderReaderUtils;
import com.edd.fileio.utils.ExcelReaderUtils;

/**
 * @author rohitkumar.patel
 *
 */
@Component
public class AppRunnerBean implements ApplicationRunner {
	
	@Autowired
	private ExcelReaderUtils excelReaderUtils;
	
	@Autowired
	private EddFolderReaderUtils eddFolderReader;

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		String[] strArgs = arg0.getSourceArgs();
		System.out.println("PartyId: " + strArgs[0]);
		System.out.println(excelReaderUtils.reader(strArgs[0]));
		//eddFolderReader.findAllFolders(excelReaderUtils.reader(strArgs[0]));
		eddFolderReader.copyFiles(eddFolderReader.findAllFolders(excelReaderUtils.reader(strArgs[0])));
	}
} 