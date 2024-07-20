package me.strafe.commands;

import me.strafe.utils.ChatUtils;
import me.strafe.utils.Rotation;
import me.strafe.utils.RotationUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Look implements ICommand {
    @Override
    public String getCommandName() {
        return "look";
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
        String x = args[0];
        String y = args[1];
        try {
            Rotation rot = new Rotation(Integer.valueOf(x),Integer.valueOf(y));
            RotationUtils.setup(rot, (long) 500);
        } catch (Exception e) {
            ChatUtils.addChatMessage("Put an integer fucktard");
        }
        RotationUtils.update();
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
