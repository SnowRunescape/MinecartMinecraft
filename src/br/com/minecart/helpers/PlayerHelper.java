package br.com.minecart.helpers;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
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

    public static Player[] getPlayersOnline()
    {
        Player[] playersOnline = null;

        try {
            if (Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).getReturnType() == Collection.class) {
                Collection<?> collection = ((Collection<?>)Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));

                playersOnline = collection.toArray(new Player[collection.size()]);
            } else {
                playersOnline = ((Player[]) Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));
            }
        }
        catch (NoSuchMethodException e) {}
        catch (InvocationTargetException e) {}
        catch (IllegalAccessException e) {}

        return playersOnline;
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