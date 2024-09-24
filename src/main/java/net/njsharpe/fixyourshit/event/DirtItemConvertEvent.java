package net.njsharpe.fixyourshit.event;

import org.bukkit.entity.Item;
import org.jetbrains.annotations.NotNull;

public final class DirtItemConvertEvent extends ItemConvertEvent {

    public DirtItemConvertEvent(@NotNull Item item) {
        super(item);
    }

}
