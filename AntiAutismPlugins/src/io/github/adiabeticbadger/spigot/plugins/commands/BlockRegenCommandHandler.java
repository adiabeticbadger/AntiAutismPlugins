package io.github.adiabeticbadger.spigot.plugins.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.adiabeticbadger.spigot.plugins.listeners.CommandListener;
import io.github.adiabeticbadger.spigot.plugins.listeners.CommandListener.CommandData;

/**
 * Exists to pass up block regeneration commands.
 * @author Brian Stewart
 */
class BlockRegenCommandHandler implements CommandExecutor
{
	private final CommandListener listener;
	
	BlockRegenCommandHandler(CommandListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player)sender;
			
			if(args.length == 0)
				player.sendMessage(ChatColor.DARK_RED + "Usage: /blkregn {reload|settime} [# minutes]. Second argument is only used if settime is the first argument.");
			else
				listener.commmandFired(new CommandData(command.getName(), args, player));
		}
		
		return true;
	}
}
