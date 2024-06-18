package net.njsharpe.fixyourshit.recipe;

import net.njsharpe.fixyourshit.Constants;
import net.njsharpe.fixyourshit.item.TimeBottle;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;

public class TimeBottleRecipe extends ShapedRecipe {

    public TimeBottleRecipe() {
        super(Constants.getTimeBottleRecipeKey(), TimeBottle.getEmpty().getItem());
        this.shape("GGG", "DCD", "LBL");
        this.setIngredient('G', Material.GOLD_INGOT);
        this.setIngredient('D', Material.DIAMOND);
        this.setIngredient('C', Material.CLOCK);
        this.setIngredient('L', Material.LAPIS_LAZULI);
        this.setIngredient('B', Material.GLASS_BOTTLE);
    }

}
