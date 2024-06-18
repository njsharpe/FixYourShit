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
import net.njsharpe.fixyourshit.entity.Entities;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SafariNet implements ShitItem {

    private static final SafariNet EMPTY = new SafariNet(EntityType.UNKNOWN, new byte[0]);
    public static SafariNet getEmpty() {
        return EMPTY;
    }

    @NotNull
    private EntityType type;

    private byte[] bytes;

    @NotNull
    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.SNOWBALL, 1);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text("Safari Net").style(Style.empty())
                .decoration(TextDecoration.ITALIC, false));

        Component name = (this.type == EntityType.UNKNOWN
                ? Component.text("Empty")
                : Component.translatable(this.type.translationKey(),"Unknown"))
                .style(Style.empty().color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));

        double h = Entities.getHealthFromBytes(this.bytes);
        Component health = (Component.text("Health:").appendSpace().append(Component.text(h)))
                .style(Style.empty().color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));

        meta.lore(Arrays.asList(name, health));

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(Constants.getEntityTypeKey(), PersistentDataType.INTEGER, this.type.ordinal());
        container.set(Constants.getEntityKey(), PersistentDataType.BYTE_ARRAY, this.bytes);

        item.setItemMeta(meta);
        return item;
    }

    @Nullable
    public static SafariNet from(@NotNull ItemStack item) {
        if(item.getType().isAir()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return null;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(!container.has(Constants.getEntityTypeKey(), PersistentDataType.INTEGER)) {
            return null;
        }

        int ordinal = Objects.requireNonNull(container.get(Constants.getEntityTypeKey(), PersistentDataType.INTEGER));
        EntityType type = EntityType.values()[ordinal];
        byte[] bytes = Objects.requireNonNull(container.get(Constants.getEntityKey(), PersistentDataType.BYTE_ARRAY));
        if(bytes.length == 0) {
            return EMPTY;
        }

        return new SafariNet(type, bytes);
    }

    @NotNull
    public static SafariNet from(@Nullable Entity entity) {
        if(!(entity instanceof LivingEntity living)) {
            return EMPTY;
        }

        byte[] bytes = Entities.serialize(living);
        if(bytes.length == 0) {
            return EMPTY;
        }

        return new SafariNet(living.getType(), bytes);
    }

}
