package me.strafe.module;

import me.strafe.SLM;
import me.strafe.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;

public class Module {

	protected static Minecraft mc = Minecraft.getMinecraft();
	
	private String name, description;
	private int key;
	private Category category;
	private boolean toggled;
	public boolean visible = true;
	public static boolean messagetoggle = true;

	public Module(String name, String description, Category category) {
		super();
		this.name = name;
		this.description = description;
		this.key = 0;
		this.category = category;
		this.toggled = false;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
		if (SLM.instance.saveLoad != null) {
			SLM.instance.saveLoad.save();
		}
	}

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
		
		if (this.toggled) {
			this.onEnable();
		} else {
			this.onDisable();
		}
	}
	
	public void toggle() {
		this.toggled = !this.toggled;
		
		if (this.toggled) {
			this.onEnable();
		} else {
			this.onDisable();
		}
		if (SLM.instance.saveLoad != null) {
			SLM.instance.saveLoad.save();
		}
	}
	
	public void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
		if (mc.thePlayer!= null && messagetoggle && mc.theWorld!=null) {
			ChatUtils.addChatMessage(EnumChatFormatting.DARK_RED + this.name + EnumChatFormatting.WHITE + " is toggled "+ EnumChatFormatting.GREEN + "ON" + EnumChatFormatting.WHITE + ".");
		}
	}
	
	public void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
		if (mc.thePlayer!= null && messagetoggle) {
			ChatUtils.addChatMessage(EnumChatFormatting.DARK_RED + this.name + EnumChatFormatting.WHITE + " is toggled " + EnumChatFormatting.RED + "OFF" + EnumChatFormatting.WHITE + ".");
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public Category getCategory() {
		return this.category;
	}

	public void enableOnStartup() {
		toggled = true;
		try {
			toggle();
			onEnable();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void toggleToggleMessages(boolean b) {
		messagetoggle = b;
	}


}
