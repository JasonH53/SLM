package me.strafe.module.kuudra;

import me.strafe.events.SecondEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.Location;
import me.strafe.utils.handlers.ScoreboardHandler;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class AutoBuy extends Module {
    private static boolean Multi40;
    private static boolean Bonus40;
    private static boolean Multi80;
    private static boolean Bonus80;
    private static int debounce = 60;

    public AutoBuy() {
        super("Auto Buy", "Auto Buy from shop", Category.KUUDRA);
    }

    public void onEnable() {
        super.onEnable();
    }

    @SubscribeEvent
    public void onSecond(SecondEvent event) {
        if (!Location.isInKuudra()) return;
        if (ScoreboardHandler.getToken() >= 50) {
            if (Multi40 || Bonus40) {
                if (mc.currentScreen == null) {
                    EntityReach.openShop();
                } else {
                    if (Multi40) {
                        ChatUtils.addChatMessage("Attempting to buy Multi Shot 2");
                        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, 6, 0, 0, mc.thePlayer);
                        Multi40 = false;
                    } else {
                        ChatUtils.addChatMessage("Attempting to buy Bonus Damage 2");
                        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, 3, 0, 0, mc.thePlayer);
                        Bonus40 = false;
                    }
                    mc.thePlayer.closeScreen();
                }
            }
        }
        if (!Multi40 && !Bonus40) {
            if (ScoreboardHandler.getToken() >= 80) {
                if (Multi80 || Bonus80) {
                    if (mc.currentScreen == null) {
                        EntityReach.openShop();
                    } else {
                        if (Multi80) {
                            ChatUtils.addChatMessage("Attempting to buy Multi Shot 3");
                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, 6, 0, 0, mc.thePlayer);
                            Multi80 = false;
                        } else {
                            ChatUtils.addChatMessage("Attempting to buy Bonus Damage 3");
                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, 3, 0, 0, mc.thePlayer);
                            Bonus80 = false;
                        }
                        mc.thePlayer.closeScreen();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        String s = StringUtils.stripControlCodes(e.message.getUnformattedText());
        if (s.equalsIgnoreCase("Multi Shot was upgraded to tier: 2")) Multi40=false;
        if (s.equalsIgnoreCase("Multi Shot was upgraded to tier: 3")) Multi80=false;
        if (s.equalsIgnoreCase("Bonus Damage was upgraded to tier: 2")) Bonus40=false;
        if (s.equalsIgnoreCase("Bonus Damage was upgraded to tier: 3")) Bonus80=false;
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        Bonus80 = true;
        Multi80 = true;
        Bonus40 = true;
        Multi40 = true;
    }

}
