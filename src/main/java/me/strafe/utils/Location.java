package me.strafe.utils;

import me.strafe.utils.handlers.ScoreboardHandler;

public class Location {
    public static boolean isSB() {
        return ScoreboardHandler.hasLine("SKYBLOCK");
    }
    public static boolean isInKuudra() {
        return ScoreboardHandler.hasLine("Kuudra's End");
    }
}
