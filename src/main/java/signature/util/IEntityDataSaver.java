package signature.util;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    public NbtCompound getPersistentData();
}