package net.njsharpe.fixyourshit;

import org.bukkit.NamespacedKey;

import java.util.logging.Logger;

public class Constants {

    private final static String CONCRETE_POWDER_TYPE_ENDING = "_CONCRETE_POWDER";
    public static String getConcretePowderTypeEnding() {
        return CONCRETE_POWDER_TYPE_ENDING;
    }

    private static final String MATERIAL_NAME_SLAB_ENDING = "_SLAB";
    public static String getMaterialNameSlabEnding() {
        return MATERIAL_NAME_SLAB_ENDING;
    }

    private final static String HOE_TYPE_ENDING = "_HOE";
    public static String getHoeTypeEnding() {
        return HOE_TYPE_ENDING;
    }

    private final static NamespacedKey ENTITY_TYPE_KEY = new NamespacedKey(FixYourShit.getInstance(), "entity_type");
    public static NamespacedKey getEntityTypeKey() {
        return ENTITY_TYPE_KEY;
    }

    private final static NamespacedKey ENTITY_KEY = new NamespacedKey(FixYourShit.getInstance(), "entity");
    public static NamespacedKey getEntityKey() {
        return ENTITY_KEY;
    }

    private final static NamespacedKey STORED_EXP_KEY = new NamespacedKey(FixYourShit.getInstance(), "stored_exp");
    public static NamespacedKey getStoredExpKey() {
        return STORED_EXP_KEY;
    }

    private final static NamespacedKey STORED_LEVEL_KEY = new NamespacedKey(FixYourShit.getInstance(), "stored_level");
    public static NamespacedKey getStoredLevelKey() {
        return STORED_LEVEL_KEY;
    }

    private final static NamespacedKey EXP_BOOK_RECIPE_KEY = new NamespacedKey(FixYourShit.getInstance(), "recipe_exp_book");
    public static NamespacedKey getExpBookRecipeKey() {
        return EXP_BOOK_RECIPE_KEY;
    }

    private final static NamespacedKey SAFARI_NET_RECIPE_KEY = new NamespacedKey(FixYourShit.getInstance(), "recipe_safari_net");
    public static NamespacedKey getSafariNetRecipeKey() {
        return SAFARI_NET_RECIPE_KEY;
    }

    private final static NamespacedKey STORED_SECONDS_KEY = new NamespacedKey(FixYourShit.getInstance(), "stored_seconds");
    public static NamespacedKey getStoredSecondsKey() {
        return STORED_SECONDS_KEY;
    }

    private final static NamespacedKey TIME_BOTTLE_RECIPE_KEY = new NamespacedKey(FixYourShit.getInstance(), "recipe_time_bottle");
    public static NamespacedKey getTimeBottleRecipeKey() {
        return TIME_BOTTLE_RECIPE_KEY;
    }

    private final static NamespacedKey ANGEL_BLOCK_KEY = new NamespacedKey(FixYourShit.getInstance(), "angel_block");
    public static NamespacedKey getAngelBlockKey() {
        return ANGEL_BLOCK_KEY;
    }

    private final static NamespacedKey ANGEL_BLOCK_RECIPE_KEY = new NamespacedKey(FixYourShit.getInstance(), "recipe_angel_block");
    public static NamespacedKey getAngelBlockRecipeKey() {
        return ANGEL_BLOCK_RECIPE_KEY;
    }

    private final static NamespacedKey ARMORED_ELYTRA_ARMOR_MODIFIER_KEY = new NamespacedKey(FixYourShit.getInstance(), "modifier_armor_armored_elytra");
    public static NamespacedKey getArmoredElytraArmorModifierKey() {
        return ARMORED_ELYTRA_ARMOR_MODIFIER_KEY;
    }

    private final static NamespacedKey ARMORED_ELYTRA_TOUGHNESS_MODIFIER_KEY = new NamespacedKey(FixYourShit.getInstance(), "modifier_toughness_armored_elytra");
    public static NamespacedKey getArmoredElytraToughnessModifierKey() {
        return ARMORED_ELYTRA_TOUGHNESS_MODIFIER_KEY;
    }

    private final static NamespacedKey ARMORED_ELYTRA_KNOCKBACK_RESISTANCE_MODIFIER_KEY = new NamespacedKey(FixYourShit.getInstance(), "modifier_knockback_resistance_armored_elytra");
    public static NamespacedKey getArmoredElytraKnockbackResistanceModifierKey() {
        return ARMORED_ELYTRA_KNOCKBACK_RESISTANCE_MODIFIER_KEY;
    }

    private final static Logger LOGGER = FixYourShit.getInstance().getLogger();
    public static Logger getLogger() {
        return LOGGER;
    }

}
