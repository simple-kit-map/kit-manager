package eu.maestro.kitmanager.commands;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import eu.maestro.kitmanager.KitManager;

public class DisableCommand extends Command {
	
	private KitManager plugin;

	public DisableCommand(final KitManager plugin, final String name) {
		super(name);
		this.setAliases(Arrays.asList("d"));
		this.setUsage("/disable");
		this.setPermission("kitmanager.disable");
		this.setPermissionMessage(ChatColor.RED + "No permissions");
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
		this.plugin.getLogger().warning(ChatColor.RED + "Disabler Command was used by " + sender.getName());
		return false;
	}
}
