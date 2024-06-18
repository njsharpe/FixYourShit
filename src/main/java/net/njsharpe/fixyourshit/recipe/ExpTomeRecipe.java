package net.njsharpe.fixyourshit.recipe;

import net.njsharpe.fixyourshit.Constants;
import net.njsharpe.fixyourshit.item.ExpTome;
import org.bukkit.Material;
import org.bukkit.inventory.ShapelessRecipe;

public class ExpTomeRecipe extends ShapelessRecipe {

    public ExpTomeRecipe() {
        super(Constants.getExpBookRecipeKey(), ExpTome.getEmpty().getItem());
        this.addIngredient(Material.BOOK);
        this.addIngredient(Material.EXPERIENCE_BOTTLE);
    }

}
