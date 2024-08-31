package net.njsharpe.fixyourshit.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Entities {

    public static byte[] serialize(LivingEntity entity) {
        CompoundTag tag = new CompoundTag();
        net.minecraft.world.entity.LivingEntity living = ((CraftLivingEntity) entity).getHandle();
        living.serializeEntity(tag);

        tag.remove("UUID");
        tag.remove("UUIDMost");
        tag.remove("UUIDLeast");

        try(ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(stream)) {

            tag.write(output);
            output.flush();

            return stream.toByteArray();
        } catch (Exception ex) {
            return new byte[0];
        }
    }

    public static void deserialize(LivingEntity entity, byte[] bytes) {
        try(ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream input = new DataInputStream(stream)) {

            net.minecraft.world.entity.LivingEntity living = ((CraftLivingEntity) entity).getHandle();
            CompoundTag tag = CompoundTag.TYPE.load(input, NbtAccounter.unlimitedHeap());

            Location location = entity.getLocation();

            ListTag pos = tag.getList("Pos", 6);
            pos.set(0, DoubleTag.valueOf(location.getX()));
            pos.set(1, DoubleTag.valueOf(location.getY()));
            pos.set(2, DoubleTag.valueOf(location.getZ()));
            tag.put("Pos", pos);
            tag.remove("Dimension");

            living.load(tag);

        } catch (Exception ex) {
            throw new RuntimeException("Could not deserialize entity", ex);
        }
    }

    public static double getHealthFromBytes(byte[] bytes) {
        try(ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream input = new DataInputStream(stream)) {

            CompoundTag tag = CompoundTag.TYPE.load(input, NbtAccounter.unlimitedHeap());

            return tag.getDouble("Health");
        } catch (Exception ex) {
            return 0.0D;
        }
    }

}
