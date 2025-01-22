package net.njsharpe.fixyourshit;

import lombok.Getter;
import net.njsharpe.fixyourshit.listener.*;
import net.njsharpe.fixyourshit.recipe.*;
import net.njsharpe.fixyourshit.task.TimeBottleTickTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.IntStream;

public class FixYourShit extends JavaPlugin {

    @Getter
    private static FixYourShit instance;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        this.getServer().addRecipe(new ExpTomeRecipe());
        this.getServer().addRecipe(new SafariNetRecipe());
        this.getServer().addRecipe(new TimeBottleRecipe());
        this.getServer().addRecipe(new AngelBlockRecipe());
        IntStream.range(4, 16).forEach(i -> this.getServer().addRecipe(new LongerRocketRecipe(i)));
        Lookup.getSkullMaterials().forEach(m -> this.getServer().addRecipe(new MiniBlockRecipe(m)));
        SlabRecipes.init();

        this.getServer().getPluginManager().registerEvents(new EntityListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        this.getServer().getScheduler().runTaskTimer(this, new TimeBottleTickTask(), 0L, 20L);
    }

    @Override
    public void onDisable() {
        instance = null;
        super.onDisable();
    }

}
