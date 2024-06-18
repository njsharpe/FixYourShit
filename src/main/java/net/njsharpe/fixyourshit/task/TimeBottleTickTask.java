package net.njsharpe.fixyourshit.task;

import net.njsharpe.fixyourshit.item.TimeBottle;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class TimeBottleTickTask implements Consumer<BukkitTask> {

    @Override
    public void accept(BukkitTask task) {
        if(!task.isCancelled()) {
            for(World world : Bukkit.getServer().getWorlds()) {
                for(Chunk chunk : world.getLoadedChunks()) {
                    for(BlockState state : chunk.getTileEntities()) {
                        if(!(state instanceof InventoryHolder holder)) {
                            continue;
                        }
                        this.updateInventory(holder);
                    }

                    for(Entity entity : chunk.getEntities()) {
                        if(!(entity instanceof InventoryHolder holder)) {
                            continue;
                        }
                        this.updateInventory(holder);
                    }
                }
            }
        }
    }

    private void updateInventory(InventoryHolder holder) {
        Inventory inventory = holder.getInventory();
        for(ItemStack item : inventory.getContents()) {
            if(item == null) {
                continue;
            }

            TimeBottle bottle = TimeBottle.from(item);
            if(bottle == null) {
                continue;
            }

            long value = bottle.getStoredSeconds();

            if(value < (long) 0) {
                value = 0;
            }

            if(value < Long.MAX_VALUE - 1) {
                value += 1;
            }

            bottle.setStoredSeconds(value);
            bottle.update(item);
        }
    }

}
