/**
 * 
 */
package com.edd.fileio.utils;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author rohitkumar.patel
 *
 */
@Component
public class EddFolderReaderUtils {

	@Value("${edd.folders.input.path}")
	private String EDD_FOLDERS_INPUT_PATH;

	@Value("${edd.folders.output.path}")
	private String EDD_FOLDERS_OUTPUT_PATH;

	@Cacheable("findAllFolderPaths")
	public List<String> findAllFolders(List<String> foldernames)
	{
		List<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(EDD_FOLDERS_INPUT_PATH))) {
			result = walk.filter(Files::isDirectory)
					.map(path -> path.toString())
					.filter(r -> (foldernames.stream().filter(f -> r.contains(f.toLowerCase().trim())).count() > 0)).map(r -> r.toString())
					.collect(Collectors.toList());
			result.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}


	public void copyFiles(List<String> folderList)
	{	
		folderList.forEach(source -> {
			try {
				Files.walkFileTree(Paths.get(source), new SimpleFileVisitor<Path>() {
					final Path targetDir = Paths.get(EDD_FOLDERS_OUTPUT_PATH +"/"+ Paths.get(source).getFileName());
			        @Override
			        public FileVisitResult preVisitDirectory(final Path dir,
			                final BasicFileAttributes attrs) throws IOException {
			            Files.createDirectories(targetDir.resolve(Paths.get(source) 
			                    .relativize(dir)));
			            return FileVisitResult.CONTINUE;
			        }

			        @Override
			        public FileVisitResult visitFile(final Path file,
			                final BasicFileAttributes attrs) throws IOException {
			            Files.copy(file,
			            		targetDir.resolve(Paths.get(source).relativize(file)));
			            return FileVisitResult.CONTINUE;
			        }
			    });
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}