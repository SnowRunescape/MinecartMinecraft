package br.com.minecart.scheduler.sources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartAPI;
import br.com.minecart.entities.MinecartKey;
import br.com.minecart.helpers.MinecartKeyHelper;
import br.com.minecart.scheduler.SchedulerInterface;
import br.com.minecart.storage.LOGStorage;
import br.com.minecart.utilities.HttpRequestException;

public class AutomaticDelivery implements SchedulerInterface
{
    public final static long DELAY = 60 * 20L;

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

            List<String> commands = new ArrayList<>();

            for (MinecartKey minecartKey : minecartKeys) {
                Collections.addAll(commands, minecartKey.getCommands());
            }

            this.executeCommands(commands);
        } catch (HttpRequestException e) {
            String message = MinecartAPI.messageHttpError(Minecart.instance.getServer().getConsoleSender(), e.getResponse());
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    private void executeCommands(List<String> commands)
    {
        long delay = 0L;

        for (final String command : commands) {
            new BukkitRunnable() {
                public void run() {
                    if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)) {
                        LOGStorage.executeCommand(command);
                    }
                }
            }.runTaskLater(Minecart.instance, delay);

            delay += Minecart.instance.delayExecuteCommands * 1L;
        }
    }
}