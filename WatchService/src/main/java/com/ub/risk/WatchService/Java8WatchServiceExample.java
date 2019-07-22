package com.ub.risk.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.commons.io.FileUtils;

public class Java8WatchServiceExample {

	public static void main(String[] args) {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get("E:\\watchservice\\source");
			
			
			File source = new File("E:\\watchservice\\source");
			File dest = new File("E:\\watchservice\\destination");
			
			
			dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

			System.out.println("Watch Service registered for dir: " + dir.getFileName());

			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException ex) {
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();

					System.out.println(kind.name() + ": " + fileName);

					if (kind == ENTRY_CREATE) {

						try {
							System.out.println("Processing.....");
							

							// only copy
							// FileUtils.copyDirectory(source, dest);
							// StandardCopyOption.REPLACE_EXISTING);

							// overright
							Path sourcepath = Paths.get(source.toURI());
							Path destpath = Paths.get(dest.toURI());

							Files.copy(sourcepath, destpath, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					if (kind == ENTRY_MODIFY ) {
						System.out.println("My source file has changed!!!");
						 FileUtils.copyDirectory(source, dest);
						 break;
					}
				}

				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
