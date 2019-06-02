package me.goatcoder.voucheraddon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CreateCoupon implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender.hasPermission("luckyessentials.admin"))
		{
			if (args.length == 3)
			{
				String type = args[0];
				Creator voucher = null;
				try {
					for (Creator vouch : Coupons.getActiveCoupons())
					{
						if (vouch.getCouponType().equalsIgnoreCase(type))
						{
							voucher = vouch;
						}
					}
				} catch (Exception e) {
					sender.sendMessage(Main.getInstance().chatColor("&c&lERROR &c&n" + type + "&7 is not a valid voucher type"));
					return true;
				}
				
					if (voucher == null)
					{
						sender.sendMessage(Main.getInstance().chatColor("&c&lERROR &c&n" + type + "&7 is not a valid voucher type"));
						return true;
					}
				
				String rawRange = args[1];
				int min = -1;
				int max = -1;
				try {
					String[] range = rawRange.split(";");
					min = Integer.parseInt(range[0]);
					max = Integer.parseInt(range[1]);
				} catch (Exception e) {
					sender.sendMessage(Main.getInstance().chatColor("&c&lERROR &c&n" + rawRange + "&7 is not a valid range"));
					sender.sendMessage(Main.getInstance().chatColor("&7&oExpected Format: min;max"));
					return true;
				}
				
				if (!(min > -1 && max > -1))
				{
					sender.sendMessage(Main.getInstance().chatColor("&c&lERROR &c&n" + rawRange + "&7 is not a valid range"));
					sender.sendMessage(Main.getInstance().chatColor("&7&oExpected Format: min;max"));
					return true;
				}
				Random ram = new Random();
				int value = ram.nextInt((max-min) +1) +min;
				Player p = null;
					try {
						p = Bukkit.getPlayer(args[2]);
					} catch (Exception e) {
						sender.sendMessage(Main.getInstance().chatColor("&c&lERROR &c&n" + args[2] + "&7 is not online"));
						return true;
					}
					if (p == null)
					{
						sender.sendMessage(Main.getInstance().chatColor("&c&lERROR &c&n" + args[2] + "&7 is not online"));
						return true;
					}
					
				ItemStack item = new ItemStack(Integer.valueOf(voucher.getId()),1,Byte.valueOf(voucher.getData()));
				ItemMeta im = item.getItemMeta();
				im.setDisplayName(Main.getInstance().chatColor(voucher.getName().replace("%xp%", String.valueOf(value))));
				List<String> newLore = new ArrayList<String>();
				for (String line : voucher.getLore())
				{
					String newLine = line.replace("%xp%", String.valueOf(value));
					newLine = Main.getInstance().chatColor(newLine);
					newLore.add(newLine);
				}
				im.setLore(newLore);
				item.setItemMeta(im);
				if (voucher.hasGlow())
				{
					im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					item.setItemMeta(im);
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
				p.getInventory().addItem(item);
				sender.sendMessage(Main.getInstance().chatColor("&a&lSUCCESS &7You have given &a&n" + p.getName() + "&7 a &a&n" + voucher.getCouponType() + "&7 voucher!"));
				p.sendMessage(Main.getInstance().chatColor("&a&lSUCCESS &7You have been given a &a&n" + voucher.getCouponType() + "&7 voucher!"));
				return true;
			} else {
				GiveCoupon.helpMessage(sender);
			}
		} else {
			GiveCoupon.noPermission(sender);
			return true;
		}
		return true;
	}

}
