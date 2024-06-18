package net.njsharpe.fixyourshit.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Updatable {

    @Contract(mutates = "param1")
    void update(@NotNull ItemStack item);

}
