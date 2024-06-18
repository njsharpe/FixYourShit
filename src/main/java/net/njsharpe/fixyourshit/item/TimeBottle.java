package net.njsharpe.fixyourshit.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.njsharpe.fixyourshit.Constants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeBottle implements ShitItem, Updatable {

    private static final TimeBottle EMPTY = new TimeBottle(0L);
    public static TimeBottle getEmpty() {
        return EMPTY;
    }

    private long storedSeconds;

    @Override
    @Contract(mutates = "param1")
    public void update(@NotNull ItemStack item) {
        this.update(item, item.getItemMeta());
    }

    @Contract(mutates = "param1")
    private void update(@NotNull ItemStack item, @NotNull ItemMeta meta) {
        long seconds = this.storedSeconds % 60;
        long minutes = (this.storedSeconds % 3600) / 60;
        long hours = this.storedSeconds / 3600;

        Component time = (Component.text("Contains")
                .appendSpace()
                .append(Component.text(hours)))
                .append(Component.text("h"))
                .appendSpace()
                .append(Component.text(minutes))
                .append(Component.text("m"))
                .appendSpace()
                .append(Component.text(seconds))
                .append(Component.text("s"))
                .appendSpace()
                .append(Component.text("of condensed time"))
                .style(Style.empty().color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));

        meta.lore(List.of(time));

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(Constants.getStoredSecondsKey(), PersistentDataType.LONG, this.storedSeconds);

        item.setItemMeta(meta);
    }

    @Override
    @NotNull
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.HONEY_BOTTLE, 1);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text("Time in a Bottle").style(Style.empty())
                .decoration(TextDecoration.ITALIC, false).color(NamedTextColor.LIGHT_PURPLE));

        this.update(item, meta);

        item.setItemMeta(meta);
        return item;
    }

    @Nullable
    public static TimeBottle from(@NotNull ItemStack item) {
        if(item.getType().isAir()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return null;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(!container.has(Constants.getStoredSecondsKey(), PersistentDataType.LONG)) {
            return null;
        }

        long seconds = Objects.requireNonNull(container.get(Constants.getStoredSecondsKey(), PersistentDataType.LONG));
        return new TimeBottle(seconds);
    }

}
