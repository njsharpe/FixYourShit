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
public class ConcretePowderItemConvertEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NotNull
    private final Item item;

    private boolean cancelled;

    public ConcretePowderItemConvertEvent(@NotNull Item item) {
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
