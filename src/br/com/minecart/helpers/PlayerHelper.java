package br.com.minecart.helpers;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.minecart.Minecart;

public class PlayerHelper
{
    public static Boolean playerInventoryClean(Player player)
    {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                return false;
            }
        }

        return true;
    }

    public static Boolean playerOnline(String username)
    {
        return Bukkit.getPlayer(username) != null;
    }

    public static long playerTimeOnline(String username)
    {
        long time = 0;

        username = username.toLowerCase();

        if (Minecart.instance.cooldown.containsKey(username)) {
            time =TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Minecart.instance.cooldown.get(username));
        }

        return time;
    }
}