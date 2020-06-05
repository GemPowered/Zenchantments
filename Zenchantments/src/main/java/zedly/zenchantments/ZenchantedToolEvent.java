package zedly.zenchantments;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class ZenchantedToolEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	private boolean cancelled = false;

	protected Event cause;
	protected ItemStack tool;
	protected CustomEnchantment enchantment;
	protected int level;

	public ZenchantedToolEvent(Player who, Event cause, ItemStack tool, CustomEnchantment ench, int level) {
		super(who);
		this.cause = cause;
		this.tool = tool;
		this.enchantment = ench;
		this.level = level;
	}

	public Event getCause() {
		return cause;
	}

	public ItemStack getTool() {
		return tool;
	}

	public CustomEnchantment getEnchantment() {
		return enchantment;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
