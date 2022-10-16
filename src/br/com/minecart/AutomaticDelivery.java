package br.com.minecart;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import br.com.minecart.helpers.MinecartKeyHelper;
import br.com.minecart.storage.LOGStorage;
import br.com.minecart.utilities.HttpRequestException;

public class AutomaticDelivery
{
    public final static int NONE = 0;
    public final static int ONLY_PLAYER_ONLINE = 1;
    public final static int ANYTIME = 2;

    public void run()
    {
        Minecart.instance.getLogger().info("Buscando produtos pendentes para ser entregue...");

        try {
            ArrayList<MinecartKey> minecartKeys = MinecartKeyHelper.filterByAutomaticDelivery(MinecartAPI.deliveryPending());

            if (minecartKeys.isEmpty()) {
                Minecart.instance.getLogger().info("Nenhum produto para ser entregue.");
                return;
            }

            MinecartAPI.deliveryConfirm(MinecartKeyHelper.getMinecartKeyIds(minecartKeys));

            Minecart.instance.getLogger().info("Realizado a confirmacao das entregas.");
            Minecart.instance.getLogger().info("Iniciando entregas...");

            for (MinecartKey minecartKey : minecartKeys) {
                this.executeCommands(minecartKey);
            }

            Minecart.instance.getLogger().info("Entregas feitas com sucesso.");
        } catch (HttpRequestException e) {
            e.printStackTrace();
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