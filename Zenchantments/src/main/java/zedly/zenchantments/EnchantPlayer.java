package zedly.zenchantments;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// This is used to manage players on the server. It allows for easy access in enabling/disabling enchantments
//      and for adding cooldowns for different enchantments as they are used
public class EnchantPlayer {

	public static final Map<Player, EnchantPlayer> PLAYERS = new HashMap<>();   // Collection of all players on the server

	private final Player                player;                          // Reference to the actual player object
	private final Map<Integer, Integer> enchantCooldown;   // Enchantment names mapped to their remaining cooldown

	// Creates a new enchant player objects and reads the player config file for their information
	public EnchantPlayer(Player player) {
		this.player = player;
		enchantCooldown = new HashMap<>();
		PLAYERS.put(player, this);
	}

	// Decrements the players cooldowns by one tick
	public void tick() {
		for (Iterator<Map.Entry<Integer, Integer>> iterator = enchantCooldown.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<Integer, Integer> entry = iterator.next();
			int cooldown = entry.getValue();
			if (cooldown <= 1) {
				iterator.remove();
			}
			else {
				entry.setValue(cooldown - 1);
			}
		}
	}

	// Returns true if the given enchantment name is disabled for the player, otherwise false
	public boolean isDisabled(int enchantmentID) {
		if (player.hasMetadata("ze." + enchantmentID)) {
			return player.getMetadata("ze." + enchantmentID).get(0).asBoolean();
		} else {
			player.setMetadata("ze." + enchantmentID, new FixedMetadataValue(Storage.zenchantments, false));
			return false;
		}
	}

	// Returns the cooldown remaining for the given enchantment name in ticks
	public int getCooldown(int enchantmentID) {
		return enchantCooldown.getOrDefault(enchantmentID, 0);
	}

	// Sets the given enchantment cooldown to the given amount of ticks
	public void setCooldown(int enchantmentID, int ticks) {
		enchantCooldown.put(enchantmentID, ticks);
	}

	// Disables the given enchantment for the player
	public void disable(int enchantmentID) {
		player.setMetadata("ze." + enchantmentID, new FixedMetadataValue(Storage.zenchantments, true));
	}

	// Enables the given enchantment for the player
	public void enable(int enchantmentID) {
		player.setMetadata("ze." + enchantmentID, new FixedMetadataValue(Storage.zenchantments, false));
	}

	// Disables all enchantments for the player
	public void disableAll() {
		for (CustomEnchantment enchant : Config.get(player.getWorld()).getEnchants()) {
			player.setMetadata("ze." + enchant.getId(), new FixedMetadataValue(Storage.zenchantments, true));
		}
	}

	// Enables all enchantments for the player
	public void enableAll() {
		for (CustomEnchantment enchant : Config.get(player.getWorld()).getEnchants()) {
			player.setMetadata("ze." + enchant.getId(), new FixedMetadataValue(Storage.zenchantments, false));
		}
	}

	// Returns the Player object associated with the EnchantPlayer
	public Player getPlayer() {
		return player;
	}

	// Returns the EnchantPlayer object associated with the given Player
	public static EnchantPlayer matchPlayer(Player player) {
		EnchantPlayer p = PLAYERS.get(player);
		if (p != null) {
			return (p);
		}
		return new EnchantPlayer(player);
	}

	// Removes a players associated EnchantPlayer object
	public static void removePlayer(Player player) {
		PLAYERS.remove(player);
	}

	// Sends the EnchantPlayer the given message
	public void sendMessage(String message) {
		player.sendMessage(message);
	}

	// Returns true if the EnchantPlayer has the given permission, otherwise false
	public boolean hasPermission(String permission) {
		return player.hasPermission(permission);
	}

}
