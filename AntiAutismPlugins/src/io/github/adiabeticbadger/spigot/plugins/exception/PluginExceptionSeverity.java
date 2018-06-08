package io.github.adiabeticbadger.spigot.plugins.exception;

public enum PluginExceptionSeverity
{
	NONE("NONE"),
	LOW("LOW"),
	HIGH("HIGH");
	
	private final String string;
	
	private PluginExceptionSeverity(String string)
	{
		this.string = string;
	}
	
	public String getString()
	{
		return string;
	}
}