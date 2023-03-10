package de.hglabor.lucky

import net.minecraft.text.MutableText
import net.silkmc.silk.core.text.literalText

fun getProgressBar(
    current: Int,
    max: Int,
    completedColor: Int,
    notCompletedColor: Int,
    totalBars: Int = 30,
    symbol: String = "|",
): MutableText {
    val progressPercentage = current.toDouble() / max.toDouble()
    return literalText {
        for (i in 0 until totalBars) {
            if (i < totalBars * progressPercentage) {
                text(symbol) { color = completedColor }
            } else {
                text(symbol) { color = notCompletedColor }
            }
        }
    }
}
