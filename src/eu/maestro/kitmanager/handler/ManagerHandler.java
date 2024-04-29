package eu.maestro.kitmanager.handler;

import eu.maestro.kitmanager.KitManager;
import eu.maestro.kitmanager.handler.manager.FilesManager;

public class ManagerHandler {
	
	private FilesManager fileManager;
	public FilesManager getFileManager() { return fileManager; }
	
	public ManagerHandler(final KitManager plugin) {
		this.fileManager = new FilesManager(plugin);
	}

}
