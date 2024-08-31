package net.njsharpe.fixyourshit.item;

import com.google.common.collect.Multimap;
import lombok.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.njsharpe.fixyourshit.Constants;
import net.njsharpe.fixyourshit.Maker;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ArmoredElytra implements ShitItem {

    private static final ArmoredElytra EMPTY = new ArmoredElytra(null, null, null, null);
    public static ArmoredElytra getEmpty() {
        return EMPTY;
    }

    @Nullable
    private final Double armor;

    @Nullable
    private final Double toughness;

    @Nullable
    private final Double knockbackResistance;

    @Nullable
    private final Material sourceArmorMaterial;

    private Map<Enchantment, Integer> enchantments;

    public ArmoredElytra(@Nullable Material sourceArmorMaterial) {
        this(null, null, null, sourceArmorMaterial);
    }

    @SuppressWarnings("UnstableApiUsage")
    @NotNull
    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.ELYTRA, 1);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text("Armored Elytra").style(Style.empty())
                .decoration(TextDecoration.ITALIC, false));

        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(this.sourceArmorMaterial != null) {
            container.set(Constants.getEntityTypeKey(), PersistentDataType.INTEGER, this.sourceArmorMaterial.ordinal());
        }

        Component spacer = Component.text("");

        Component name = (this.sourceArmorMaterial == null || this.sourceArmorMaterial.isAir()
                ? Component.text("Empty")
                : Component.translatable(this.sourceArmorMaterial.translationKey(),"Unknown"))
                .style(Style.empty().color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));

        meta.lore(this.enchantments.isEmpty() ? List.of(name) : List.of(spacer, name));

        if(this.armor != null) {
            AttributeModifier armor = new AttributeModifier(Constants.getArmoredElytraArmorModifierKey(),
                    this.armor, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor);
        }

        if(this.toughness != null) {
            AttributeModifier toughness = new AttributeModifier(Constants.getArmoredElytraToughnessModifierKey(),
                    this.toughness, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughness);
        }

        if(this.knockbackResistance != null) {
            AttributeModifier knockbackResistance = new AttributeModifier(Constants.getArmoredElytraKnockbackResistanceModifierKey(),
                    this.knockbackResistance, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST);
            meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockbackResistance);
        }

        item.setItemMeta(meta);

        item.addUnsafeEnchantments(this.enchantments);

        return item;
    }

    @NotNull
    public static ArmoredElytra merge(@NotNull ItemStack sourceArmor, @NotNull ItemStack elytra) {
        if(sourceArmor.getType().isAir()) {
            return ArmoredElytra.getEmpty();
        }

        Material sourceArmorType = sourceArmor.getType();

        Map<Enchantment, Integer> enchantments = new HashMap<>(sourceArmor.getEnchantments());
        for(Map.Entry<Enchantment, Integer> entry : elytra.getEnchantments().entrySet()) {
            if(enchantments.containsKey(entry.getKey())) {
                enchantments.compute(entry.getKey(), (k, level) ->
                        Math.max(level == null ? 1 : level, entry.getValue()));
                continue;
            }
            enchantments.put(entry.getKey(), entry.getValue());
        }

        ItemMeta meta = sourceArmor.getItemMeta();
        if(meta == null) {
            return Maker.make(new ArmoredElytra(sourceArmorType), ae ->
                ae.enchantments = enchantments);
        }

        Multimap<Attribute, AttributeModifier> modifiers = sourceArmorType.getDefaultAttributeModifiers(EquipmentSlot.CHEST);

        Double armor = Attributes.getAttributeSum(modifiers, Attribute.GENERIC_ARMOR,
                a -> true);

        Double toughness = Attributes.getAttributeSum(modifiers, Attribute.GENERIC_ARMOR_TOUGHNESS,
                a -> true);

        Double knockbackResistance = Attributes.getAttributeSum(modifiers, Attribute.GENERIC_KNOCKBACK_RESISTANCE,
                a -> true);

        return Maker.make(new ArmoredElytra(armor, toughness, knockbackResistance, sourceArmor.getType()), ae ->
                ae.enchantments = enchantments);
    }

}
