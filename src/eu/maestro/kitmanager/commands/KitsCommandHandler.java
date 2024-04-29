package eu.maestro.kitmanager.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import eu.maestro.kitmanager.KitManagerPlugin;

public class KitsCommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		StringBuilder ret = new StringBuilder();
		ret.append("\n");
		
		final HashMap<String, String> kitMap = KitManagerPlugin.getInstance().getKits();
		
	    for(final Map.Entry<String, String> kit : kitMap.entrySet()) {
	    	final String kitName = kit.getKey();
	    	ret.append("- " + kitName + "\n");
	    }
	    
	    if (ret.length() != 0) {
	    	System.out.println(ret.toString());
	    	sender.sendMessage(ret.toString());
	    }
	    else {
	    	sender.sendMessage("kitmanager: There are currently no kits");
	    }
	    	
	    
		return true;
	}
}