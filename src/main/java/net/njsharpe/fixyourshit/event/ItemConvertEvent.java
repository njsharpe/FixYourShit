package net.njsharpe.fixyourshit.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public sealed class ItemConvertEvent extends Event implements Cancellable
        permits ConcretePowderItemConvertEvent, DirtItemConvertEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NotNull
    // Item before the conversion
    private final Item item;

    private boolean cancelled;

    public ItemConvertEvent(@NotNull Item item) {
        this.item = item;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
