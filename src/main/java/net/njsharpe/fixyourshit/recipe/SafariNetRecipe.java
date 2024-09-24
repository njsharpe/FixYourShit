package net.njsharpe.fixyourshit.recipe;

import net.njsharpe.fixyourshit.Constants;
import net.njsharpe.fixyourshit.item.SafariNet;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;

public class SafariNetRecipe extends ShapedRecipe {

    public SafariNetRecipe() {
        super(Constants.getSafariNetRecipeKey(), SafariNet.getEmpty().getItem());
        this.shape(" G ", "GEG", " G ");
        this.setIngredient('G', Material.GHAST_TEAR);
        this.setIngredient('E', Material.ENDER_PEARL);
    }

}
