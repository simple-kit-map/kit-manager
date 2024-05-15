package eu.maestro.kitmanager.commands;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import eu.maestro.kitmanager.KitManager;
import eu.maestro.kitmanager.cache.Kit;

public class KitCommand extends Command {
	
	private KitManager plugin;
	private String name;

	public KitCommand(final KitManager plugin, String name) {
		super(name);
		this.plugin = plugin;
		this.setAliases(Arrays.asList("kit", "kits", "k", "ks", "ladder"));
		this.setUsage(ChatColor.RED + "usage: /" + name + " <kit> / create <kit> / rename <kit> / delete <kit> / list");
		this.name = name;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		
		
		final Set<String> kits = this.plugin.getManagerHandler().getFileManager().getKits().keySet();
		
		
		if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("list"))) {
			
			sender.sendMessage(this.usageMessage);
			
			if (kits.isEmpty()) {
				sender.sendMessage("There are no kits to list (empty)");
			}
			sender.sendMessage(ChatColor.GRAY + "There are " + kits.size() + " kits available:");
			sender.sendMessage("");
			for (String name : kits) {
				sender.sendMessage(name);
			}
			sender.sendMessage("");	
			return false;
		}
		if (args[0].equalsIgnoreCase("create")) {
			if (args.length != 2) {
				sender.sendMessage(ChatColor.RED + "/" + name + " create <kit>");
				return false;
			}
			if (!(sender instanceof Player)) {
				this.plugin.getConsole().log(Level.SEVERE, ChatColor.RED + "Only usable by a player!");
				return false;
			}
			boolean emptyInventory = true;
			for (int i = 0; i < Bukkit.getPlayer(sender.getName()).getInventory().getSize(); i++) {
				if (Bukkit.getPlayer(sender.getName()).getInventory().getContents()[i] != null) {
				    if (!Bukkit.getPlayer(sender.getName()).getInventory().getContents()[i].getType().equals(Material.AIR)) {
				        emptyInventory = false;
				        break;
				    }	
				}
			}
			if (emptyInventory) {
			    sender.sendMessage(ChatColor.RED + "Sorry but you don't have any items in your inventory!");
			    return false;
			}
			if (!sender.hasPermission("kitmanager.overwritekit") && this.plugin.getManagerHandler().getFileManager().getKits().containsKey(args[1])) {
				sender.sendMessage(ChatColor.RED + "Sorry but " + args[0] + " kit already exist please choose another name!");
				return false;
			}
			this.plugin.getManagerHandler().getFileManager().getKits().put(
					args[1],
					new Kit(
						Bukkit.getPlayer(
						sender.getName()).getUniqueId(),
						args[1], Bukkit.getPlayer(sender.getName()).getInventory().getContents(),
						Bukkit.getPlayer(sender.getName()).getInventory().getArmorContents(),
						Bukkit.getPlayer(sender.getName()).getActivePotionEffects().stream().collect(Collectors.toList())
						)
			);
			sender.sendMessage(ChatColor.GREEN + "Succesfully created the kit " + args[1]);
			return false;
		}
		if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
			
	
			if (!kits.contains(args[1])){
				sender.sendMessage(ChatColor.RED + "kit `" + args[1] + "` doesn't exist!");
				return false;
			}
			this.plugin.getManagerHandler().getFileManager().getKits().remove(args[1]);
			sender.sendMessage(ChatColor.GREEN + "deleted kit `" + args[1] + "` successfully");
			return false;
		}
		if (args.length == 1) {
			if (!(sender instanceof Player)) {
				this.plugin.getConsole().log(Level.SEVERE, ChatColor.RED + "Only usable by a player!");
				return false;
			}
			final Kit kit = this.plugin.getManagerHandler().getFileManager().getKits().get(args[0]);
			if (kit == null) {
				sender.sendMessage(ChatColor.RED + args[0] + " kit doesn't exist!");
				return false;
			}
			Bukkit.getPlayer(sender.getName()).getInventory().clear();
			Bukkit.getPlayer(sender.getName()).getInventory().setContents(kit.getContent());
			Bukkit.getPlayer(sender.getName()).updateInventory();
			
			Bukkit.getPlayer(sender.getName()).getInventory().setArmorContents(kit.getArmorContent());

			
			for(PotionEffect effect : Bukkit.getPlayer(sender.getName()).getActivePotionEffects())
			{
				Bukkit.getPlayer(sender.getName()).removePotionEffect(effect.getType());
			}
			
			if (!kit.getPotionEffect().isEmpty()) Bukkit.getPlayer(sender.getName()).addPotionEffects(kit.getPotionEffect());
			
			
			Bukkit.getPlayer(sender.getName()).setHealth(20);
			Bukkit.getPlayer(sender.getName()).setSaturation(20.0f);
			Bukkit.getPlayer(sender.getName()).setFoodLevel(20);
			sender.sendMessage(ChatColor.GREEN + args[0] + " kit has been set!!");
			return false;
		}
		return false;
	}

}
