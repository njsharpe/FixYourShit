package net.njsharpe.fixyourshit.event;

import lombok.Getter;
import lombok.Setter;
import net.njsharpe.fixyourshit.item.Crop;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ReplantCropEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NotNull
    private final Block block;

    @NotNull
    private final Crop crop;

    private boolean cancelled;

    public ReplantCropEvent(@NotNull Block block, @NotNull Crop crop) {
        this.block = block;
        this.crop = crop;
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
