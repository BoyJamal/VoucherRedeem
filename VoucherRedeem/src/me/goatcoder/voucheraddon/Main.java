package me.goatcoder.voucheraddon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

	private static Main instance;
	
	public void onEnable()
	{
		instance = this;
		Coupons.loadFiles();
		loadCommands();
		loadListeners();
	}
	
	public void onDisbale()
	{
		Coupons.removeCoupons();
	}
	
	public static Main getInstance()
	{
		return instance;
	}
	
	public void loadCommands()
	{
		getCommand("givecoupon").setExecutor(new GiveCoupon());
		getCommand("createcoupon").setExecutor(new CreateCoupon());
	}
	
	public void loadListeners()
	{
		Bukkit.getPluginManager().registerEvents(new RedeemEvent(), this);
	}
	
	public String chatColor(String message)
	{
		return ChatColor.translateAlternateColorCodes('&',message);
	}
}
