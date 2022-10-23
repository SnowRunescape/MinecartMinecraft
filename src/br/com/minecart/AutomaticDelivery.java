package br.com.minecart;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.minecart.helpers.MinecartKeyHelper;
import br.com.minecart.storage.LOGStorage;
import br.com.minecart.utilities.HttpRequestException;

public class AutomaticDelivery
{
    public final static int NONE = 0;
    public final static int ONLY_PLAYER_ONLINE = 1;
    public final static int ANYTIME = 2;

    public void run()
    {
        try {
            ArrayList<MinecartKey> minecartKeys = MinecartKeyHelper.filterByAutomaticDelivery(MinecartAPI.deliveryPending());

            if (minecartKeys.isEmpty()) {
                return;
            }

            MinecartAPI.deliveryConfirm(MinecartKeyHelper.getMinecartKeyIds(minecartKeys));

            for (MinecartKey minecartKey : minecartKeys) {
                this.executeCommands(minecartKey);
            }
        } catch (HttpRequestException e) {
            String message = MinecartAPI.messageHttpError(Minecart.instance.getServer().getConsoleSender(), e.getResponse());

            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    private void executeCommands(MinecartKey minecartKey)
    {
        for (final String command : minecartKey.getCommands()) {
            Bukkit.getScheduler().runTask(Minecart.instance, new BukkitRunnable() {
                public void run() {
                    if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)) {
                        LOGStorage.executeCommand(command);
                    }
                }
            });
        }
    }
}