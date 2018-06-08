package io.github.adiabeticbadger.spigot.plugins.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import adiabeticbadger.utilities.storage.LinkedList;

public class ExceptionHandler
{
	private final Logger logger;
	private final LinkedList<PluginException> exceptions;
	
	public ExceptionHandler()
	{	
		logger = Bukkit.getLogger();
		exceptions = new LinkedList<PluginException>();
	}
	
	public void log(PluginException pe)
	{
		exceptions.add(pe);
		
		Level level;
		
		if(pe.getExceptionSeverity() == PluginExceptionSeverity.HIGH)
			level = Level.SEVERE;
		else
			level = Level.WARNING;
		
		StackTraceElement origin = pe.getStackTrace()[0];
		int line = origin.getLineNumber();
		String className = origin.getClassName();
		String methodName = origin.getMethodName();
		
		logger.log(level, "[AntiAustismPlugins] " + ChatColor.RED + "Exception occurred in Class:"  + className + "; Method:  " + methodName + "; Line: " + line + "\nMessage: " + pe.getMessage());
	}
}
