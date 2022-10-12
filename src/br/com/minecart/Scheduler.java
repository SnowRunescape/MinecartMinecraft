package br.com.minecart;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import br.com.minecart.utilities.HttpRequestException;
import br.com.minecart.utilities.MinecartKeyHelper;

public class Scheduler extends BukkitRunnable
{
    private AutomaticDelivery automaticDelivery;

    public Scheduler()
    {
        this.automaticDelivery = new AutomaticDelivery();
    }

    public void run()
    {
        Minecart.instance.getLogger().info("Buscando produtos pendentes para ser entregue...");

        try {
            ArrayList<MinecartKey> minecartKeys = MinecartKeyHelper.filterByPlayerOnline(MinecartAPI.deliveryPending());

            if (minecartKeys.isEmpty()) {
                Minecart.instance.getLogger().info("Nenhum produto para ser entregue.");
                return;
            }

            MinecartAPI.deliveryConfirm(MinecartKeyHelper.getMinecartKeyIds(minecartKeys));

            this.automaticDelivery.delivery(minecartKeys);
        } catch (HttpRequestException e) {
            e.printStackTrace();
        }
    }
}