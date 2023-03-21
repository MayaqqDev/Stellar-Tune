package dev.mayaqq.stellartune.registry;

import eu.pb4.polymer.core.api.other.PolymerStat;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;

public class StatRegistry {
    public static Identifier RTP_USES = PolymerStat.registerStat(new Identifier("stellartune", "rtp_uses"), StatFormatter.DEFAULT);

    public static void register() {
    }
}
