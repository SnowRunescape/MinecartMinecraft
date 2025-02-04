package br.com.minecart.scheduler;

import org.bukkit.scheduler.BukkitRunnable;

public class Scheduler extends BukkitRunnable
{
    private SchedulerInterface scheduler;

    public Scheduler(SchedulerInterface scheduler)
    {
        this.scheduler = scheduler;
    }

    public void run()
    {
        scheduler.run();
    }
}