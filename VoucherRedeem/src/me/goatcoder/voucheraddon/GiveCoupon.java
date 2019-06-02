package me.goatcoder.voucheraddon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveCoupon implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender.hasPermission("luckyprison.givecoupons")))
		{
			noPermission(sender);
			return true;
		}
		
		if (args.length == 0)
		{
			helpMessage(sender);
			return true;
		}
		
		if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("list"))
			{
				List<String> couponNames = new ArrayList<String>();
				for (Creator list : Coupons.getActiveCoupons())
				{
					couponNames.add(list.getCouponType());
				}
				sender.sendMessage(Main.getInstance().chatColor("&e&lActive Vouchers:"));
				sender.sendMessage(couponNames.toString());
				return true;
			} else if (args[0].equalsIgnoreCase("reload")) {
				Coupons.getActiveCoupons().clear();
				Coupons.loadCoupons();
				sender.sendMessage(Main.getInstance().chatColor("&a&lSUCCESS &7You have reloaded the config!"));
				return true;
			} else {
				helpMessage(sender);
				return true;
			}
		}
		if (args.length > 1)
		{
			String name = args[0];
			Player p;
			if (Bukkit.getServer().getPlayer(name) == null)
			{
				sender.sendMessage(Main.getInstance().chatColor("&c&n&lERROR&7 " + name + " is not online"));
				return true;
			} else {
				p = Bukkit.getPlayer(name);
			}
			String couponName = args[1];
			for (Creator coupon : Coupons.getActiveCoupons())
			{
				if (coupon.getCouponType().equalsIgnoreCase(couponName))
				{
					Coupons.giveCoupon(p, coupon);
					return true;
				}
			}
			sender.sendMessage(Main.getInstance().chatColor("&c&n&LERROR&7 " + couponName + " &7does not exist"));
			return true;
		}
		
		return true;
	}
	
	public static void noPermission(CommandSender sender)
	{
		sender.sendMessage(Main.getInstance().chatColor("&c&n&lNo Permission&7 You cannot access this feature"));
		sender.sendMessage("");
		sender.sendMessage(Main.getInstance().chatColor("&7&o&nCreated by GOATCoder"));
	}
	
	public static void helpMessage(CommandSender sender)
	{
		sender.sendMessage(Main.getInstance().chatColor("&e&l+-=======================-+"));
		sender.sendMessage(" ");
		sender.sendMessage(Main.getInstance().chatColor("&7&l     LuckyVoucher-Add-On"));
		sender.sendMessage(Main.getInstance().chatColor("&7&o/givecoupon <name> <type>"));
		sender.sendMessage(Main.getInstance().chatColor("&7&o/givecoupon list"));
		sender.sendMessage(Main.getInstance().chatColor("&7&o/givecoupon reload"));
		sender.sendMessage(Main.getInstance().chatColor("&7&o/createcoupon <type> <range> <player>"));
		sender.sendMessage(" ");
		sender.sendMessage(Main.getInstance().chatColor("&e&l+-=======================-+"));
		sender.sendMessage("");
		sender.sendMessage(Main.getInstance().chatColor("&7&o&nCreated by GOATCoder"));
	}
	
	public static ItemStack createItem(Creator coupon)
	{
		ItemStack item = new ItemStack(Material.getMaterial(Integer.valueOf(coupon.getId())), 1, Byte.valueOf(coupon.getData()));
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(Main.getInstance().chatColor(coupon.getName()));
		im.setLore(colorCode(coupon.getLore()));
		item.setItemMeta(im);
		if (coupon.hasGlow())
		{
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(im);
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		return item;
	}
	
	public static RandomCreator getSimilarCoupon(ItemStack item)
	{
		if (!(item.hasItemMeta() && item.getItemMeta().hasDisplayName()))
		{
			return null;
		}
		
		String[] words = item.getItemMeta().getDisplayName().split(" ");
		
		for (Creator list : Coupons.getActiveCoupons())
		{
			ItemStack coupon = createItem(list);
			if (!(coupon.hasItemMeta() && coupon.getItemMeta().hasDisplayName()))
			{
				return null;
			}
			String[] coupWords = coupon.getItemMeta().getDisplayName().split(" ");
			if (coupWords.length == words.length)
			{
				if (coupWords[0].equals(words[0]) && coupWords[1].equals(words[1]))
				{
					if (coupon.getType() == item.getType())
					{
						if (list.hasGlow())
						{
							if (item.containsEnchantment(Enchantment.DURABILITY))
							{
								String numbers = ChatColor.stripColor(words[2]);
								numbers = numbers.replaceAll("\\D+","");
								return new RandomCreator(list,Integer.valueOf(numbers));
							}
						} else {
							String numbers = ChatColor.stripColor(words[2]);
							numbers = numbers.replaceAll("\\D+","");
							return new RandomCreator(list,Integer.valueOf(numbers));
						}
					}
				}
			}
		}
		return null;
	}
	
	public static List<String> colorCode(List<String> pickaxeLore) {
		List<String> converted = new ArrayList<>();
		for (String rawLore : pickaxeLore)
		{
			converted.add(Main.getInstance().chatColor(rawLore));
		}
		return converted;
	}
	
}
