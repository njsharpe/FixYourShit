package net.njsharpe.fixyourshit.listener;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import net.njsharpe.fixyourshit.Constants;
import net.njsharpe.fixyourshit.FixYourShit;
import net.njsharpe.fixyourshit.entity.Entities;
import net.njsharpe.fixyourshit.event.ConcretePowderItemConvertEvent;
import net.njsharpe.fixyourshit.item.SafariNet;
import net.njsharpe.fixyourshit.item.TimeBottle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EntityListener implements Listener {

    @SuppressWarnings({"unused", "deprecation"})
    @EventHandler
    public void onDropConcrete(ItemSpawnEvent event) {
        Item item = event.getEntity();
        ItemStack itemStack = item.getItemStack();
        Material material = itemStack.getType();

        String name = material.name();
        String ending = Constants.getConcretePowderTypeEnding();

        if(name.endsWith(ending)) {
            String color = name.substring(0, name.length() - ending.length());
            if(color.equals("LEGACY")) {
                return;
            }

            Material concrete = Material.getMaterial(color + "_CONCRETE");
            if(concrete == null) {
                return;
            }

            Bukkit.getScheduler().runTaskTimer(FixYourShit.getInstance(), task -> {
                if(!item.isInWater() && !item.isOnGround()) {
                    return;
                }

                task.cancel();

                Location location = item.getLocation();
                Block block = location.getBlock();
                if(!block.getType().equals(Material.WATER)) {
                    return;
                }

                ConcretePowderItemConvertEvent e = new ConcretePowderItemConvertEvent(item);
                Bukkit.getPluginManager().callEvent(e);

                if(e.isCancelled()) {
                    return;
                }

                itemStack.setType(concrete);
                item.setItemStack(itemStack);
            }, 0L, 5L);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onProjectileLaunch(PlayerLaunchProjectileEvent event) {
        Projectile projectile = event.getProjectile();
        ItemStack item = event.getItemStack();

        SafariNet net = SafariNet.from(item);
        if(net == null) {
            return;
        }

        PersistentDataContainer container = projectile.getPersistentDataContainer();
        EntityType type = net.getType();
        byte[] bytes = net.getBytes();
        container.set(Constants.getEntityTypeKey(), PersistentDataType.INTEGER, type.ordinal());
        container.set(Constants.getEntityKey(), PersistentDataType.BYTE_ARRAY, bytes);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        PersistentDataContainer container = projectile.getPersistentDataContainer();
        Integer ordinal = container.get(Constants.getEntityTypeKey(), PersistentDataType.INTEGER);
        if(ordinal == null) {
            return;
        }

        EntityType type = EntityType.values()[ordinal];
        byte[] bytes = Objects.requireNonNull(container.get(Constants.getEntityKey(), PersistentDataType.BYTE_ARRAY));

        if(bytes.length == 0) {
            Entity entity = event.getHitEntity();

            SafariNet net = SafariNet.from(entity);
            World world = projectile.getWorld();

            if(entity == null) {
                Block block = event.getHitBlock();
                if(block == null) {
                    throw new IllegalStateException("Block and entity are null");
                }

                Location location = block.getLocation().add(0, 1, 0);
                world.dropItemNaturally(location, net.getItem());
                return;
            }

            // Capturing the Entity

            if(entity.isInsideVehicle()) {
                entity.leaveVehicle();
            }

            List<Entity> passengers = entity.getPassengers();
            if(!passengers.isEmpty()) {
                for(Entity passenger : passengers) {
                    passenger.leaveVehicle();
                }
            }

            Location location = entity.getLocation();
            world.dropItemNaturally(location, net.getItem());
            entity.remove();
            return;
        }

        // Spawning the Entity

        Block block = Objects.requireNonNull(event.getHitBlock());
        World world = block.getWorld();
        Location location = block.getLocation().add(0, 1 ,0);
        LivingEntity living = (LivingEntity) world.spawnEntity(location, type);

        Sound sound = living.getHurtSound();
        if(sound != null) {
            world.playSound(living, sound, 1.0F, 1.0F);
        }

        Entities.deserialize(living, bytes);
    }

}
