package eu.maestro.kitmanager;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import eu.maestro.kitmanager.commands.KitCommandHandler;
import eu.maestro.kitmanager.commands.KitsCommandHandler;
import eu.maestro.kitmanager.commands.SetKitCommandHandler;
import eu.maestro.kitmanager.utils.KitUtils;

public class KitManagerPlugin extends JavaPlugin {
	
	public static KitManagerPlugin instance;
	
	private HashMap<String, String> kitMap;
	
	@Override
	public void onEnable() {
		instance = this;
		
		System.out.println("loading kits...");
		this.kitMap = KitUtils.loadKits();
		System.out.println("kits loaded!");
		
		this.registerCommands();
		
		System.out.println("kitmanager enabled !");
	}
	
	private void registerCommands() {
		this.getCommand("setkit").setExecutor(new SetKitCommandHandler());
		this.getCommand("kit").setExecutor(new KitCommandHandler());
		this.getCommand("kits").setExecutor(new KitsCommandHandler());
	}
	
	@Override
	public void onDisable() {
		System.out.println("saving kits...");
		KitUtils.writeKits(this.kitMap);
		System.out.println("kits saved!");
		
		System.out.println("kitmanager disabled !");
	}
	
	public static KitManagerPlugin getInstance() {
		return instance;
	}
	
	public final HashMap<String, String> getKits(){
		return this.kitMap;
	}
	
	public void addKit(final String name, final String kit) {
		this.kitMap.put(name, kit);
	}
	
	public String getKit(final String name) {
		return this.kitMap.get(name);
	}
}
