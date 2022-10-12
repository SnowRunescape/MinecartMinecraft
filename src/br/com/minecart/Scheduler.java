package br.com.minecart;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import br.com.minecart.helpers.MinecartKeyHelper;
import br.com.minecart.utilities.HttpRequestException;

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

            Minecart.instance.getLogger().info("Realizado a confirmacao das entregas.");
            Minecart.instance.getLogger().info("Iniciando entregas...");

            this.automaticDelivery.delivery(minecartKeys);

            Minecart.instance.getLogger().info("Entregas feitas com sucesso.");
        } catch (HttpRequestException e) {
            e.printStackTrace();
        }
    }
}