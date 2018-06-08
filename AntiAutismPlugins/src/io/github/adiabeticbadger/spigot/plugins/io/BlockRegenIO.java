package io.github.adiabeticbadger.spigot.plugins.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import adiabeticbadger.utilities.storage.LinkedList;
import adiabeticbadger.utilities.storage.LinkedQueue;
import io.github.adiabeticbadger.spigot.plugins.blockregen.BlockRegenData;

/**
 * Handles any config/IO operations such as retrieving/setting
 * settings of the regenerator and converting working data lists
 * and queues to save-able formats.
 * 
 * @author Brian Stewart
 *
 */
public class BlockRegenIO
{
	private static File dataFolder = null;									//The folder our plugin files are contained.
	private static FileConfiguration config = new YamlConfiguration();		//The plugin configuration
	private static FileConfiguration blocks = new YamlConfiguration();		//The block regeneration data file
	
	/**
	 * Gets a list of block materials we are tracking for regeneration
	 * @return
	 */
	public static LinkedList<Material> getMaterials()
	{
		final LinkedList<Material> materials = new LinkedList<Material>();
		
		for(String s : config.getStringList("blockregen.materials"))
			materials.add(Material.getMaterial(s));
		
		return materials;
	}
	
	/**
	 * Parses the data contained in the block regeneration data file
	 * and converts it to a usable format for the plugin. Used on
	 * {@code /blkrgn reload} and initial startup.
	 * @return
	 */
	public static Queue<BlockRegenData> getRegenData()
	{
		Queue<BlockRegenData> data = new LinkedQueue<BlockRegenData>();
		
		int x, y, z;
		Location loc;
		Material mat;
		World world;
		Date time;
		
		for(String s : blocks.getStringList("blocks"))
		{
			String[] args = s.split(";");									//[material][worldname][x][y][z][time]
			
			if(args.length != 6)
				continue;

			mat = Material.getMaterial(args[0]);
			
			world = Bukkit.getServer().getWorld(args[1]);
			x = Integer.parseInt(args[2]);
			y = Integer.parseInt(args[3]);
			z = Integer.parseInt(args[4]);
			
			loc = new Location(world, x, y, z);
			time = new Date(Long.parseLong(args[5]));
			data.add(new BlockRegenData(loc, mat, time));
		}
		
		return data;
	}
	
	/**
	 * Gets the time in minutes to wait before regenerating a block.
	 * 
	 * @return The time, in minutes, between breaking and regenerating a block.
	 */
	public static int getTime()
	{
		return config.getInt("blockregen.time");
	}
	
	/**
	 * Gets the list of worlds the regenerator is enabled on.
	 * 
	 * @return The list of worlds the regenerator is enabled on.
	 */
	public static LinkedList<String> getWorldNames()
	{
		return new LinkedList<String>(config.getStringList("blockregen.worlds"));
	}
	
	/**
	 * Initializes the IO
	 * 
	 * @param dataFolder The folder the plugin data files are located
	 */
	public static void init(File dataFolder) 
	{
		try
		{
			BlockRegenIO.dataFolder = dataFolder;
			File file = new File(dataFolder, "config.yml");
			
			if(!file.exists())	
				file.createNewFile();
			else
				config.load(file);
			
			file = new File(dataFolder, "blocks.yml");

			if(!file.exists())	
				file.createNewFile();
			else
				blocks.load(file);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves the data files
	 */
	public static void saveConfig()
	{
		try
		{
			config.save(new File(dataFolder, "config.yml"));
			blocks.save(new File(dataFolder, "blocks.yml"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the material list for the block regenerator.
	 * 
	 * @param materials The list of materials
	 */
	public static void setMaterials(List<Material> materials)
	{
		List<String> matStr = new LinkedList<String>();
		
		for(Material m : materials)
			matStr.add(m.toString());
		
		config.set("blockregen.materials", matStr);
	}
	
	/**
	 * Parses the incoming block regeneration data and converts
	 * it to a save-able format
	 * 
	 * @param data The data to be set
	 */
	public static void setRegenData(Queue<BlockRegenData> data)
	{
		List<String> blockStr = new LinkedList<String>();
		String mat, world, x, y, z, time;
		Location loc;
		
		for(BlockRegenData br : data)
		{
			mat = br.getMaterial() + ";";
			loc = br.getLocation();
			world = br.getWorld()+ ";";
			x = loc.getBlockX() + ";";
			y = loc.getBlockY() + ";";
			z = loc.getBlockZ() + ";";
			time = br.getRegenTime().getTime() + "";
			
			blockStr.add(mat + world + x + y + z + time);
		}
		
		
		blocks.set("blocks", blockStr);
	}
	
	/**
	 * Sets the time between the breaking and regeneration of blocks.
	 * 
	 * @param time The time, in minutes.
	 */
	public static void setTime(int time)
	{
		config.set("blockregen.time", time);
	}

	/**
	 * Sets the world the renerator is enabled on.
	 * 
	 * @param worlds The list of worlds.
	 */
	public static void setWorlds(List<String> worlds)
	{
		config.set("blockregen.worlds", worlds);
	}
}
