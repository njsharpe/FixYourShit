package net.njsharpe.fixyourshit.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.njsharpe.fixyourshit.item.Crop;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class CropTrampleEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NotNull
    private final Entity entity;

    @NotNull
    private final Crop crop;

    @NotNull
    private final Block block;

    private boolean cancelled;

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
