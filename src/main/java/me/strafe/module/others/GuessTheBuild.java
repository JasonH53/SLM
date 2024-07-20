package me.strafe.module.others;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.strafe.SLM;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class GuessTheBuild extends Module {

    private ArrayList<String> wordList = new ArrayList();
    private int tips = 0;
    private String temp;

    public GuessTheBuild() {
        super("Guess The Build Helper","gtb helper", Category.OTHERS);
        loadWords();
    }

        @SubscribeEvent
        public void onChat(ClientChatReceivedEvent clientChatReceivedEvent) {
            if (clientChatReceivedEvent.type == 2) {
                if (clientChatReceivedEvent.message.getFormattedText().contains("The theme is") && clientChatReceivedEvent.message.getFormattedText().contains("_")) {
                    int n;
                    if (wordList.isEmpty()) {
                        loadWords();
                    }
                    if ((n = getTips(clientChatReceivedEvent.message.getFormattedText())) != tips) {
                        tips = n;
                        String string = ChatFormatting.stripFormatting(clientChatReceivedEvent.message.getFormattedText()).replaceFirst("The theme is ", "");
                        ArrayList<String> arrayList = getMatchingWords(string);
                        if (arrayList.get(0) == temp) return;
                        if (arrayList.size() == 1) {
                            ChatUtils.addChatMessage("The word is " + arrayList.get(0).toLowerCase());
                            temp = arrayList.get(0);
                            return;
                        }
                        ChatUtils.addChatMessage("Multiple words found");
                        if (arrayList.size() <= 20) {
                            for (String s : arrayList) {
                                ChatUtils.addChatMessage("The word could be: " + s);
                            }
                            temp = arrayList.get(0);
                            return;
                        } else {
                            ChatUtils.addChatMessage("More than 20 words found, try again with more hints");
                            temp = arrayList.get(0);
                        }
                    } else {
                        tips = 0;
                    }
                }
            }
        }

    private void loadWords() {
        try {
            String string;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(SLM.class.getClassLoader().getResourceAsStream("words.txt")));
            while ((string = bufferedReader.readLine()) != null) {
                wordList.add(string);
            }
            bufferedReader.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (mc.theWorld == null || mc.thePlayer == null) return;
            ChatUtils.addChatMessage("Word list failed to load");
        }
    }

    private int getTips(String string) {
        return string.replaceAll(" ", "").replaceAll("_", "").length();
    }

    public ArrayList<String> getMatchingWords(String string) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string2 : this.wordList) {
            if (string2.length() != string.length()) continue;
            boolean bl = true;
            for (int i = 0; i < string2.length(); ++i) {
                if (string.charAt(i) == '_') {
                    if (string2.charAt(i) != ' ') continue;
                    bl = false;
                }
                if (string.charAt(i) != string2.charAt(i)) {
                    bl = false;
                }
                if (!bl) break;
            }
            if (!bl) continue;
            arrayList.add(string2);
        }
        return arrayList;
    }
}