package io.github.adiabeticbadger.spigot.plugins.exception;

public class PluginException extends Exception
{
	private final PluginExceptionSeverity severity;
	
	public PluginException(String message, PluginExceptionSeverity severity, Class<?> throwingClass)
	{
		super(message);
		this.severity = severity;
	}
	
	public PluginExceptionSeverity getExceptionSeverity()
	{
		return severity;
	}
}
