package io.github.adiabeticbadger.spigot.plugins.blockregen;

import java.util.Calendar;
import java.util.Date;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import adiabeticbadger.utilities.storage.LinkedList;
import io.github.adiabeticbadger.spigot.plugins.Messaging;
import io.github.adiabeticbadger.spigot.plugins.io.BlockRegenIO;
import io.github.adiabeticbadger.spigot.plugins.listeners.CommandListener;

/**
 * Handles all events pertaining to the regeneration of blocks.
 * <br><br>
 * The handler ticks every 1200 server ticks (1 minute) and is synchronous to the
 * server, so any blocks that are broken are synchronous to the server
 * but asynchronous to the handler. These blocks will have their reference
 * tick to the minute prior to their actual break time.
 * <br><br>
 * If the block broken is on a regeneration-enabled world (controlled via command/config)
 * , then the block will be scheduled for regeneration a specified number
 * of minutes into the future, and this queue will be saved to the configuration every
 * 5 minutes, to lower server load. Should a /blkrgn reload command be issued or
 * the server crash before it can disable plugins, any block schedules since the last tick
 * will not be saved to file and will be lost.
 *
 */
public final class BlockRegenHandler extends BukkitRunnable implements CommandListener, Listener
{
	private Queue<BlockRegenData> regenData;					//FIFO Queue for blocks to regerate
	private LinkedList<String> worlds;							//list of worlds we are operating on
	private int regenTime; 										//how many minutes before regenerating
	private LinkedList<Material> materials;						//List of blocks to regenerate
	private int blockSaveTick = 0;
	
	private final Calendar regenTick = Calendar.getInstance();	//Gets updated every minute, used as the regen time
	
	/**
	 * Creates a new Block Regeneration Handler
	 * 
	 * @param plugin - Plugin reference for task scheduling.
	 */
	public BlockRegenHandler(Plugin plugin)
	{
		super();

		reload();
		
		//Calculate how many milliseconds until the next minute
		regenTick.setTime(new Date());
		regenTick.add(Calendar.MINUTE, 1);
		regenTick.set(Calendar.SECOND, 0);
		regenTick.set(Calendar.MILLISECOND, 0);
		
		long millis = regenTick.getTimeInMillis() - System.currentTimeMillis();
		
		regenTick.add(Calendar.MINUTE, regenTime);
		
		this.runTaskTimer(plugin, (millis / 50), 1200L); 		//Start on the next minute, + 1 ensures not an early start; 20 TPS = 50ms
																//with approx 1 minute between fires; 20TPS x 60S = 1200 tick
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		//Don't do anything if the block isn't on one of the enabled worlds
		Block block = event.getBlock();
		String worldName = block.getWorld().getName();
		
		if(!worlds.contains(worldName))
			return;

		//The block in question
		
		///If the block is in the config list then add it to the regen list and send a message to the console with details
		if(materials.contains(block.getType())) 
		{
			BlockRegenData data = new BlockRegenData(block.getLocation(), block.getType(), regenTick.getTime());
			regenData.add(data);
			Messaging.blockScheduled(data);
		}
	}

	@Override
	public void run()
	{
		//Reset the calendar to the current time and format
		regenTick.setTime(new Date());
		regenTick.set(Calendar.SECOND, 0);
		regenTick.set(Calendar.MILLISECOND, 0);
		
		//Capture time
		Date now = regenTick.getTime();
		
		//Then add the time
		regenTick.add(Calendar.MINUTE, regenTime);
	
		//Set the blocks in config
		if(blockSaveTick % 5 == 0 && !regenData.isEmpty())
			BlockRegenIO.setRegenData(regenData);
		blockSaveTick++;
		
		//Cycle through blocks list and regenerate as scheduled.
		boolean flag = true;
		
		while(!regenData.isEmpty() && flag)
		{
			BlockRegenData b = regenData.peek();
			
			if(b == null || now.before(b.getRegenTime()))
				flag = false;
			else
			{
				b.getLocation().getBlock().setType(b.getMaterial());
				Messaging.blockRegenerated(b);
				regenData.poll();
			}
		}
	}
	
	public void saveAll()
	{
		BlockRegenIO.setRegenData(regenData);
		BlockRegenIO.saveConfig();
	}
	
	private void reload()
	{
		this.regenTime = BlockRegenIO.getTime();
		this.worlds = BlockRegenIO.getWorldNames();
		this.materials = BlockRegenIO.getMaterials();
		this.regenData = BlockRegenIO.getRegenData();
		
		Messaging.sendConsoleMessage(regenData.size() + "");
	}

	@Override
	public void commmandFired(CommandData data)
	{
		String command = data.getCommand();
		Player player = data.getPlayer();
		String[] args = data.getArguments();
		
		if(command.equals("reload"))
		{
			reload();
			Messaging.blockConfigReload(regenTime, worlds.toString(), materials.toString(), player);
		}
		else if(command.equals("settime"))
		{
			if(args.length == 0)
				player.sendMessage(ChatColor.DARK_RED + "Usage: /blkregn settime minutes. Minutes must be a whole number");
			else
			{
				//If the try statement fails, i.e user enters a letter, regenTime will be unchanged
				int min = regenTime;
				try{min = Integer.parseInt(args[0]);}
				catch(Exception e) {player.sendMessage(Messaging.errorHighlight("Usage: /blkregn settime minutes. Minutes must be a whole number")); return;}
				regenTick.add(Calendar.MINUTE, Math.abs(min - regenTime));
				regenTime = min;
				BlockRegenIO.setTime(min);
				Messaging.blockRegenTimeChanged(min, player);
			}
		}
		else if(command.equals("add"))
		{
			if(args.length < 2)
				player.sendMessage(Messaging.errorHighlight("Usage: /blkregn add {material|world} name. Where name is a string"));
			else if(args[0].equals("material"))
			{
				Material material = Material.getMaterial(args[1]);
				
				if(material == null)
					player.sendMessage(Messaging.errorHighlight("Material " + Messaging.dataHighlight(args[1])) + " was not found.");
				else
				{
					materials.add(material);
					BlockRegenIO.setMaterials(materials);
					Messaging.blockMaterialAdded(material, player);
				}
			}
			else if(args[0].equals("world"))
			{
				String name = null;
				
				for(World w : Bukkit.getWorlds())
				{
					if(w.getName().equals(args[1]))
					{
						name = w.getName();
						break;
					}
				}
				
				if(name == null)
					player.sendMessage(Messaging.errorHighlight("World " + Messaging.dataHighlight(args[1])) + " was not found.");
				else
				{	
					worlds.add(name);
					BlockRegenIO.setWorlds(worlds);
					Messaging.blockWorldAdded(name, player);
				}
			}
		}
	}
}
