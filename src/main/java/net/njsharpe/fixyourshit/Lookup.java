package net.njsharpe.fixyourshit;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Lookup {

    private static final Map<Material, ItemStack> MATERIAL_SKULL_MAP = new HashMap<>();

    static {
        MATERIAL_SKULL_MAP.put(Material.ACACIA_LOG, getSkullFromTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjY3ZTljNzRhYjc3YzAwOTE1NGE5YzczNzg0NmI1MjUxMDliOGMzMTdhNzE2Y2FlZGVjOTI3MDJhZmQwZGU2NSJ9fX0="));
        MATERIAL_SKULL_MAP.put(Material.ACACIA_PLANKS, getSkullFromTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzAxNjk2NmY0ZjNkYWU2NTI0ODYxZTZjYzU2MzE3NDk5MDcwMWJlYWUyNjI3NzEyNzE4YzUxMGYzMzNjNmM4MyJ9fX0="));
        MATERIAL_SKULL_MAP.put(Material.AMETHYST_BLOCK, getSkullFromTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEzYjc3ZjVhNzQ2YzFmMDBkZDFjZTdmZTY2OTc2Yzc1Y2VjZjdkODliZGIwMmU4Y2Y5NDM2NjcyYWY1ODk2ZCJ9fX0="));
        MATERIAL_SKULL_MAP.put(Material.ANCIENT_DEBRIS, getSkullFromTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTcxMTRmNWQzOTAxODhkZjA0NzdjZGY5YWVjZjViYzgxNDE2Y2U1ZTVjNTljZmNhYzU4MWE0M2YzOTAyYzFlIn19fQ=="));
        MATERIAL_SKULL_MAP.put(Material.ANDESITE, getSkullFromTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM1ZTE2Yzc2MWUwYWFhMmRkNTI4OWU0M2Y1MmNjNDcxNTY3Y2Q4ZjhjOGE0NzVhNGIyOTBhZWU4Y2ZhNDUzOCJ9fX0="));
        MATERIAL_SKULL_MAP.put(Material.AZALEA_LEAVES, getSkullFromTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGRhNjI1ZGNmYmZiODc1NTZmZTk0NjI3MWQ4YWIwMWFhZjAyYmE2ODFmNTY3MzcyZDA1NjI4NmI3YTAyYmIxZiJ9fX0="));
    }

    private static ItemStack getSkullFromTexture(String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 2);

        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text("Mini")
                .appendSpace()
                .append(Component.translatable(Material.OAK_LOG))
                .style(b -> b.color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false)));

        SkullMeta skull = (SkullMeta) meta;
        PlayerProfile profile = new CraftPlayerProfile(null, null);
        profile.setProperty(new ProfileProperty("textures", texture));
        skull.setPlayerProfile(profile);

        item.setItemMeta(skull);

        return item;
    }

    public static Set<Material> getSkullMaterials() {
        return MATERIAL_SKULL_MAP.keySet();
    }

    @NotNull
    public static ItemStack getSkullForMaterial(@NotNull Material material) {
        return MATERIAL_SKULL_MAP.getOrDefault(material, new ItemStack(Material.PLAYER_HEAD));
    }

}
