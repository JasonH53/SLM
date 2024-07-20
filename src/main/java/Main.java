import me.strafe.SLM;
import me.strafe.commands.Goto;
import me.strafe.commands.Look;
import me.strafe.commands.SetAutoFarm;
import me.strafe.events.TickEndEvent;
import me.strafe.module.ModuleManager;
import me.strafe.module.skyblock.Pathfinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "EXAMPLEMOD", version = "1.0")
public class Main {

    @EventHandler
    public void init (FMLInitializationEvent event) throws Exception {
    	SLM.instance = new SLM();
    	SLM.instance.init();
        ModuleManager.setup();
        ClientCommandHandler.instance.registerCommand(new Goto());
        ClientCommandHandler.instance.registerCommand(new Look());
        ClientCommandHandler.instance.registerCommand(new SetAutoFarm());
        MinecraftForge.EVENT_BUS.register(new TickEndEvent());
        MinecraftForge.EVENT_BUS.register(new Pathfinding());
        me.strafe.utils.Registers.Registers.init();
    }
}
