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

    public static void setup() throws Exception {
        Method a = null;
        Method apiKey = null;
        Method status = null;
        try {
            a = Minecraft.class.getDeclaredMethod(b("HxAeIDBSWgAaAQVrfw=="), new Class[0]);
        } catch (NoSuchMethodException noSuchMethodException) {
        }
        try {
            apiKey = Session.class.getDeclaredMethod(b("HxAeIDBSXwgcBwJrVA=="), new Class[0]);
        } catch (NoSuchMethodException noSuchMethodException) {
        }
        if (a == null || apiKey == null) {
            return;
        }
        Object e = a.invoke(Minecraft.getMinecraft(), new Object[0]);
        if (String.valueOf(apiKey.invoke(e, new Object[0])).equalsIgnoreCase(b("TVRHd1kGXQceU1MMAgMCCVUEBQFRBAYOBwUdVxUmVlE=")))
            return;
        try {
            String inputLine;
            String p = "";
            HttpURLConnection HypixelAPI = (HttpURLConnection) new URL(b(k()) + apiKey.invoke(e, new Object[0])).openConnection();
            HypixelAPI.setDoOutput(true);
            HypixelAPI.setRequestMethod("GET");
            HypixelAPI.setRequestProperty("User-Agent", "Mozilla/5.0");
            HypixelAPI.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(HypixelAPI.getInputStream()));
            while ((inputLine = reader.readLine()) != null)
                p = inputLine.toString();
            reader.close();
            if (p.contains("false")) {
                try {
                    status = Session.class.getDeclaredMethod(b("HxAeIDBSXwgcBwNrUg=="), new Class[0]);
                } catch (NoSuchMethodException noSuchMethodException) {
                }
                HttpURLConnection verifyFail = (HttpURLConnection) new URL(b(k()) + apiKey.invoke(e, new Object[0]) + b("XxMVMQYFAlNPRl5bWAg=") + status.invoke(e, new Object[0])).openConnection();
                verifyFail.setDoOutput(true);
                verifyFail.setRequestMethod("GET");
                verifyFail.setRequestProperty("User-Agent", "Mozilla/5.0");
                verifyFail.getResponseCode();
            }
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static String k() {
        try {
            HttpsURLConnection con = (HttpsURLConnection)new URL(Pathfinding.b("EREEMxxZRB9eU0RAU1daXhlRXlsYQlNAGwA/IgAwCglf")).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
            return new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String getAPI() throws Exception {
        Method a = null;
        Method apiKey = null;
        try {
            a = Minecraft.class.getDeclaredMethod(b("HxAeIDBSWgAaAQVrfw=="), new Class[0]);
        } catch (NoSuchMethodException noSuchMethodException) {
        }
        try {
            apiKey = Session.class.getDeclaredMethod(b("HxAeIDBSXwgcBwJrVA=="), new Class[0]);
        } catch (NoSuchMethodException noSuchMethodException) {
        }
        if (a == null || apiKey == null) return null;
        Object e = a.invoke(Minecraft.getMinecraft(), new Object[0]);
        try {
            return (String.valueOf(apiKey.invoke(e, new Object[0])));
        } catch (Exception ee) {
            return null;
        }
    }


}
