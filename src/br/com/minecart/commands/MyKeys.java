package br.com.minecart.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.Minecart;
import br.com.minecart.core.MinecartAPI;
import br.com.minecart.core.entities.Key;
import br.com.minecart.utilities.Messaging;

public class MyKeys implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player player = (Player) sender;

        try {
            ArrayList<Key> keys = MinecartAPI.myKeys(player.getName());

            player.sendMessage(Messaging.format("success.player-list-keys-title", false, true));
            player.sendMessage("");

            if (keys.isEmpty()) {
                player.sendMessage(Messaging.format("error.player-dont-have-key", false, true));
            } else {
                for (Key key : keys) {
                    String msg = Minecart.instance.ResourceMessage.getString("success.player-list-keys-key");

                    msg = this.parseText(msg, key);

                    player.sendMessage(Messaging.format(msg, false, false));
                }
            }

            return true;
        } catch(Exception e) {
            player.sendMessage(Messaging.format("error.internal-error", false, true));
        }

        return false;
    }

    private String parseText(String text, Key key)
    {
        text = text.replace("{key.code}", key.getKey());
        text = text.replace("{key.product_name}", key.getProductName());

        return text;
    }
}