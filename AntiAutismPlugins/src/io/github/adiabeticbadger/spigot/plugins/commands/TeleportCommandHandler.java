package io.github.adiabeticbadger.spigot.plugins.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles all of the teleport commands for the pack.
 * 
 * @author Brian Stewart
 */
final class TeleportCommandHandler implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player)sender;
			String cmd = command.getName();
			
			if(cmd.equals("tph"))										//Teleports you to your bed.
				player.teleport(player.getBedSpawnLocation());
			else if(cmd.equals("tpws"))									//Teleports you to the world spawn.
				player.teleport(player.getWorld().getSpawnLocation());
		}
		
		return true;
	}
}
