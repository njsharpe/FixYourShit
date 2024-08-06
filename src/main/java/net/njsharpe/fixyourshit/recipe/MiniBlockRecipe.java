package net.njsharpe.fixyourshit.recipe;

import net.njsharpe.fixyourshit.Keys;
import net.njsharpe.fixyourshit.Lookup;
import org.bukkit.Material;
import org.bukkit.inventory.StonecuttingRecipe;
import org.jetbrains.annotations.NotNull;

public class MiniBlockRecipe extends StonecuttingRecipe {

    public MiniBlockRecipe(@NotNull Material source) {
        super(Keys.getMiniBlockKeyForMaterial(source), Lookup.getSkullForMaterial(source), source);
    }

}
