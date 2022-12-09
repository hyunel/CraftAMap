package com.hyun.craftamap;

import com.hyun.craftamap.commands.GenerateCommand;
import com.hyun.craftamap.commands.GenerateTabComplete;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class CraftAMap extends JavaPlugin {
    public static CraftAMap instance;
    @Override
    public void onLoad() {
        instance = this;
    }

    public static CraftAMap getInstance() {
        return instance;
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        // Plugin startup logic
        File mapDataFolder = new File(Objects.requireNonNull(getDataFolder()), "mapdata");
        if(!mapDataFolder.exists()) {
            mapDataFolder.mkdirs();
        }

        Objects.requireNonNull(getCommand("craftamap")).setExecutor(new GenerateCommand());
        Objects.requireNonNull(getCommand("craftamap")).setTabCompleter(new GenerateTabComplete());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
