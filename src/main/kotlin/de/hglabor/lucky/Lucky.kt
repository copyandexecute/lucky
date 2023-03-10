package de.hglabor.lucky

import de.hglabor.lucky.mixin.loot.accessor.LeafEntryAccessor
import kotlinx.coroutines.Job
import net.fabricmc.api.ModInitializer
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.provider.number.LootNumberProvider
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.MathHelper
import net.silkmc.silk.commands.command
import net.silkmc.silk.core.logging.logInfo
import net.silkmc.silk.core.task.infiniteMcCoroutineTask
import net.silkmc.silk.core.text.literalText
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object Lucky : ModInitializer {
    private var isActive: Boolean = false
    private var jobs: MutableList<Job> = mutableListOf()
    private const val LUCK_COLOR = 0x00ff26
    private val MinecraftServer.players: MutableList<ServerPlayerEntity>
        get() = playerManager.playerList


    override fun onInitialize() {
        logInfo("Initializing Lucky Mod")

        command("lucky") {
            literal("start") {
                runs {
                    this.source.sendMessage(literalText("Viel Glück ;)") {
                        color = LUCK_COLOR
                    })
                    isActive = true
                    val server = this.source.server
                    jobs += infiniteMcCoroutineTask(period = 1.seconds) {
                        server.players.forEach {
                            val luckInstance =
                                it.attributes.getCustomInstance(EntityAttributes.GENERIC_LUCK) ?: return@forEach
                            luckInstance.baseValue += 0.25
                        }
                    }
                    jobs += infiniteMcCoroutineTask(sync = false) {
                        server.players.forEach {
                            it.sendMessage(getProgressBar(it.luck.toInt(), 1024, LUCK_COLOR, 0x5c6066), true)
                        }
                    }
                }
            }
            literal("stop") {
                runs {
                    this.source.sendMessage(literalText("Nicht mehr viel Glück ;)") {
                        color = 0xa200ff
                    })
                    jobs.forEach(Job::cancel)
                    isActive = false
                }
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
