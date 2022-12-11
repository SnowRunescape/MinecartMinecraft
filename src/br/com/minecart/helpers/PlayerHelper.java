package br.com.minecart.helpers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
}