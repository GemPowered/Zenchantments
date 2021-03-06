package zedly.zenchantments.arrows.enchanted;

import org.bukkit.Particle;
import org.bukkit.entity.*;
import zedly.zenchantments.Storage;
import zedly.zenchantments.Utilities;
import zedly.zenchantments.arrows.EnchantedArrow;

public class FirestormArrow extends EnchantedArrow {

    public FirestormArrow(Arrow entity, int level, double power) {
        super(entity, level, power);
    }

    public void onImpact() {
        Utilities.display(Utilities.getCenter(arrow.getLocation()), Particle.FLAME, 100 * getLevel(), 0.1f,
                getLevel(), 1.5f, getLevel());
        double radius = 1 + getLevel() * getPower();
        for (Entity e : arrow.getNearbyEntities(radius, radius, radius)) {
            if (e instanceof LivingEntity && !e.equals(arrow.getShooter())
                    && Storage.COMPATIBILITY_ADAPTER.attackEntity(
                            (LivingEntity) e, (Player) arrow.getShooter(), 0)) {
                e.setFireTicks((int) Math.round(getLevel() * getPower() * 100));
            }
        }
        die();
    }
}
