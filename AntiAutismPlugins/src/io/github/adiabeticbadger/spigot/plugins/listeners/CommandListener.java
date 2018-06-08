package io.github.adiabeticbadger.spigot.plugins.listeners;

import org.bukkit.entity.Player;

public interface CommandListener
{
	public void commmandFired(CommandData data);

	public final class CommandData
	{
		private final String command;
		private final String[] args;
		private final Player player;
		
		public CommandData(String command, String[] args, Player player)
		{
			this.command = command;
			this.args = args;
			this.player = player;
		}
		
		public String getCommand()
		{
			return command;
		}
		
		public String[] getArguments()
		{
			return args;
		}
		
		public Player getPlayer()
		{
			return player;
		}
	}
}

