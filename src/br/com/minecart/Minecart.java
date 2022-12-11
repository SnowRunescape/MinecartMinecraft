package br.com.minecart;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minecart.commands.MainCommand;
import br.com.minecart.listeners.Playerlistener;

public class Minecart extends JavaPlugin
{
    public final String VERSION = "2.3.0";
    public final int TIME_PREVENT_LOGIN_DELIVERY = 120;

    public YamlConfiguration ResourceMessage;

    public static Minecart instance;

    public String MinecartAutorization;
    public String MinecartShopServer;

    public boolean preventLoginDelivery;

    public HashMap<String, Long> cooldown = new HashMap<String, Long>();

    public void onEnable()
    {
        instance = this;

        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        this.loadMessages();

        this.MinecartAutorization = getConfig().getString("Minecart.ShopKey", "");
        this.MinecartShopServer = getConfig().getString("Minecart.ShopServer", "");
        this.preventLoginDelivery = getConfig().getBoolean("config.preventLoginDelivery", true);

        MainCommand MainCommand = new MainCommand();

        getCommand("minecart").setExecutor(MainCommand);
        getCommand("mykeys").setExecutor(MainCommand);
        getCommand("redeemcash").setExecutor(MainCommand);
        getCommand("redeemkey").setExecutor(MainCommand);

        getServer().getPluginManager().registerEvents(new Playerlistener(), this);

        new Scheduler().runTaskTimerAsynchronously(this, MinecartAPI.DELAY, MinecartAPI.DELAY);
    }

    private void loadMessages()
    {
        File resourceMessage = new File(getDataFolder(), "messages.yml");

        if (!resourceMessage.exists()) {
            saveResource("messages.yml", false);
        }

        this.ResourceMessage = YamlConfiguration.loadConfiguration(resourceMessage);
    }
}