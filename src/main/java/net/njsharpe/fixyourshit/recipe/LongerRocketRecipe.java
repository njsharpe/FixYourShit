package net.njsharpe.fixyourshit.recipe;

import net.njsharpe.fixyourshit.Keys;
import net.njsharpe.fixyourshit.Maker;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LongerRocketRecipe extends ShapelessRecipe {

    private static final Map<Integer, Collection<Material>> DURATION_TO_MATERIALS_MAP = new HashMap<>();

    static {
        DURATION_TO_MATERIALS_MAP.put(4, List.of(Material.GUNPOWDER, Material.GUNPOWDER, Material.GUNPOWDER, Material.GUNPOWDER));
        DURATION_TO_MATERIALS_MAP.put(5, List.of(Material.TNT));
        DURATION_TO_MATERIALS_MAP.put(6, List.of(Material.TNT, Material.GUNPOWDER));
        DURATION_TO_MATERIALS_MAP.put(7, List.of(Material.TNT, Material.GUNPOWDER, Material.GUNPOWDER));
        DURATION_TO_MATERIALS_MAP.put(8, List.of(Material.TNT, Material.GUNPOWDER, Material.GUNPOWDER, Material.GUNPOWDER));
        DURATION_TO_MATERIALS_MAP.put(9, List.of(Material.TNT, Material.GUNPOWDER, Material.GUNPOWDER, Material.GUNPOWDER, Material.GUNPOWDER));
        DURATION_TO_MATERIALS_MAP.put(10, List.of(Material.TNT, Material.TNT));
        DURATION_TO_MATERIALS_MAP.put(11, List.of(Material.TNT, Material.TNT, Material.GUNPOWDER));
        DURATION_TO_MATERIALS_MAP.put(12, List.of(Material.TNT, Material.TNT, Material.GUNPOWDER, Material.GUNPOWDER));
        DURATION_TO_MATERIALS_MAP.put(13, List.of(Material.TNT, Material.TNT, Material.GUNPOWDER, Material.GUNPOWDER, Material.GUNPOWDER));
        DURATION_TO_MATERIALS_MAP.put(14, List.of(Material.TNT, Material.TNT, Material.GUNPOWDER, Material.GUNPOWDER, Material.GUNPOWDER, Material.GUNPOWDER));
        DURATION_TO_MATERIALS_MAP.put(15, List.of(Material.TNT, Material.TNT, Material.TNT));
    }

    public LongerRocketRecipe(int duration) {
        super(Keys.getRocketKeyForDuration(duration), Maker.make(new ItemStack(Material.FIREWORK_ROCKET, 3), (item) -> {
            ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
            FireworkMeta firework = (FireworkMeta) meta;
            firework.setPower(duration);
            item.setItemMeta(firework);
        }));

        if(duration <= 3 || duration > 15) {
            throw new IllegalArgumentException("Duration of %d not allowed!".formatted(duration));
        }

        this.addIngredient(Material.PAPER);
        Collection<Material> materials = DURATION_TO_MATERIALS_MAP.get(duration);
        materials.forEach(this::addIngredient);
    }

}
