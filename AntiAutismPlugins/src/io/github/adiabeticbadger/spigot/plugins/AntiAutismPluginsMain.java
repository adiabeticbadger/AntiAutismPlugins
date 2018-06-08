package io.github.adiabeticbadger.spigot.plugins;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.adiabeticbadger.spigot.plugins.blockregen.BlockRegenHandler;
import io.github.adiabeticbadger.spigot.plugins.commands.CommandHandler;
import io.github.adiabeticbadger.spigot.plugins.io.BlockRegenIO;
import io.github.adiabeticbadger.spigot.plugins.listeners.CommandListener;

/**
 * Main class for the AntiAutismCouncil Discord plugins.
 * 
 * @author Brian Stewart
 */
public class AntiAutismPluginsMain extends JavaPlugin implements CommandListener
{
	
	private CommandHandler commandHandler = null;
	private BlockRegenHandler blockHandler = null;
	
	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();
		
		BlockRegenIO.init(getDataFolder());	
		
		if(commandHandler == null)
			commandHandler = new CommandHandler(this, this);
		
		if(blockHandler == null)
			blockHandler = new BlockRegenHandler(this);
		
		Bukkit.getServer().getPluginManager().registerEvents(blockHandler, this);
		Messaging.sendConsoleMessage("Plugin Pack Enabled! By: Brian Stewart");
	}
	
	@Override
	public void onDisable()
	{
		blockHandler.saveAll();
	}

	@Override
	public void commmandFired(CommandData data)
	{
		//Rearrange the command, and then pass it to the appropriate handler.
		if(data.getCommand().equals("blkrgn"))
		{
			String[] args = data.getArguments();
			String newCommand = args[0];
			
			CommandData newData = new CommandData(newCommand, Arrays.copyOfRange(args, 1, args.length), data.getPlayer());
			
			blockHandler.commmandFired(newData);
		}
	}
}