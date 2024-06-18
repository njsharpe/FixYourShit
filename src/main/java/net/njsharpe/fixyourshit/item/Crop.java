package net.njsharpe.fixyourshit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Crop {

    private static final List<Crop> CROPS = new ArrayList<>();

    static {
        CROPS.add(new Crop(Material.WHEAT_SEEDS, Material.WHEAT, Material.WHEAT));
        CROPS.add(new Crop(Material.BEETROOT_SEEDS, Material.BEETROOT, Material.BEETROOTS));
        CROPS.add(new Crop(Material.CARROT, Material.CARROT, Material.CARROTS));
        CROPS.add(new Crop(Material.POTATO, Material.POTATO, Material.POTATOES));
    }

    private final Material seed;
    private final Material result;
    private final Material crop;

    public static List<Crop> getCrops() {
        return CROPS;
    }

}
