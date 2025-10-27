package me.toplib.easypass;

import org.bukkit.plugin.java.JavaPlugin;

public final class EasyPass extends JavaPlugin {

    private static EasyPass instance;

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static EasyPass getInstance() {
        return instance;
    }
}
