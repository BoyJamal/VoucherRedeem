package me.goatcoder.voucheraddon;

import java.util.List;

public class Creator {

	private String id;
	private String data;
	private String reference;
	private String range;
	private String name;
	private List<String> actions;
	private List<String> lore;
	private boolean glow;
	
	public Creator (String id, String data, String reference, String range, String name, List<String> actions, List<String> lore, boolean glow)
	{
		this.id = id;
		this.data = data;
		this.actions = actions;
		this.reference = reference;
		this.range = range;
		this.name = name;
		this.lore = lore;
		this.glow = glow;
	}
	
	public String getId() 
	{
		return id;
	}
	
	public String getData()
	{
		return data;
	}
	
	public String getCouponType()
	{
		return reference;
	}
	
	public String getXpRange()
	{
		return range;
	}
	
	public String getName()
	{
		return name;
	}
	
	public List<String> getLore()
	{
		return lore;
	}
	
	public List<String> getActions()
	{
		return actions;
	}
	
	public boolean hasGlow()
	{
		return glow;
	}
	
}
