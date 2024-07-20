package me.strafe.Mixins;

import me.strafe.events.ChestBackgroundDrawnEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiContainer.class}, priority=999)
public abstract class MixinContainerGui extends GuiScreen {
    @Shadow
    public Container inventorySlots;

    @Inject(method={"drawScreen"}, at={@At(value="HEAD")})
    private void backgroundDrawn(CallbackInfo ci) {
        if (this.inventorySlots instanceof ContainerChest) {
            IInventory chest = ((ContainerChest)((Object)this.inventorySlots)).getLowerChestInventory();
            MinecraftForge.EVENT_BUS.post(new ChestBackgroundDrawnEvent(this.inventorySlots, StringUtils.stripControlCodes((String)chest.getDisplayName().getUnformattedText().trim()), this.inventorySlots.inventorySlots.size(), this.inventorySlots.inventorySlots, chest));
        }
    }
}