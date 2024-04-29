package eu.maestro.kitmanager.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class KitUtils {

	public static HashMap<String, String> loadKits() {
		final HashMap<String, String> kitMap = new HashMap<String, String>();
		final File kitsFolder = new File("plugins/kitmanager");
		
		for(final File kitFile : kitsFolder.listFiles()) {
			
			if(kitFile.isDirectory()) {
				continue;
			}
			
			final String fileName = kitFile.getName();
			final String kitName = fileName.replace(".kit", "");
			
			try {
				final FileReader fileReader = new FileReader(kitFile);
				final BufferedReader bufferedReader = new BufferedReader(fileReader);
				
				final StringBuilder stringBuilder = new StringBuilder();
				
				String line = null;
				while((line = bufferedReader.readLine()) != null)
					stringBuilder.append(line);
				
				kitMap.put(kitName, stringBuilder.toString());
				bufferedReader.close();
				
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return kitMap;
	}
	
	public static void writeKits(HashMap<String, String> kits) {
		for(Map.Entry<String, String> kit : kits.entrySet()) {
			final String kitName = kit.getKey();
			final String kitContent = kit.getValue();
			
			try {
				
				final FileOutputStream outputStream = new FileOutputStream("plugins/kitmanager/" + kitName + ".kit");
				final PrintStream printStream = new PrintStream(outputStream);
				
				printStream.print(kitContent);
				
				printStream.close();
				
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static String serializeKit(final Player player) {
		try {
			final ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
			
			final BukkitObjectOutputStream objectStream = new BukkitObjectOutputStream(memoryStream);
			
			final PlayerInventory playerInventory = player.getInventory();
			
			final ItemStack[] inventoryContent = playerInventory.getContents();
			final ItemStack[] armorContent = playerInventory.getArmorContents();
			final Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
			
			objectStream.writeInt(inventoryContent.length);
			for(ItemStack ic : inventoryContent) {
				if(ic != null && ic.getType().equals(Material.WRITTEN_BOOK))
					objectStream.writeObject(null);
				else
					objectStream.writeObject(ic);
			}
			
			objectStream.writeInt(armorContent.length);
			for(ItemStack ac : armorContent)
				objectStream.writeObject(ac);
			
			objectStream.writeInt(potionEffects.size());
			for(PotionEffect pe : potionEffects)
				objectStream.writeObject(pe);
			
			objectStream.flush();
			objectStream.close();
			
			return Base64Coder.encodeLines(memoryStream.toByteArray());
		}
		catch(Exception ex){
			ex.printStackTrace();
			return "";
		}
	}
	
	public static boolean deserializeKit(final String kit, final Player player) {
		try {
			final ByteArrayInputStream memoryStream = new ByteArrayInputStream(Base64Coder.decodeLines(kit));
			final BukkitObjectInputStream objectStream = new BukkitObjectInputStream(memoryStream);
			
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
			
			player.setHealth(20.0);
			player.setSaturation(20.0f);
			player.setFoodLevel(20);
			
			objectStream.close();
			
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
