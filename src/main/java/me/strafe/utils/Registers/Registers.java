package me.strafe.utils.Registers;

import me.strafe.utils.Friendutils;

public class Registers {

    public static Friendutils[] FriendsDatabase = new Friendutils[100];

    public static int sentPlayers = 0;
    public static int webhookPlayers = 0;

    public static void init() {
        for (int i = 0; i <= FriendsDatabase.length-1; i ++) {
            FriendsDatabase[i] = new Friendutils();
        }
    }

}
