package zedly.zenchantments.enchantments;

import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import zedly.zenchantments.CustomEnchantment;
import zedly.zenchantments.Storage;
import zedly.zenchantments.Utilities;
import zedly.zenchantments.enums.Hand;
import zedly.zenchantments.enums.Tool;

import static org.bukkit.Material.AIR;
import static zedly.zenchantments.enums.Tool.SWORD;

public class Transformation extends CustomEnchantment {

    public static final int ID = 64;

    @Override
    public Builder<Transformation> defaults() {
        return new Builder<>(Transformation::new, ID)
                .maxLevel(3)
                .loreName("Transformation")
                .probability(0)
                .enchantable(new Tool[]{SWORD})
                .conflicting(new Class[]{})
                .description("Occasionally causes the attacked mob to be transformed into its similar cousin")
                .cooldown(0)
                .power(1.0)
                .handUse(Hand.LEFT);
    }

    @Override
    public boolean onEntityHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
        if (evt.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && evt.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            return false;
        }

        if (!(evt.getEntity() instanceof LivingEntity)) {
            return false;
        }
        if (evt.getEntity() instanceof Tameable) {
            if (((Tameable) evt.getEntity()).isTamed()) {
                return false;
            }
        }
        if (evt.getEntity().getCustomName() != null) {
            return false;
        }
        if (!evt.getEntity().isEmpty() || evt.getEntity().isInsideVehicle()) {
            return false;
        }
        if (evt.getEntity().getScoreboardTags().size() == 0) {
            return false;
        }
        if (!((LivingEntity) evt.getEntity()).hasAI()) {
            return false;
        }

        if (ADAPTER.attackEntity((LivingEntity) evt.getEntity(), (Player) evt.getDamager(), 0)) {
            if (Storage.rnd.nextInt(100) > (100 - (level * power * 8))) {
                LivingEntity newEnt = Storage.COMPATIBILITY_ADAPTER.TransformationCycle((LivingEntity) evt.getEntity(),
                        Storage.rnd);

                if (newEnt != null) {
                    if (evt.getDamage() > ((LivingEntity) evt.getEntity()).getHealth()) {
                        evt.setCancelled(true);
                    }
                    Utilities.display(Utilities.getCenter(evt.getEntity().getLocation()), Particle.HEART, 70, .1f,
                            .5f, 2, .5f);

                    double originalMaxHealth = ((LivingEntity) evt.getEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    double originalHealth = ((LivingEntity) evt.getEntity()).getHealth();

                    EntityEquipment equipment = ((LivingEntity) evt.getEntity()).getEquipment();
                    if (equipment.getBootsDropChance() > 0.0f && equipment.getBoots().getType() != AIR) {
                        newEnt.getWorld().dropItemNaturally(newEnt.getLocation(), equipment.getBoots());
                    }
                    if (equipment.getLeggingsDropChance() > 0.0f && equipment.getLeggings().getType() != AIR) {
                        newEnt.getWorld().dropItemNaturally(newEnt.getLocation(), equipment.getLeggings());
                    }
                    if (equipment.getChestplateDropChance() > 0.0f && equipment.getChestplate().getType() != AIR) {
                        newEnt.getWorld().dropItemNaturally(newEnt.getLocation(), equipment.getChestplate());
                    }
                    if (equipment.getHelmetDropChance() > 0.0f && equipment.getHelmet().getType() != AIR) {
                        newEnt.getWorld().dropItemNaturally(newEnt.getLocation(), equipment.getHelmet());
                    }
                    if (equipment.getItemInMainHandDropChance() > 0.0f && equipment.getItemInMainHand().getType() != AIR) {
                        newEnt.getWorld().dropItemNaturally(newEnt.getLocation(), equipment.getItemInMainHand());
                    }
                    if (equipment.getItemInOffHandDropChance() > 0.0f && equipment.getItemInOffHand().getType() != AIR) {
                        newEnt.getWorld().dropItemNaturally(newEnt.getLocation(), equipment.getItemInOffHand());
                    }

                    evt.getEntity().remove();

                    newEnt.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(originalMaxHealth);
                    newEnt.setHealth(originalHealth);

                }
            }
        }
        return true;
    }
}
