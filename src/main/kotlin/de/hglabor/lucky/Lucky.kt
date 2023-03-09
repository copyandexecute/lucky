package de.hglabor.lucky

import de.hglabor.lucky.mixin.loot.accessor.LeafEntryAccessor
import net.fabricmc.api.ModInitializer
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.provider.number.LootNumberProvider
import net.minecraft.util.math.MathHelper
import net.silkmc.silk.commands.command
import net.silkmc.silk.core.logging.logInfo

object Lucky : ModInitializer {
    private var isActive: Boolean = false

    override fun onInitialize() {
        logInfo("Initializing Lucky Mod")

        command("lucky") {
            literal("start") {
                
            }
        }
    }

    fun LivingEntity.apply(): Int {
        return if (!isActive or (this !is PlayerEntity)) EnchantmentHelper.getLooting(this) else (this as PlayerEntity).luck.toInt()
    }

    fun LootNumberProvider.apply(lootContext: LootContext): Float {
        return if (isActive) 1f else nextFloat(lootContext)
    }

    fun LeafEntryAccessor.apply(luck: Float): Int {
        val luckyAndQuality: Float = if (weight <= 10 && isActive) {
            weight * luck
        } else {
            quality * luck
        }
        return MathHelper.floor(weight + luckyAndQuality).coerceAtLeast(0)
    }
}