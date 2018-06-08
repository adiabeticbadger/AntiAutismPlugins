package io.github.adiabeticbadger.spigot.plugins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import io.github.adiabeticbadger.spigot.plugins.blockregen.BlockRegenData;

/**
 * Class that handles all off the console messaging.
 * Contains methods for formatting and sending messages.
 * Exists to shorten code in other classes that pertain
 * to sending messages to the console or player.
 * 
 * @author Brian Stewart
 */
public class Messaging
{
	public static final String PREFIX = "[" + ChatColor.AQUA + "AAC" + ChatColor.RESET + "]: ";			//Prefix for the plugin
	public static final String BLOCK_PREFIX = "[" + ChatColor.GREEN + "BR" + ChatColor.RESET + "]: ";	//Prefix for the block regenerator
	
	/**
	 * Sends a message when a configuration reload has happened
	 * 
	 * @param time The regeneration time
	 * @param worlds The worlds the regenerator is enabled on
	 * @param materials The materials that will be regenerated
	 * @param player The player that initiated the reload
	 */
	public static void blockConfigReload(int time, String worlds, String materials, Player player)
	{
		worlds = "Worlds:\n\t\t" + dataHighlight(worlds.replace(" ", "\n\t\t").trim()) + "\n\t";
		materials = "Materials:\n\t\t" + dataHighlight(materials.replace(" ", "\n\t\t").trim());

		String pref = eventHighlight("Configuration reloaded\n\t");
		String timeStr = "Regeneration Time: " +  dataHighlight(time + "\n\t");
		
		sendConsoleMessage(BLOCK_PREFIX + pref + timeStr + worlds + materials);
		player.sendMessage("Block Regeneration configuration files reloaded " + dataHighlight("successfully") + ".");
	}
	
	/**
	 * Sends a message that a material was added to the list.
	 * 
	 * @param material The material that was added
	 * @param player The player that added it
	 */
	public static void blockMaterialAdded(Material material, Player player)
	{
		sendConsoleMessage(BLOCK_PREFIX + eventHighlight(dataHighlight(material.toString()) + "was added to the list of materials to regenerate"));
		player.sendMessage(dataHighlight(material.toString()) + "was added to the list of materials to regenerate");
	}
	
	/**
	 * Sends a message that a block was regenerated.
	 * 
	 * @param block The data pertaining to the block that was regenerated
	 */
	public static void blockRegenerated(BlockRegenData block)
	{
		String material = eventHighlight("Regenerated: ") + dataHighlight(block.getMaterial().toString());
		String world = dataHighlight(block.getWorld());
		String coord = parseCoord(block.getLocation());
		
		sendConsoleMessage(material + " on world : " + world + " at coordinate: " + coord);
	}
	
	/**
	 * Sends a message that the regeneration time has changed.
	 * 
	 * @param time The new regeneration time
	 * @param player The player that changed the time
	 */
	public static void blockRegenTimeChanged(int time, Player player)
	{
		sendConsoleMessage(BLOCK_PREFIX + eventHighlight("Regeneration time set to: " + dataHighlight(time + "") + " minutes."));
		player.sendMessage("Regeneration time set to: " + Messaging.dataHighlight(time + "") + " minutes.");
	}
	
	/**
	 * Sends a message that a block was scheduled for regeneration
	 * 
	 * @param block Data pertaining to the block that was regenerated.
	 */
	public static void blockScheduled(BlockRegenData block)
	{
		String material = eventHighlight("Scheduled: ") + dataHighlight(block.getMaterial().toString());
		String world = dataHighlight(block.getWorld());
		String coord = parseCoord(block.getLocation());
		String time = dataHighlight(block.getRegenTime().toString());
		
		sendConsoleMessage(BLOCK_PREFIX + material + " on world : " + world + " at coordinate: " + coord + "at: " + time);
	}
	
	/**
	 * Sends a message that the regenerator was enabled on another world.
	 * 
	 * @param world The world the regenerator was enabled on.
	 * @param player The player that added the world.
	 */
	public static void blockWorldAdded(String world, Player player)
	{
		sendConsoleMessage(BLOCK_PREFIX + eventHighlight(dataHighlight(world) + "was added to the list of worlds to regenerate on"));
		player.sendMessage(dataHighlight(world) + "was added to the list of worlds to regenerate on");
	}
	
	/**
	 * Highlights a string yellow to mark data.
	 * Exists to shorten code.
	 * 
	 * @param data The string to highlight
	 * @return The highlighted string
	 */
	public static String dataHighlight(String data)
	{
		return ChatColor.YELLOW + data + ChatColor.RESET;
	}

	/**
	 * Highlights a string light purple to mark events.
	 * Exists to shorten code.
	 * 
	 * @param data The string to highlight
	 * @return The highlighted string
	 */
	public static String eventHighlight(String data)
	{
		return ChatColor.LIGHT_PURPLE + data + ChatColor.RESET;
	}

	/**
	 * Highlights a string red to mark errors.
	 * Exists to shorten code.
	 * 
	 * @param data The string to highlight
	 * @return The highlighted string
	 */
	public static String errorHighlight(String data)
	{
		return ChatColor.DARK_RED + data + ChatColor.RESET;
	}
	
	/**
	 * Parses a Location to a String in (x,y,z) format.
	 * 
	 * @param loc The location to parse.
	 * @return A string representation of the cooridinate.
	 */
	private static String parseCoord(Location loc)
	{
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		
		return dataHighlight("(" + x + ", " + y + ", " + z + ")");
	}
	
	/**
	 * Sends a prefixed console message.
	 * 
	 * @param message The message to send
	 */
	public static void sendConsoleMessage(String message)
	{
		Bukkit.getServer().getConsoleSender().sendMessage(PREFIX + message);
	}
}
