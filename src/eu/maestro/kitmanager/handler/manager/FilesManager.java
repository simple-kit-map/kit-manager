package eu.maestro.kitmanager.handler.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.maestro.kitmanager.KitManager;
import eu.maestro.kitmanager.cache.Kit;
import eu.maestro.kitmanager.utils.BukkitSerialization;

public class FilesManager {
	
	private KitManager plugin;
	private Map<String, Kit> kits;
	public Map<String, Kit> getKits() { return kits; }
	
	public FilesManager(final KitManager plugin) {
		this.plugin = plugin;
		this.kits = new HashMap<>();
		this.load();
	}
	
    public void load() {
        final long timeUnit = System.currentTimeMillis();
        final File file = new File(this.plugin.getDataFolder() + "/kits.yml");
        if (file.exists()) {
            YamlConfiguration configFile = YamlConfiguration.loadConfiguration(file);
            for (String strs : configFile.getConfigurationSection("kits").getKeys(false)) {
                try {
                    List<PotionEffect> potionEffects = new ArrayList<>();
                    ConfigurationSection kitSection = configFile.getConfigurationSection("kits." + strs);
                    for (String effectKey : kitSection.getStringList("potionEffects")) {
                        PotionEffect effect = parsePotionEffectFromString(effectKey);
                        if (effect != null) {
                            potionEffects.add(effect);
                        }
                    }
                    this.kits.putIfAbsent(strs, new Kit(UUID.fromString(configFile.getString("kits." + strs + ".creator")), strs, BukkitSerialization.itemStackArrayFromBase64(configFile.getString("kits." + strs + ".content")), BukkitSerialization.itemStackArrayFromBase64(configFile.getString("kits." + strs + ".armorContent")), potionEffects));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }  
            long endTime = System.currentTimeMillis();
            this.plugin.getConsole().log(Level.INFO, ChatColor.GREEN + "Kits loaded in " + String.valueOf(endTime - timeUnit) + "ms!");
        }
    }

    public void save() {
        if (this.kits.size() != 0) {
            final long startTime = System.currentTimeMillis();
            for (Entry<String, Kit> entry : this.kits.entrySet()) {
                String uuid = entry.getKey();
                Kit kit = entry.getValue();
                File file = new File(this.plugin.getDataFolder() + "/kits.yml");
                if (!file.exists()) {
                    try {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                YamlConfiguration configFile = YamlConfiguration.loadConfiguration(file);
                List<String> potionEffectsAsString = new ArrayList<>();
                for (PotionEffect effect : kit.getPotionEffect()) {
                    potionEffectsAsString.add(formatPotionEffectToString(effect));
                }
                configFile.createSection("kits." + uuid + ".creator");
                configFile.createSection("kits." + uuid + ".potionEffects");
                configFile.createSection("kits." + uuid + ".content");
                configFile.createSection("kits." + uuid + ".armorContent");
                configFile.set("kits." + uuid + ".creator", kit.getCreator().toString());
                configFile.set("kits." + uuid + ".potionEffects", potionEffectsAsString);
                configFile.set("kits." + uuid + ".content", BukkitSerialization.itemStackArrayToBase64(kit.getContent()));
                configFile.set("kits." + uuid + ".armorContent", BukkitSerialization.itemStackArrayToBase64(kit.getArmorContent()));
                try {
                    configFile.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }   
            final long endTime = System.currentTimeMillis();
            this.plugin.getConsole().log(Level.INFO, ChatColor.GREEN + "Kits saved in " + String.valueOf(endTime - startTime) + "ms!");
        }
    }
    
    private PotionEffect parsePotionEffectFromString(String effectString) {
        if (effectString == null || effectString.isEmpty()) {
            return null;
        }

        String[] parts = effectString.split(":");
        if (parts.length != 3) {
            return null;
        }

        int type = Integer.parseInt(parts[0]);
        int duration = Integer.parseInt(parts[1]);
        int amplifier = Integer.parseInt(parts[2]);

        return new PotionEffect(PotionEffectType.getById(type), duration, amplifier);
    }

    private String formatPotionEffectToString(PotionEffect effect) {
        if (effect == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(effect.getType().getId()).append(":");
        builder.append(effect.getDuration()).append(":");
        builder.append(effect.getAmplifier());

        return builder.toString();
    }
}
