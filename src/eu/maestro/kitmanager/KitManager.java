package eu.maestro.kitmanager;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import eu.maestro.kitmanager.commands.DisableCommand;
import eu.maestro.kitmanager.commands.KitCommand;
import eu.maestro.kitmanager.handler.ManagerHandler;
import net.minecraft.server.v1_7_R4.MinecraftServer;

public class KitManager extends JavaPlugin {

	private Logger console = this.getLogger();
	public Logger getConsole() { return console; }
	
	private ManagerHandler managerHandler;
	public ManagerHandler getManagerHandler() { return managerHandler; }
	
	@Override
	public void onEnable() {	
		this.managerHandler = new ManagerHandler(this);
	    final List<Command> commands = Arrays.asList(new DisableCommand(this, "disable"), new KitCommand(this, "kit"));
	    commands.forEach(cmd -> MinecraftServer.getServer().server.getCommandMap().register(cmd.getName(), this.getName(), cmd));
		console.log(Level.INFO, "Succesfully enabled!");
	}

	@Override
	public void onDisable() {
		this.managerHandler.getFileManager().save();
	}
	
}
