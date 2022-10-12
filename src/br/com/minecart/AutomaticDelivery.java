package br.com.minecart;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import br.com.minecart.storage.LOGStorage;

public class AutomaticDelivery
{
    public void delivery(ArrayList<MinecartKey> minecartKeys)
    {
        for (MinecartKey minecartKey : minecartKeys) {
            this.executeCommands(minecartKey);
        }
    }

    private void executeCommands(MinecartKey minecartKey)
    {
        for (String command : minecartKey.getCommands()) {
            if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)) {
                LOGStorage.executeCommand(command);
            }
        }
    }
}