package de.hglabor.lucky.mixin.loot.entry;

import de.hglabor.lucky.Lucky;
import de.hglabor.lucky.mixin.loot.accessor.LeafEntryAccessor;
import net.minecraft.loot.LootChoice;
import net.minecraft.loot.entry.LeafEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.loot.entry.LeafEntry$Choice")
public abstract class ChoiceMixin implements LootChoice {
    @Shadow
    @Final
    LeafEntry field_1004;

    @Override
    public int getWeight(float luck) {
        return Lucky.INSTANCE.apply(((LeafEntryAccessor) field_1004), luck);
    }
}