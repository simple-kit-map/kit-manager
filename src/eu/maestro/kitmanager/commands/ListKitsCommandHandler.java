package eu.maestro.kitmanager.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListKitsCommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		final File folder = new File("plugins/kitmanager");
		
		StringBuilder ret = new StringBuilder();
		ret.append("\n");
		
	    for (final File fileEntry : folder.listFiles()) {
	    	
	        if (fileEntry.isDirectory()) {
	            continue;
	        }
	        
	    	String filename = fileEntry.getName();
	    	
	    	if (filename.endsWith(".kit"))
	    		ret.append("- " + fileEntry.getName().replace(".kit", "") + "\n");
	            //sender.sendMessage(fileEntry.getName().replace(".kit", ""));
	        
	    }
	    if (ret.length() != 0) 	    
	    	sender.sendMessage(ret.toString());
	    else
	    	sender.sendMessage("kitmanager: There are currently no kits");
	    
		return true;
	}
}