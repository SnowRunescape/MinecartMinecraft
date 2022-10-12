package br.com.minecart;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import br.com.minecart.utilities.HttpRequestException;
import br.com.minecart.utilities.Utils;

public class Scheduler extends BukkitRunnable
{
    public void run()
    {
        Minecart.instance.getLogger().info("Buscando produtos pendentes para ser entregue...");

        try {
            ArrayList<MinecartKey> minecartKeys = MinecartAPI.deliveryPending();
            minecartKeys = this.filterArray(minecartKeys);

            if (minecartKeys.isEmpty()) {
                Minecart.instance.getLogger().info("Nenhum produto para ser entregue.");
                return;
            }

            MinecartAPI.deliveryConfirm(null);

            for (MinecartKey minecartKey : minecartKeys) {
                // Execute commands...
            }
        } catch (HttpRequestException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<MinecartKey> filterArray(ArrayList<MinecartKey> minecartKeys)
    {
        ArrayList<MinecartKey> list = new  ArrayList<MinecartKey>();

        for (MinecartKey minecartKey : minecartKeys) {
            if (Utils.playerOnline(minecartKey.getUsername())) {
                list.add(minecartKey);
            }
        }

        return list;
    }
}
