package net.njsharpe.fixyourshit.recipe;

import net.njsharpe.fixyourshit.Constants;
import net.njsharpe.fixyourshit.item.AngelBlock;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;

public class AngelBlockRecipe extends ShapedRecipe {

    public AngelBlockRecipe() {
        super(Constants.getAngelBlockRecipeKey(), AngelBlock.getEmpty().getItem());
        this.shape("   ", " G ", "FOF");
        this.setIngredient('G', Material.GOLD_INGOT);
        this.setIngredient('F', Material.FEATHER);
        this.setIngredient('O', Material.OBSIDIAN);
    }

}
