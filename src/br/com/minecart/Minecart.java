package br.com.minecart;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minecart.commands.MainCommand;
import br.com.minecart.expansion.PurchasesExpansion;
import br.com.minecart.helpers.PlayerHelper;
import br.com.minecart.listeners.Playerlistener;

public class Minecart extends JavaPlugin
{
    public final String VERSION = "2.4.0";
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
        this.loadCooldownToPlayersOnline();
        this.loadPlaceholderAPI();
        this.loadConfigs();

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

    private void loadCooldownToPlayersOnline()
    {
        for (Player player : PlayerHelper.getPlayersOnline()) {
            String username = player.getName().toLowerCase();
            this.cooldown.put(username, System.currentTimeMillis());
        }
    }

    private void loadPlaceholderAPI()
    {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PurchasesExpansion().register();
        }
    }

    public void loadConfigs()
    {
        this.MinecartAutorization = getConfig().getString("Minecart.ShopKey", "");
        this.MinecartShopServer = getConfig().getString("Minecart.ShopServer", "");
        this.preventLoginDelivery = getConfig().getBoolean("config.preventLoginDelivery", true);
    }
}