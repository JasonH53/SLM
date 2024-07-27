package me.strafe.utils.handlers;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import me.strafe.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.*;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.strafe.utils.Utils.mc;

public class ScoreboardHandler {

    private static final Pattern p = Pattern.compile("Tokens: ([0-9]*)");
    public static boolean hasLine(String sbString) {
        ScoreObjective sbo;
        if (mc != null && mc.thePlayer != null && (sbo = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1)) != null) {
            List<String> scoreboard = getSidebarLines();
            scoreboard.add(StringUtils.stripControlCodes((String)sbo.getDisplayName()));
            for (String s : scoreboard) {
                String validated = stripString(s);
                if (!validated.contains(sbString)) continue;
                return true;
            }
        }
        return false;
    }

    public static String stripString(String s) {
        char[] nonValidatedString = StringUtils.stripControlCodes((String)s).toCharArray();
        StringBuilder validated = new StringBuilder();
        for (char a : nonValidatedString) {
            if (a >= '' || a <= '') continue;
            validated.append(a);
        }
        return validated.toString();
    }

    public static int getToken() {
        ScoreObjective sbo;
        if (mc != null && mc.thePlayer != null && (sbo = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1)) != null) {
            List<String> scoreboard = getSidebarLines();
            scoreboard.add(StringUtils.stripControlCodes((String)sbo.getDisplayName()));
            for (String s : scoreboard) {
                String validated = stripString(s);
                if (validated.contains("Tokens: ")) {
                    String tokenz = validated;
                    Matcher m = p.matcher(tokenz);
                    if (m.find()) {
                        int tokens = Integer.parseInt(m.group(1));
                        return tokens;
                    }
                }
            }
        }
        return 0;
    }

    private static List<String> getSidebarLines() {
        ArrayList<String> lines = new ArrayList<String>();
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (scoreboard == null) {
            return lines;
        }
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) {
            return lines;
        }
        Collection<Score> scores = scoreboard.getSortedScores(objective);
        ArrayList<Score> list = new ArrayList<Score>();
        for (Score s : scores) {
            if (s == null || s.getPlayerName() == null || s.getPlayerName().startsWith("#")) continue;
            list.add(s);
        }
        scores = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, scores.size() - 15)) : list;
        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName((Team)team, (String)score.getPlayerName()));
        }
        return lines;
    }
}
