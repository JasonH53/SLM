package me.strafe.commands;

import me.strafe.module.skyblock.AutoFarm;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.Rotation;
import me.strafe.utils.RotationUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetAutoFarm implements ICommand {
    @Override
    public String getCommandName() {
        return "setAutoFarm";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 2) return;
        String y = args[0];
        String p = args[1];
        try {
            AutoFarm.pitch = Float.valueOf(p);
            AutoFarm.yaw = Float.valueOf(y);
        } catch (Exception e) {
            ChatUtils.addChatMessage("retard");
        }
        ChatUtils.addChatMessage("Set yaw to " + y + " and pitch to " + p);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(@NotNull ICommand o) {
        return 0;
    }
}
