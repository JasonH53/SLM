package me.strafe.module;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import me.strafe.module.fishing.AntiAfk;
import me.strafe.module.fishing.SlugFishAutoFish;
import me.strafe.module.fishing.ThunderAura;
import me.strafe.module.kuudra.*;
import me.strafe.module.others.GuessTheBuild;
import me.strafe.module.others.MurderMystery;
import me.strafe.module.render.*;
import me.strafe.module.skyblock.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import javax.net.ssl.HttpsURLConnection;

import static me.strafe.module.skyblock.Pathfinding.b;

public class ModuleManager {

    public static ArrayList<Module> modules;

    public ModuleManager() {
        (modules = new ArrayList<Module>()).clear();

        //Render
        this.modules.add(new ClickGUI());
        this.modules.add(new HUD());
        this.modules.add(new ReloadConfig());
        this.modules.add(new PlayerDisplayer());
        this.modules.add(new Notifications());
        this.modules.add(new ESP());

        //Skyblock
        this.modules.add(new AutoGloom());
        this.modules.add(new ChestStealer());
        this.modules.add(new GhostBlock());
        this.modules.add(new Velocity());
        this.modules.add(new HyperionClicker());
        this.modules.add(new ArmorSwappa());
        this.modules.add(new GhostAimbot());
        this.modules.add(new AutoSalvage());
        this.modules.add(new HorsemanAOTD());
        this.modules.add(new DojoAddons());
        this.modules.add(new JerryBoxes());
        this.modules.add(new PeltsHelper());
        this.modules.add(new HarpSolver());
        this.modules.add(new ReforgeClicker());
        this.modules.add(new AutoFarm());
        this.modules.add(new Chay());
        this.modules.add(new D4MIT());

        //Kuudra
        this.modules.add(new AutoKuudra());
        this.modules.add(new AutoReady());
        this.modules.add(new AutoBuy());
        this.modules.add(new EntityReach());
        this.modules.add(new FindEntity());
        this.modules.add(new AutoChest());

        //Fishing
        this.modules.add(new AntiAfk());
        this.modules.add(new SlugFishAutoFish());
        this.modules.add(new ThunderAura());
        this.modules.add(new PlasmaPlacer());

        //Others
        this.modules.add(new GuessTheBuild());
        this.modules.add(new MurderMystery());
    }

    public Module getModule(String name) {
        for (Module m : this.modules) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public ArrayList<Module> getModuleList() {
        return this.modules;
    }

    public ArrayList<Module> getModulesInCategory(Category c) {
        ArrayList<Module> mods = new ArrayList<Module>();
        for (Module m : this.modules) {
            if (m.getCategory() == c) {
                mods.add(m);
            }
        }
        return mods;
    }

}
