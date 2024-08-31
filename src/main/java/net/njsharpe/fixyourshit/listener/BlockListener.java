package net.njsharpe.fixyourshit.listener;

import net.njsharpe.fixyourshit.FixYourShit;
import net.njsharpe.fixyourshit.event.CropTrampleEvent;
import net.njsharpe.fixyourshit.event.ReplantCropEvent;
import net.njsharpe.fixyourshit.item.ArmoredElytra;
import net.njsharpe.fixyourshit.item.Crop;
import net.njsharpe.fixyourshit.item.TimeBottle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.view.AnvilView;

import java.util.*;

public class BlockListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack hand = inventory.getItemInMainHand();

        if(!hand.getType().name().endsWith("_HOE")) {
            return;
        }

        List<Crop> crops = Crop.getCrops();

        Block block = event.getBlock();
        Material type = block.getType();

        Optional<Crop> optional = crops.stream().filter(c -> c.getCrop() == type).findAny();
        if(optional.isEmpty()) {
            return;
        }

        Crop crop = optional.get();

        if(!inventory.contains(crop.getSeed(), 1)) {
            return;
        }

        ReplantCropEvent e = new ReplantCropEvent(block, crop);
        Bukkit.getPluginManager().callEvent(e);

        if(e.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTaskLater(FixYourShit.getInstance(), () ->
        {
            block.setType(crop.getCrop());
            int slot = inventory.first(crop.getSeed());
            ItemStack item = Objects.requireNonNull(inventory.getItem(slot));
            inventory.setItem(slot, item.subtract());
        }, 1L);
    }

    @SuppressWarnings({"unused", "UnstableApiUsage"})
    @EventHandler
    public void onUseNetherStarInAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        AnvilView view = event.getView();

        ItemStack first = inventory.getFirstItem();
        ItemStack second = inventory.getSecondItem();

        if(first == null || first.getEnchantments().isEmpty()) {
            return;
        }

        if(second == null || second.getType() != Material.NETHER_STAR) {
            return;
        }

        ItemMeta meta = first.getItemMeta();
        if(!(meta instanceof Repairable repairable)) {
            return;
        }

        repairable.setRepairCost(0);
        first.setItemMeta(meta);

        view.setRepairCost(1);
        event.setResult(first);
    }

    @SuppressWarnings({"unused", "UnstableApiUsage"})
    @EventHandler
    public void onUseElytraInAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        AnvilView view = event.getView();

        ItemStack first = inventory.getFirstItem();
        ItemStack second = inventory.getSecondItem();

        if(first == null || !first.getType().name().endsWith("_CHESTPLATE")) {
            return;
        }

        if(second == null || second.getType() != Material.ELYTRA) {
            return;
        }

        ArmoredElytra result = ArmoredElytra.merge(first, second);

        view.setRepairCost(1);
        event.setResult(result.getItem());
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPrepareCraftItem(CraftItemEvent event) {
        CraftingInventory inventory = event.getInventory();

        boolean hasBottle = false;
        for(ItemStack item : inventory.getMatrix()) {
            if(item == null) {
                continue;
            }

            TimeBottle bottle = TimeBottle.from(item);
            if(bottle != null) {
                hasBottle = true;
            }
        }

        if(hasBottle) {
            inventory.setResult(null);
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onCropTrample(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();

        Block block = event.getBlock();
        Block plant = block.getRelative(BlockFace.UP);

        Optional<Crop> optional = Crop.getCrops().stream()
                .filter(c -> c.getCrop().equals(plant.getType()))
                .findFirst();

        if(optional.isEmpty()) {
            return;
        }

        Crop crop = optional.get();

        CropTrampleEvent e = new CropTrampleEvent(entity, crop, block);
        Bukkit.getPluginManager().callEvent(e);

        if(e.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        if(!(entity instanceof LivingEntity living)) {
            return;
        }

        EntityEquipment equipment = living.getEquipment();
        if(equipment == null) {
            return;
        }

        ItemStack boots = equipment.getItem(EquipmentSlot.FEET);
        if(boots.isEmpty()) {
           return;
        }

        Map<Enchantment, Integer> enchantments = boots.getEnchantments();
        if(enchantments.containsKey(Enchantment.FEATHER_FALLING)) {
            int level = enchantments.get(Enchantment.FEATHER_FALLING);
            float height = (level * 1.35F) + 2.0F;
            float distance = entity.getFallDistance();
            if(height >= distance) {
                event.setCancelled(true);
            }
        }
    }

}
