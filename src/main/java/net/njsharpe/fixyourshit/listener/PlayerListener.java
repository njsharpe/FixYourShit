package net.njsharpe.fixyourshit.listener;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.njsharpe.fixyourshit.FixYourShit;
import net.njsharpe.fixyourshit.item.AngelBlock;
import net.njsharpe.fixyourshit.item.ExpTome;
import net.njsharpe.fixyourshit.item.TimeBottle;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

public class PlayerListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerUseExpTome(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(!event.getAction().isRightClick()) {
            return;
        }

        if(event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack hand = inventory.getItemInMainHand();

        ExpTome tome = ExpTome.from(hand);
        if(tome == null) {
            return;
        }

        event.setCancelled(true);

        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        World world = player.getWorld();

        if(player.isSneaking()) {
            int level = player.getLevel();
            float exp = player.getExp();
            tome.setStoredLevel(tome.getStoredLevel() + level);
            tome.setStoredExp(tome.getStoredExp() + exp);
            player.setLevel(0);
            player.setExp(0.0F);

            world.playSound(player, Sound.ENTITY_PUFFER_FISH_BLOW_UP, 1.0F, 1.0F);
        } else {
            int level = tome.getStoredLevel();
            float exp = tome.getStoredExp();
            tome.setStoredLevel(0);
            tome.setStoredExp(0.0F);
            player.setLevel(player.getLevel() + level);
            player.setExp(player.getExp() + exp);

            world.playSound(player, Sound.ENTITY_PUFFER_FISH_BLOW_OUT, 1.0F, 1.0F);
        }

        tome.update(hand);
    }

    @ApiStatus.Internal
    private static final Map<Block, BukkitTask> BLOCK_TASK_MAP = new HashMap<>();

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerUseTimeBottle(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getHand() == null || !event.getHand().isHand()) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack hand = inventory.getItemInMainHand();

        TimeBottle bottle = TimeBottle.from(hand);
        if(bottle == null) {
            return;
        }

        event.setCancelled(true);

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if(!player.isSneaking()) {
            return;
        }

        Block block = Objects.requireNonNull(event.getClickedBlock());
        BlockState state = block.getState();

        double multiplier;
        switch (block.getType()) {
            case FURNACE -> {
                Furnace furnace = (Furnace) state;
                multiplier = this.handleFurnace(furnace, player, bottle);
            }
            case SMOKER -> {
                Smoker smoker = (Smoker) state;
                multiplier = this.handleFurnace(smoker, player, bottle);
            }
            case BLAST_FURNACE -> {
                BlastFurnace blastFurnace = (BlastFurnace) state;
                multiplier = this.handleFurnace(blastFurnace, player, bottle);
            }
            default -> {
                return;
            }
        }

        World world = block.getWorld();

        if(multiplier > 0) {
            Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(FixYourShit.getInstance(), task -> {
                Material[] furnaces = new Material[] { Material.FURNACE, Material.SMOKER, Material.BLAST_FURNACE };
                if(Arrays.stream(furnaces).noneMatch(m -> block.getType() == m) || !BLOCK_TASK_MAP.containsKey(block)) {
                    task.cancel();
                }

                if(!task.isCancelled()) {
                    world.spawnParticle(Particle.WAX_ON, block.getLocation().toCenterLocation(),
                            16, 0.5D, 0.5D, 0.5D);
                }
            }, 0L, 10L);
        }

        bottle.setStoredSeconds(bottle.getStoredSeconds() - (30L * (int) multiplier));
        bottle.update(hand);
    }

    private double handleFurnace(Furnace furnace, Player player, TimeBottle bottle) {
        Block block = furnace.getBlock();

        double multiplier = this.getMultiplier(furnace, bottle);
        // Stop execution early
        if(multiplier == 0) {
            return 0;
        }

        furnace.setCookSpeedMultiplier(multiplier);
        furnace.update();

        this.playSound(furnace, multiplier);
        this.sendMultiplierIncreaseMessage(block, player, multiplier);

        if(BLOCK_TASK_MAP.containsKey(block)) {
            BLOCK_TASK_MAP.get(block).cancel();
            BLOCK_TASK_MAP.remove(block);
        }

        BukkitTask task = Bukkit.getServer().getScheduler().runTaskLater(FixYourShit.getInstance(), () -> {
            furnace.setCookSpeedMultiplier(1);
            furnace.update();
            BLOCK_TASK_MAP.remove(block);
        }, (long) 20 * (30 + (int) multiplier));

        BLOCK_TASK_MAP.put(block, task);
        return multiplier;
    }

    private double getMultiplier(Furnace furnace, TimeBottle bottle) {
        double multiplier = furnace.getCookSpeedMultiplier();
        int cost = (int) multiplier * 30;
        if(bottle.getStoredSeconds() < cost) {
            return 0;
        }
        return Math.min(multiplier * 2, 128.0D);
    }

    private static final Map<Double, Note> MULTIPLIER_NOTE_MAP = new HashMap<>();
    static {
        MULTIPLIER_NOTE_MAP.put(2.0D, Note.natural(0, Note.Tone.G));
        MULTIPLIER_NOTE_MAP.put(4.0D, Note.natural(0, Note.Tone.A));
        MULTIPLIER_NOTE_MAP.put(8.0D, Note.natural(0, Note.Tone.B));
        MULTIPLIER_NOTE_MAP.put(16.0D, Note.natural(0, Note.Tone.C));
        MULTIPLIER_NOTE_MAP.put(32.0D, Note.natural(0, Note.Tone.D));
        MULTIPLIER_NOTE_MAP.put(64.0D, Note.natural(0, Note.Tone.E));
        MULTIPLIER_NOTE_MAP.put(128.0D, Note.natural(0, Note.Tone.F));
    }

    private void playSound(Furnace furnace, double multiplier) {
        Note note = Objects.requireNonNullElse(MULTIPLIER_NOTE_MAP.get(multiplier), Note.natural(0, Note.Tone.C));
        World world = furnace.getWorld();
        world.playNote(furnace.getLocation(), Instrument.BELL, note);
    }

    private void sendMultiplierIncreaseMessage(Block block, Audience audience, double multiplier) {
        Material material = block.getType();
        String name = material.name();

        Component message = (Component.text(name)
                .appendSpace()
                .append(Component.text("has a multiplier of"))
                .appendSpace()
                .append(Component.text((int) multiplier))
                .append(Component.text("x")))
                .style(Style.empty().color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));

        audience.sendMessage(message);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        if(event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        ItemStack hand = player.getInventory().getItem(event.getHand());
        AngelBlock angel = AngelBlock.from(hand);
        if(angel == null) {
            return;
        }

        World world = player.getWorld();
        Block block = world.getBlockAt((int) player.getX(), (int) (player.getY() - 0.5D), (int) player.getZ());
        if(!block.getType().isAir()) {
            return;
        }

        block.setType(Material.OBSIDIAN);
        if(player.getGameMode() != GameMode.CREATIVE) {
            hand.subtract();
        }
    }

}
