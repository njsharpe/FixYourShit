package net.njsharpe.fixyourshit.item;

import com.google.common.collect.Multimap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.function.Predicate;

public class Attributes {

    public static Double getAttributeSum(Multimap<Attribute, AttributeModifier> modifiers, Attribute type, Predicate<AttributeModifier> predicate) {
        return modifiers.get(type).stream()
                .filter(predicate)
                .reduce(0.0D, (x, a) -> a.getAmount(), Double::sum);
    }

}
