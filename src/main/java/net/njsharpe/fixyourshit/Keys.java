package net.njsharpe.fixyourshit;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.UUID;

public class Keys {

    public static NamespacedKey getBlockKeyForMaterial(Material material) {
        return new NamespacedKey(FixYourShit.getInstance(), "recipe_%s_to_block"
                .formatted(material.name().toLowerCase()));
    }

    public static NamespacedKey getRocketKeyForDuration(int duration) {
        return new NamespacedKey(FixYourShit.getInstance(), "recipe_longer_rocket_%s"
                .formatted(duration));
    }

}
