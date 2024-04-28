package eu.maestro.kitmanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.maestro.kitmanager.KitManagerPlugin;

public class DisableCommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player)sender;
			player.sendMessage("plugin disabled!");
		}
		
		KitManagerPlugin.getPluginInstance().disablePlugin();
		
		return true;
	}

}
