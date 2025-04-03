package org.pushkraj;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;

public class ReferMe extends JavaPlugin {
    private File dataFile;
    private FileConfiguration config;
    private Gson gson;
    private ReferralData referralData;

    @Override
    public void onEnable() {
        // Initialize configuration
        saveDefaultConfig();
        config = getConfig();

        // Initialize Gson for JSON handling
        gson = new GsonBuilder().setPrettyPrinting().create();

        // Initialize data file
        dataFile = new File(getDataFolder(), "data.json");
        if (!dataFile.exists()) {
            saveResource("data.json", false);
        }
        loadData();

        // Register commands
        getCommand("refer").setExecutor(new ReferCommand(this));
        getCommand("referinfo").setExecutor(new ReferInfoCommand(this));

        // Register event listeners
        getServer().getPluginManager().registerEvents(new ReferralGUIListener(this), this);

        getLogger().info("ReferMe plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Save data before shutdown
        saveData();
        getLogger().info("ReferMe plugin has been disabled!");
    }

    private void saveData() {
        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }
            String json = gson.toJson(referralData);
            java.nio.file.Files.write(dataFile.toPath(), json.getBytes());
        } catch (IOException e) {
            getLogger().severe("Could not save data file!");
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            if (dataFile.exists()) {
                String content = new String(java.nio.file.Files.readAllBytes(dataFile.toPath()));
                referralData = gson.fromJson(content, ReferralData.class);
            }
            if (referralData == null) {
                referralData = new ReferralData();
            }
        } catch (IOException e) {
            getLogger().severe("Could not load data file!");
            e.printStackTrace();
            referralData = new ReferralData();
        }
    }

    public void executeRewards(String referrer, String referred) {
        // Execute rewards for referrer
        for (String cmd : getConfig().getStringList("rewards.referrer.commands")) {
            String command = cmd.replace("{player}", referrer);
            getServer().dispatchCommand(getServer().getConsoleSender(), command);
        }

        // Execute rewards for referred player
        for (String cmd : getConfig().getStringList("rewards.referred.commands")) {
            String command = cmd.replace("{player}", referred);
            getServer().dispatchCommand(getServer().getConsoleSender(), command);
        }
    }

    public ReferralData getReferralData() {
        return referralData;
    }

    public File getDataFile() {
        return dataFile;
    }

    public Gson getGson() {
        return gson;
    }
}
