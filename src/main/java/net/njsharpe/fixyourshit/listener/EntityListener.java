package net.njsharpe.fixyourshit.listener;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import net.njsharpe.fixyourshit.Enums;
import net.njsharpe.fixyourshit.Constants;
import net.njsharpe.fixyourshit.FixYourShit;
import net.njsharpe.fixyourshit.entity.Entities;
import net.njsharpe.fixyourshit.event.ConcretePowderItemConvertEvent;
import net.njsharpe.fixyourshit.event.DirtItemConvertEvent;
import net.njsharpe.fixyourshit.item.SafariNet;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.*;

public class EntityListener implements Listener {

    private final float verticalCauldronItemMomentum = 0.4275F;

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

                if(!this.isCauldronUpdated(block)) {
                    return;
                }

                ConcretePowderItemConvertEvent e = new ConcretePowderItemConvertEvent(item);
                Bukkit.getPluginManager().callEvent(e);

                if(e.isCancelled()) {
                    return;
                }

                itemStack.setType(concrete);
                item.setItemStack(itemStack);

                Random random = new Random();

                item.setRotation(0, 0);
                item.setVelocity(new Vector(random.nextFloat(), this.verticalCauldronItemMomentum, random.nextFloat()));
            }, 0L, 5L);
        }
    }

    @SuppressWarnings({"unused", "deprecation"})
    @EventHandler
    public void onDropDirt(ItemSpawnEvent event) {
        Item item = event.getEntity();
        ItemStack itemStack = item.getItemStack();
        Material material = itemStack.getType();

        if(material != Material.DIRT) {
            return;
        }

        Bukkit.getScheduler().runTaskTimer(FixYourShit.getInstance(), task -> {
            if(!item.isInWater() && !item.isOnGround()) {
                return;
            }

            task.cancel();

            Location location = item.getLocation();
            Block block = location.getBlock();

            if(!this.isCauldronUpdated(block)) {
                return;
            }

            DirtItemConvertEvent e = new DirtItemConvertEvent(item);
            Bukkit.getPluginManager().callEvent(e);

            if(e.isCancelled()) {
                return;
            }

            itemStack.setType(Material.MUD);
            item.setItemStack(itemStack);

            Random random = new Random();

            item.setRotation(0, 0);
            item.setVelocity(new Vector(random.nextFloat(), this.verticalCauldronItemMomentum, random.nextFloat()));
        }, 0L, 5L);
    }

    private boolean isCauldronUpdated(Block block) {
        if(!block.getType().equals(Material.WATER_CAULDRON)) {
            return false;
        }

        Levelled levelled = (Levelled) block.getBlockData();
        if(levelled.getLevel() > levelled.getMinimumLevel()) {
            levelled.setLevel(levelled.getLevel() - 1);
            block.setBlockData(levelled);
            return true;
        }

        if(levelled.getLevel() == levelled.getMinimumLevel()) {
            block.setType(Material.CAULDRON);
            return true;
        }

        return false;
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

    @SuppressWarnings({"unused", "UnstableApiUsage"})
    @EventHandler
    public void onEntityDeathByArrow(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        DamageSource source = event.getDamageSource();
        if(source.getDamageType() != DamageType.ARROW) {
            return;
        }

        Entity cause = source.getCausingEntity();
        if(!(cause instanceof Player player)) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        if(item.getType() != Material.BOW) {
            return;
        }

        if(!item.containsEnchantment(Enchantment.LOOTING)) {
            return;
        }

        int level = item.getEnchantmentLevel(Enchantment.LOOTING);

        World world = player.getWorld();

        Random random = new Random();
        LootContext.Builder builder = new LootContext.Builder(entity.getLocation())
                .killer(player)
                .lootedEntity(entity);

        LootContext context = builder.build();

        Collection<ItemStack> items = Enums.getIfPresent(LootTables.class, entity.getType().name())
                .map(lootTable -> {
                    LootTable table = lootTable.getLootTable();
                    return table.populateLoot(random, context);
                })
                .orElse(new ArrayList<>());

        event.getDrops().clear();
        event.getDrops().addAll(items);
    }

}
