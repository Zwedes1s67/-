package com.yourname.economy.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerBalanceProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerBalance> PLAYER_BALANCE = CapabilityManager.get(new CapabilityToken<PlayerBalance>() {});

    private PlayerBalance balance = null;
    private final LazyOptional<PlayerBalance> optional = LazyOptional.of(this::createPlayerBalance);

    private PlayerBalance createPlayerBalance() {
        if (this.balance == null) {
            this.balance = new PlayerBalance();
        }
        return this.balance;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_BALANCE) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerBalance().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerBalance().loadNBTData(nbt);
    }
}
