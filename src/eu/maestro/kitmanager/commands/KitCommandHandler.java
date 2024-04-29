package eu.maestro.kitmanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.maestro.kitmanager.KitManagerPlugin;
import eu.maestro.kitmanager.utils.KitUtils;
import net.md_5.bungee.api.ChatColor;

public class KitCommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			return false;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only a player can use this command");
			return true;
		}
		
		final String kitName  = args[0];
		final Player player   = (Player)sender;
		final String kit      = KitManagerPlugin.getInstance().getKit(kitName);
		
		if(kit != null && KitUtils.deserializeKit(kit, player)) {
			player.sendMessage(ChatColor.GREEN + "successfully loaded kit: " + kitName);
		}
		else {
			player.sendMessage(ChatColor.RED + "failed to load kit: " + kitName);
		}
		
		return true;
	}

}
