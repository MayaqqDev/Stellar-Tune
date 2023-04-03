package dev.mayaqq.stellartune.utils;

import dev.mayaqq.stellartune.config.StellarConfig;

import java.util.concurrent.TimeUnit;

public class Common {
    public static String calcCooldown(long lastUsed, long cooldown) {
        long nextUseTimestamp = lastUsed + (cooldown * 1000L);
        long remainingCooldown = (nextUseTimestamp - System.currentTimeMillis());
        long hours = TimeUnit.MILLISECONDS.toHours(remainingCooldown);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingCooldown - TimeUnit.HOURS.toMillis(hours));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingCooldown - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));
        return String.format("%d hours, %d minutes, %d seconds", hours, minutes, seconds);
    }
}
