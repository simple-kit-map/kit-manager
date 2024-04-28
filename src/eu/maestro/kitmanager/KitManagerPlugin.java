package eu.maestro.kitmanager;

import org.bukkit.plugin.java.JavaPlugin;

import eu.maestro.kitmanager.commands.DisableCommandHandler;
import eu.maestro.kitmanager.commands.KitCommandHandler;
import eu.maestro.kitmanager.commands.SetKitCommandHandler;

public class KitManagerPlugin extends JavaPlugin {

	private static KitManagerPlugin instance;
	
	@Override
	public void onEnable() {
		instance = this;
		
		System.out.println("kitmanager enabled !");
		
		this.getCommand("disable").setExecutor(new DisableCommandHandler());
		this.getCommand("setkit").setExecutor(new SetKitCommandHandler());
		this.getCommand("kit").setExecutor(new KitCommandHandler());
	}
	
	@Override
	public void onDisable() {
		System.out.println("kitmanager disabled !");
	}
	
	public static KitManagerPlugin getPluginInstance() {
		return instance;
	}
	
	public void disablePlugin() {
		getServer().getPluginManager().disablePlugin(this);
	}
	
}
