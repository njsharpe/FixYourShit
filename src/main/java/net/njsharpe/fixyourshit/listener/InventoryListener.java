package net.njsharpe.fixyourshit.listener;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.view.AnvilView;

public class InventoryListener implements Listener {

    @SuppressWarnings({"unused", "UnstableApiUsage"})
    @EventHandler
    public void onAnvilApplyEnchantment(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        AnvilView view = event.getView();

        ItemStack bow = inventory.getFirstItem();
        if(bow == null || bow.getType() != Material.BOW) {
            return;
        }

        ItemStack enchantment = inventory.getSecondItem();
        if(enchantment == null) {
            return;
        }

        int level;
        if(enchantment.getType() != Material.ENCHANTED_BOOK) {
            ItemMeta meta = enchantment.getItemMeta();
            if(!meta.hasEnchant(Enchantment.LOOTING)) {
                return;
            }
            level = meta.getEnchantLevel(Enchantment.LOOTING);
        } else {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantment.getItemMeta();
            if(!meta.hasStoredEnchant(Enchantment.LOOTING)) {
                return;
            }
            level = meta.getStoredEnchantLevel(Enchantment.LOOTING);
        }

        int cost = view.getRepairCost();
        cost += level == 0 ? 0 : Enchantment.LOOTING.getAnvilCost();

        ItemStack result = event.getResult();
        if(result == null) {
            result = new ItemStack(Material.BOW, 1);
        }

        result.addUnsafeEnchantment(Enchantment.LOOTING, level);

        view.setRepairCost(cost);

        event.setResult(result);
    }

}
