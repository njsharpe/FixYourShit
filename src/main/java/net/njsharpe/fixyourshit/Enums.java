package net.njsharpe.fixyourshit;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Enums {

    public static <T extends Enum<T>> Optional<T> getIfPresent(@NotNull Class<T> clazz, @NotNull String name) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotNull(name);
        try {
            return Optional.of(Enum.valueOf(clazz, name));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

}
