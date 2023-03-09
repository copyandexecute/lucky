package de.hglabor.lucky.mixin.loot.condition;

import de.hglabor.lucky.Lucky;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RandomChanceWithLootingLootCondition.class)
public abstract class RandomChanceWithLootConditionMixin implements LootCondition {
    @Redirect(method = "test(Lnet/minecraft/loot/context/LootContext;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getLooting(Lnet/minecraft/entity/LivingEntity;)I"))
    private int lootingInjection(LivingEntity entity) {
        return Lucky.INSTANCE.apply(entity);
    }
}