package net.njsharpe.fixyourshit.event;

import org.bukkit.entity.Item;
import org.jetbrains.annotations.NotNull;

public final class ConcretePowderItemConvertEvent extends ItemConvertEvent {

    public ConcretePowderItemConvertEvent(@NotNull Item item) {
        super(item);
    }

}
