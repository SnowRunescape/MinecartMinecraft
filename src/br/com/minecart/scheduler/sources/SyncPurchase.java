package br.com.minecart.scheduler.sources;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartAPI;
import br.com.minecart.scheduler.SchedulerInterface;
import br.com.minecart.utilities.HttpRequestException;

public class SyncPurchase implements SchedulerInterface
{
    public final static long DELAY = 5 * 60 * 20L;

    public void run()
    {
        try {
            Minecart.instance.purchasePlayers = MinecartAPI.purchases();
        } catch (HttpRequestException e) {}
    }
}