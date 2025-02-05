package br.com.minecart.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartAPI;
import br.com.minecart.entities.MinecartKey;
import br.com.minecart.utilities.Messaging;

public class MyKeys implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player player = (Player) sender;

        try {
            ArrayList<MinecartKey> minecartKeys = MinecartAPI.myKeys(player);

            player.sendMessage(Messaging.format("success.player-list-keys-title", false, true));
            player.sendMessage("");

            if (minecartKeys.isEmpty()) {
                player.sendMessage(Messaging.format("error.player-dont-have-key", false, true));
            } else {
                for (MinecartKey minecartKey : minecartKeys) {
                    String msg = Minecart.instance.ResourceMessage.getString("success.player-list-keys-key");

                    msg = this.parseText(msg, minecartKey);

                    player.sendMessage(Messaging.format(msg, false, false));
                }
            }

            return true;
        } catch(Exception e) {
            player.sendMessage(Messaging.format("error.internal-error", false, true));
        }

        return false;
    }

    private String parseText(String text, MinecartKey minecartKey)
    {
        text = text.replace("{key.code}", minecartKey.getKey());
        text = text.replace("{key.product_name}", minecartKey.getProductName());

        return text;
    }
}