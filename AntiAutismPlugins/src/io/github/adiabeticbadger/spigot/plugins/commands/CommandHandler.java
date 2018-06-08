package io.github.adiabeticbadger.spigot.plugins.commands;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.adiabeticbadger.spigot.plugins.listeners.CommandListener;

/**
 * Top level command handler. Simply provides a bridge from the
 * main plugin to command handlers by passing commands up to be
 * distributed to the corresponding handler in the plugin via
 * interfaces.
 * 
 * @author Brian Stewart
 */
public class CommandHandler implements CommandListener
{
	private final TeleportCommandHandler tHandler;
	private final BlockRegenCommandHandler bHandler;
	private final CommandListener listener;
	
	public CommandHandler(JavaPlugin plugin, CommandListener listener)
	{
		this.listener = listener;
		
		tHandler = new TeleportCommandHandler();
		bHandler = new BlockRegenCommandHandler(this);
		
		registerCommands(plugin);
	}

	private void registerCommands(JavaPlugin plugin) 
	{
		plugin.getCommand("blkrgn").setExecutor(bHandler);
		plugin.getCommand("tph").setExecutor(tHandler);
		plugin.getCommand("tpws").setExecutor(tHandler);
	}

	@Override
	public void commmandFired(CommandData data)
	{
		listener.commmandFired(data);
	}
}
