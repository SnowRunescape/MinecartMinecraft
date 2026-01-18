package br.com.minecart.scheduler.sources;

import br.com.minecart.Minecart;
import br.com.minecart.core.MinecartAPI;
import br.com.minecart.core.utilities.http.HttpRequestException;
import br.com.minecart.scheduler.SchedulerInterface;

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