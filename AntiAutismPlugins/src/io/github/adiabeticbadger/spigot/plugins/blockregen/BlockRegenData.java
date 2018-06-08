package io.github.adiabeticbadger.spigot.plugins.blockregen;

import java.util.Date;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Wrapper class that contains the relevant
 * data to regenerating blocks.
 * 
 * @author Brian Stewart
 */
public final class BlockRegenData
{
	private final Location loc;				//The location of the block
	private final Date regenTime;			//The date and time to regenerate it on
	private final Material regenMaterial;	//The material the block will be regenerated to
	
	public BlockRegenData(Location loc, Material regenMaterial, Date date)
	{
		this.loc = loc;
		this.regenTime = date;
		this.regenMaterial = regenMaterial;
	}
	
	public Location getLocation()
	{
		return loc;
	}
	
	public Date getRegenTime()
	{
		return regenTime;
	}
	
	public Material getMaterial()
	{
		return regenMaterial;
	}
	
	public String getWorld()
	{
		return loc.getWorld().getName();
	}
}
