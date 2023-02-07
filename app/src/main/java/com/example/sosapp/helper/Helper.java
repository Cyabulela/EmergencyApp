package com.example.sosapp.helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public final class Helper implements Comparable<Object>{
    @NotNull
    private String name;
    @NotNull
    private String number;

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.name = var1;
    }

    @NotNull
    public String getNumber() {
        return this.number;
    }

    public void setNumber(@NotNull String var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.number = var1;
    }

    public Helper(@NotNull String name, @NotNull String number) {
        super();
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(number, "number");
        this.name = name;
        this.number = number;
    }

    @NotNull
    public final Helper copy(@NotNull String name, @NotNull String number) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(number, "number");
        return new Helper(name, number);
    }

    // $FF: synthetic method
    public static Helper copy$default(Helper var0, String var1, String var2, int var3, Object var4) {
        if ((var3 & 1) != 0) {
            var1 = var0.name;
        }

        if ((var3 & 2) != 0) {
            var2 = var0.number;
        }

        return var0.copy(var1, var2);
    }

    @NotNull
    public String toString() {
        return "Helper(name=" + this.name + ", number=" + this.number + ")";
    }

    public int hashCode() {
        String var10000 = this.name;
        int var1 = (var10000 != null ? var10000.hashCode() : 0) * 31;
        String var10001 = this.number;
        return var1 + (var10001 != null ? var10001.hashCode() : 0);
    }

    public boolean equals(@Nullable Object var1) {
        if (this != var1) {
            if (var1 instanceof Helper) {
                Helper var2 = (Helper)var1;
                return Intrinsics.areEqual(this.name, var2.name) && Intrinsics.areEqual(this.number, var2.number);
            }

            return false;
        } else {
            return true;
        }
    }

    @Override
    public int compareTo(Object o) {
        Helper helper = (Helper) o;
        return name.compareTo(helper.getName());
    }
}
