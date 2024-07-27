package me.strafe.commands;

import me.strafe.module.skyblock.Pathfinding;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.VecUtils;
import me.strafe.utils.pathfinding.Pathfinder;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.strafe.utils.Stolenutils.mc;

public class Goto implements ICommand {

    @Override
    public String getCommandName() {
        return "goto";
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
        if(args.length != 3) return;
        String x = args[0];
        String y = args[1];
        String z = args[2];
        new Thread(() -> {
            Pathfinding.initWalk();
            Pathfinder.setup(new BlockPos(VecUtils.floorVec(mc.thePlayer.getPositionVector())), new BlockPos(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z)), 0.0);
        }).start();
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
