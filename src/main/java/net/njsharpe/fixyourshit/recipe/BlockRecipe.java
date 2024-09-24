package net.njsharpe.fixyourshit.recipe;

import net.njsharpe.fixyourshit.Keys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class BlockRecipe extends ShapedRecipe {

    public BlockRecipe(Material slab, ItemStack result) {
        super(Keys.getBlockKeyForMaterial(slab), result);
        this.shape("S ", "S ");
        this.setIngredient('S', slab);
    }

}
