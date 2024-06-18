package net.njsharpe.fixyourshit;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Maker {

    public static <T> T make(Supplier<T> supplier) {
        return supplier.get();
    }

    public static <T> T make(T t, Consumer<T> consumer) {
        consumer.accept(t);
        return t;
    }

}
