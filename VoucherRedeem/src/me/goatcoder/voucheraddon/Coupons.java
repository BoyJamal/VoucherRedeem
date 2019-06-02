package me.goatcoder.voucheraddon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_12_R1.IScoreboardCriteria;

public class Coupons {

	private static ArrayList<Creator> customCoupons = new ArrayList<Creator>();
	private static File config;
    private static FileConfiguration configYML;
	
    public static void loadFiles()
    {
    	if (!(Main.getInstance().getDataFolder().exists()))
    	{
    		Main.getInstance().getDataFolder().mkdir();
    	}
        config = new File(Main.getInstance().getDataFolder(), "config.yml");
        if (!(config.exists()))
        {
            try {
                config.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try {
            configYML = YamlConfiguration.loadConfiguration(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadCoupons();
    }
    
	public static void loadCoupons()
	{
		ConfigurationSection couponTypes = configYML.getConfigurationSection("coupons");
		if (couponTypes == null)
		{
			return;
		}
		for (String key : couponTypes.getKeys(false))
		{
			ConfigurationSection itemType = couponTypes.getConfigurationSection(key + ".item");
			if (itemType != null)
			{
				Creator coupon = new Creator(itemType.getString("id"), itemType.getString("data"), key, itemType.getString("range"), itemType.getString("name"),
						couponTypes.getStringList(key + ".actions"), itemType.getStringList("lore"), itemType.getBoolean("glow"));
				customCoupons.add(coupon);
			}
		}
	}
	
	public static void removeCoupons()
	{
		customCoupons.clear();
	}
	
	public static List<Creator> getActiveCoupons()
	{
		return customCoupons;
	}
	
	public static void giveCoupon(Player p, Creator coupon)
	{
		String[] valueRange = coupon.getXpRange().split(";");
		try {
			Integer.valueOf(coupon.getId());
			Byte.valueOf(coupon.getData());
		} catch (Exception e) {
			for (int i = 0; i < 4; i++)
			{
				System.out.println("&c&lERROR &7Invalid number in " + coupon.getCouponType());
			}
		}
		ItemStack item = new ItemStack(Material.getMaterial(Integer.valueOf(coupon.getId())), 1, Byte.valueOf(coupon.getData()));
		ItemMeta im = item.getItemMeta();
		
		Random ram = new Random();
		int min = Integer.valueOf(valueRange[0]);
		int max = Integer.valueOf(valueRange[1]);
		int value = ram.nextInt((max-min) +1) +min;
		
		im.setDisplayName(Main.getInstance().chatColor(coupon.getName().replace("%xp%", String.valueOf(value))));
		List<String> newLore = new ArrayList<>();
		for (String line : coupon.getLore())
		{
			String newLine = line.replace("%xp%", String.valueOf(value));
			newLine = Main.getInstance().chatColor(newLine);
			newLore.add(newLine);
		}
		im.setLore(newLore);
		item.setItemMeta(im);
		if (coupon.hasGlow())
		{
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(im);
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		p.getInventory().addItem(item);
		p.sendMessage(Main.getInstance().chatColor("&a&lSUCCESS &7You have been givin a &a&n" + coupon.getCouponType() + "&7 coupon!"));
	}
	
}
