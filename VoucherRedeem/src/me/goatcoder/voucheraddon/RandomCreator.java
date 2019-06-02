package me.goatcoder.voucheraddon;

public class RandomCreator {
	
	private Creator voucher;
	private int value;
	
	public RandomCreator(Creator voucher, int value)
	{
		this.value = value;
		this.voucher = voucher;
	}
	
	public Creator getCoupon()
	{
		return voucher;
	}
	
	public int getAmount()
	{
		return value;
	}

}
