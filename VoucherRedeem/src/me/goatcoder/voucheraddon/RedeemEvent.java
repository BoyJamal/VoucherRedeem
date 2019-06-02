package me.goatcoder.voucheraddon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RedeemEvent implements Listener {

	@EventHandler
	public void onRedeem(PlayerInteractEvent e)
	{
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK
				|| e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			Player p = e.getPlayer();
			if (p.getInventory().getItemInMainHand() != null)
			{
				ItemStack coupon = p.getInventory().getItemInMainHand();
				if (!(coupon.hasItemMeta() && coupon.getItemMeta().hasDisplayName()))
				{
					return;
				}
				if (GiveCoupon.getSimilarCoupon(coupon) != null)
				{
					RandomCreator coup = GiveCoupon.getSimilarCoupon(coupon);
					int value = coup.getAmount();
					for (String rewards : coup.getCoupon().getActions())
					{
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), rewards.replaceAll("%xp%", String.valueOf(value)).
								replaceAll("%player%", p.getName()));
					}
					p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
					p.sendMessage(Main.getInstance().chatColor("&a&LSUCCESS &7You have redeemed a &a&n" + coup.getCoupon().getCouponType() + " Voucher&7 "
							+ "for &a&n" + value + "&7 " + coup.getCoupon().getCouponType()));
				}
			}
		}
	}
	
}
