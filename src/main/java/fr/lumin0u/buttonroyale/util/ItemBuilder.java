package fr.lumin0u.buttonroyale.util;

import fr.worsewarn.cosmox.tools.items.ImmutableItemStack;
import fr.worsewarn.cosmox.tools.utils.PlayerSkin;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.util.LinkedList;
import java.util.List;

public class ItemBuilder
{
	
	private ItemStack stack;
	
	private ItemBuilder()
	{}
	
	public ItemBuilder(Material mat) {
		stack = new ItemStack(mat);
	}
	
	public ItemBuilder(ItemStack item) {
		stack = item.clone();
	}
	
	public static ItemBuilder modify(ItemStack stack)
	{
		if(stack instanceof ImmutableItemStack)
			((ImmutableItemStack) stack).throwImmutable();
		
		ItemBuilder builder = new ItemBuilder();
		builder.stack = stack;
		return builder;
	}
	
	public ItemMeta getItemMeta() {
		return stack.getItemMeta();
	}
	
	public ItemBuilder setFireworkStarColor(Color color) {
		
		try
		{
			FireworkEffectMeta meta = (FireworkEffectMeta) stack.getItemMeta();
			FireworkEffect aa = FireworkEffect.builder().withColor(color).build();
			meta.setEffect(aa);
			setItemMeta(meta);
		} catch(NullPointerException ignored)
		{
		}
		
		return this;
	}
	
	public ItemBuilder setLeatherColor(Color color) {
		if(stack.getItemMeta() instanceof LeatherArmorMeta meta)
		{
			meta.setColor(color);
			setItemMeta(meta);
		}
		return this;
	}
	
	public ItemBuilder setPotionColor(Color color) {
		
		PotionMeta potionMeta = (PotionMeta) stack.getItemMeta();
		potionMeta.setColor(color);
		setItemMeta(potionMeta);
		
		return this;
	}
	
	public ItemBuilder setGlow(boolean glow) {
		if(glow)
		{
			addEnchant(Enchantment.KNOCKBACK, 1);
			addItemFlag(ItemFlag.HIDE_ENCHANTS);
		}
		else
		{
			ItemMeta meta = getItemMeta();
			for(Enchantment enchantment : meta.getEnchants().keySet())
			{
				meta.removeEnchant(enchantment);
			}
		}
		return this;
	}
	
	public ItemBuilder setUnbreakable(boolean unbreakable) {
		ItemMeta meta = stack.getItemMeta();
		meta.setUnbreakable(unbreakable);
		stack.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setBannerColor(DyeColor color) {
		BannerMeta meta = (BannerMeta) stack.getItemMeta();
		meta.setBaseColor(color);
		setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setAmount(int amount) {
		stack.setAmount(amount);
		return this;
	}
	
	public ItemBuilder setItemMeta(ItemMeta meta) {
		stack.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setHead(String owner) {
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(owner);
		setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setSkullTexture(String texture) {
		this.stack = PlayerSkin.itemFromTextureID(texture);// 45
		return this;// 46
	}
	
	public ItemBuilder setDisplayName(String displayname) {
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(displayname);
		setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setItemStack(ItemStack stack) {
		this.stack = stack;
		return this;
	}
	
	public ItemBuilder addLore(String lore) {
		
		ItemMeta meta = getItemMeta();
		
		List<String> lore2 = new LinkedList<>();
		
		if(meta.hasLore())
		{
			
			lore2 = meta.getLore();
		}
		lore2.add(lore);
		meta.setLore(lore2);
		setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder addLore(List<String> lore) {
		
		ItemMeta meta = getItemMeta();
		
		List<String> lore2 = new LinkedList<>();
		
		if(meta.hasLore())
		{
			lore2 = meta.getLore();
		}
		
		lore2.addAll(lore);
		meta.setLore(lore2);
		setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setLore(List<String> lore) {
		ItemMeta meta = getItemMeta();
		meta.setLore(lore);
		setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setLore(String lore) {
		return setLore(lore.split("\n"));
	}
	
	public ItemBuilder setLore(String... lore) {
		return setLore(List.of(lore));
	}
	
	public ItemBuilder addEnchant(Enchantment enchantment, int level) {
		ItemMeta meta = getItemMeta();
		meta.addEnchant(enchantment, level, true);
		setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder addBookEnchant(Enchantment enchantment, int level) {
		
		if(getItemMeta() instanceof EnchantmentStorageMeta meta)
		{
			meta.addStoredEnchant(enchantment, level, true);
			setItemMeta(meta);
		}
		return this;
	}
	
	public ItemBuilder addItemFlag(ItemFlag flag) {
		ItemMeta meta = getItemMeta();
		meta.addItemFlags(flag);
		setItemMeta(meta);
		return this;
	}
	
	public ItemStack build() {
		return stack.clone();
	}
	
	public ImmutableItemStack buildImmutable() {
		return new ImmutableItemStack(stack);
	}
}