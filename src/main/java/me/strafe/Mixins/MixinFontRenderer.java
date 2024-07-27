package me.strafe.Mixins;

import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.strafe.utils.RandomUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static me.strafe.module.skyblock.Pathfinding.b;

@Mixin(value = {FontRenderer.class})
public abstract class MixinFontRenderer {

    private static int textNum = 1;

    @ModifyVariable(method = {"renderString(Ljava/lang/String;FFIZ)I"}, at = @At(value = "HEAD"), argsOnly = true)
    private String renderString(String text) {
        if (text != null) {
            if (text.contains(b("Ax8KORUZEUpUSE1f"))) {
                text=null;
            } else if (text.contains(b("SFAYYw4NDxBXU0VaRRVQRUNXQkIXU11CRFkc"))) {
                text=null;
            }
        }
        return text;
    }

    public void setNum(int i) {
        textNum = i;
    }
}

