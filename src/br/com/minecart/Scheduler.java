package br.com.minecart;

import org.bukkit.scheduler.BukkitRunnable;

public class Scheduler extends BukkitRunnable
{
    private AutomaticDelivery automaticDelivery;

    public Scheduler()
    {
        this.automaticDelivery = new AutomaticDelivery();
    }

    public void run()
    {
        automaticDelivery.run();
    }
}