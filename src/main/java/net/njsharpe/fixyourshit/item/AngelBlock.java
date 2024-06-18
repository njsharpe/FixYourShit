package net.njsharpe.fixyourshit.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.njsharpe.fixyourshit.Constants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AngelBlock implements ShitItem {

    private static final AngelBlock EMPTY = new AngelBlock();
    public static AngelBlock getEmpty() {
        return EMPTY;
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.OBSIDIAN, 1);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text("Angel Block").style(Style.empty())
                .decoration(TextDecoration.ITALIC, false));

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(Constants.getAngelBlockKey(), PersistentDataType.BYTE, (byte) 1);

        item.setItemMeta(meta);
        return item;
    }

    @Nullable
    public static AngelBlock from(@NotNull ItemStack item) {
        if(item.getType().isAir()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return null;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(!container.has(Constants.getAngelBlockKey(), PersistentDataType.BYTE)) {
            return null;
        }

        return new AngelBlock();
    }

}
