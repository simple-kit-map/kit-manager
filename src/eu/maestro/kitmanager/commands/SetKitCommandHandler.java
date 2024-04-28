package eu.maestro.kitmanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.maestro.kitmanager.utils.KitUtils;
import net.md_5.bungee.api.ChatColor;

public class SetKitCommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			return false;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only a player can use this command");
			return true;
		}
		
		final String kitName = args[0];
		final Player player  = (Player)sender;
		
		final boolean success = KitUtils.saveKit(kitName, player, args);
		
		if(success)
			player.sendMessage(ChatColor.GREEN + "successfully created kit: " + kitName);
		else
			player.sendMessage(ChatColor.RED + "failed to create kit: " + kitName);
		
		return true;
	}

}
