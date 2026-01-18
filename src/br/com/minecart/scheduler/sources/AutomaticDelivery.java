package br.com.minecart.scheduler.sources;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartHttpResponseTranslateMessage;
import br.com.minecart.core.CommandFailureLogger;
import br.com.minecart.core.MinecartAPI;
import br.com.minecart.core.entities.Key;
import br.com.minecart.core.utilities.http.HttpRequestException;
import br.com.minecart.helpers.MinecartKeyHelper;
import br.com.minecart.scheduler.SchedulerInterface;

public class AutomaticDelivery implements SchedulerInterface
{
    public final static long DELAY = 60 * 20L;

    public final static int NONE = 0;
    public final static int ONLY_PLAYER_ONLINE = 1;
    public final static int ANYTIME = 2;

    public void run()
    {
        try {
            ArrayList<Key> keys = MinecartKeyHelper.filterByAutomaticDelivery(MinecartAPI.deliveryPending());

            if (keys.isEmpty()) {
                return;
            }

            MinecartAPI.deliveryConfirm(keys);

            for (Key key : keys) {
                this.executeCommands(key);
            }
        } catch (HttpRequestException e) {
            String message = MinecartHttpResponseTranslateMessage.messageHttpError(Minecart.instance.getServer().getConsoleSender(), e.getResponse());
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    private void executeCommands(Key key)
    {
        long delay = 0L;

        for (final String command : key.getCommands()) {
            new BukkitRunnable() {
                public void run() {
                    if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)) {
                        CommandFailureLogger.executeCommand(command);
                    }
                }
            }.runTaskLater(Minecart.instance, delay);

            delay += Minecart.instance.delayExecuteCommands * 1L;
        }
    }
}