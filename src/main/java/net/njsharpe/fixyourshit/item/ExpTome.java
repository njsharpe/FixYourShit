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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpTome implements ShitItem, Updatable {

    private static final ExpTome EMPTY = new ExpTome(0, 0.0F);
    public static ExpTome getEmpty() {
        return EMPTY;
    }

    @Range(from = (long) 0, to = (long) Integer.MAX_VALUE)
    private int storedLevel;

    @Range(from = (long) 0.0F, to = (long) Float.MAX_VALUE)
    private float storedExp;

    @Override
    @Contract(mutates = "param1")
    public void update(@NotNull ItemStack item) {
        this.update(item, item.getItemMeta());
    }

    @Contract(mutates = "param1")
    private void update(@NotNull ItemStack item, @NotNull ItemMeta meta) {
        List<Component> lore = List.of(
                Component.text("Stored XP: %.2f".formatted(this.storedLevel + this.storedExp))
                        .style(Style.empty().decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY)),
                Component.text("Shwump. Fwump.")
                        .style(Style.empty().decoration(TextDecoration.ITALIC, false).color(NamedTextColor.DARK_GRAY))
        );
        meta.lore(lore);

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(Constants.getStoredLevelKey(), PersistentDataType.INTEGER, this.storedLevel);
        container.set(Constants.getStoredExpKey(), PersistentDataType.FLOAT, this.storedExp);

        item.setItemMeta(meta);
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text("Exp Tome").style(Style.empty())
                .decoration(TextDecoration.ITALIC, false));

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        this.update(item, meta);

        item.addUnsafeEnchantment(Enchantment.MENDING, 1);

        return item;
    }

    @Nullable
    public static ExpTome from(@NotNull ItemStack item) {
        if(item.getType().isAir()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return null;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(!container.has(Constants.getStoredLevelKey(), PersistentDataType.INTEGER)) {
            return null;
        }

        if(!container.has(Constants.getStoredExpKey(), PersistentDataType.FLOAT)) {
            return null;
        }

        int storedLevel = Objects.requireNonNull(container.get(Constants.getStoredLevelKey(), PersistentDataType.INTEGER));
        float storedExp = Objects.requireNonNull(container.get(Constants.getStoredExpKey(), PersistentDataType.FLOAT));

        return new ExpTome(storedLevel, storedExp);
    }

}
