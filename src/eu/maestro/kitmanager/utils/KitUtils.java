package eu.maestro.kitmanager.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class KitUtils {
	
	public static boolean saveKit(final String name, final Player player, final String[] options) {
		try {
			final BukkitObjectOutputStream objectStream = new BukkitObjectOutputStream(new FileOutputStream("plugins/kitmanager/" + name + ".kit"));
			
			final PlayerInventory playerInventory = player.getInventory();
			
			final ItemStack[] inventoryContent = playerInventory.getContents();
			final ItemStack[] armorContent = playerInventory.getArmorContents();
			final Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
			
			objectStream.writeInt(inventoryContent.length);
			for(ItemStack ic : inventoryContent)
				objectStream.writeObject(ic);
			
			objectStream.writeInt(armorContent.length);
			for(ItemStack ac : armorContent)
				objectStream.writeObject(ac);
			
			objectStream.writeInt(potionEffects.size());
			for(PotionEffect pe : potionEffects)
				objectStream.writeObject(pe);
			
			objectStream.writeInt(options.length);
			for(String opt : options)
				objectStream.writeObject(opt);
			
			objectStream.flush();
			objectStream.close();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean loadKit(final String name, Player player) {
		try {

			final BukkitObjectInputStream objectStream = new BukkitObjectInputStream(new FileInputStream("plugins/kitmanager/" + name + ".kit"));
			
			final PlayerInventory playerInventory = player.getInventory();
			
			final ItemStack[] inventoryContent = new ItemStack[objectStream.readInt()];
			for(int i = 0; i < inventoryContent.length; i++) {
				inventoryContent[i] = (ItemStack)objectStream.readObject();
			}
			playerInventory.setContents(inventoryContent);
			
			final ItemStack[] armorContent = new ItemStack[objectStream.readInt()];
			for(int i = 0; i < armorContent.length; i++) {
				armorContent[i] = (ItemStack)objectStream.readObject();
			}
			playerInventory.setArmorContents(armorContent);
			
			final PotionEffect[] potionEffects = new PotionEffect[objectStream.readInt()];
			for(int i = 0; i < potionEffects.length; i++) {
				potionEffects[i] = (PotionEffect)objectStream.readObject();
			}
			for(PotionEffect e : player.getActivePotionEffects()){
				player.removePotionEffect(e.getType());
			}
			for(PotionEffect e : potionEffects) {
				player.addPotionEffect(e);
			}
			
			final String[] options = new String[objectStream.readInt()];
			for(int i = 0; i < options.length; i++) {
				options[i] = (String)objectStream.readObject();
				player.sendMessage(options[i]);
			}
			
			player.setSaturation(20.0f);
			player.setFoodLevel(20);
			
			objectStream.close();
			
			return true;
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
