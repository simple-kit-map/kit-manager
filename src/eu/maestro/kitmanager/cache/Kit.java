package eu.maestro.kitmanager.cache;

import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Kit {
	
	private UUID creator;
	public UUID getCreator() { return creator; }
	private String name;
	public String getName() { return name; }
	private ItemStack[] content;
	public ItemStack[] getContent() { return content; }
	private ItemStack[] armorContent;
	public ItemStack[] getArmorContent() { return armorContent; }
	private List<PotionEffect> potionEffect;
	public List<PotionEffect> getPotionEffect() { return potionEffect; }
	
	public Kit(final UUID creator, final String name, final ItemStack[] content, final ItemStack[] armorContent, final List<PotionEffect> potionEffect) {
		this.creator = creator;
		this.name = name;
		this.content = content;
		this.armorContent = armorContent;
		this.potionEffect = potionEffect;
	}

}
