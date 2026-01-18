package br.com.minecart.commands;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartHttpResponseTranslateMessage;
import br.com.minecart.core.CommandFailureLogger;
import br.com.minecart.core.MinecartAPI;
import br.com.minecart.core.entities.Key;
import br.com.minecart.core.utilities.http.HttpRequestException;
import br.com.minecart.helpers.PlayerHelper;
import br.com.minecart.utilities.Messaging;

public class RedeemKey implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Messaging.format("error.inform-key", true, true));
            return false;
        }

        if (Minecart.instance.getConfig().getBoolean("config.force_clean_inventry", true) && !PlayerHelper.playerInventoryClean(player)) {
            player.sendMessage(Messaging.format("error.clean-inventory", true, true));
            return false;
        }

        String key = args[0];

        try {
            Key minecartKey = MinecartAPI.redeemKey(player.getName(), key);
            this.delivery(player, minecartKey);
            return true;
        } catch (HttpRequestException e) {
            MinecartHttpResponseTranslateMessage.processHttpError(player, e.getResponse());
        }

        return false;
    }

    private void delivery(Player player, Key key)
    {
        if (this.executeCommands(player, key)) {
            this.sendMessageSuccessful(player, key);
        } else {
            this.sendMessageFailed(player, key);
        }
    }

    private Boolean executeCommands(Player player, Key key)
    {
        Boolean result = true;

        for (String command : key.getCommands()) {
            if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)) {
                CommandFailureLogger.executeCommand(command);
                result = false;
            }
        }

        return result;
    }

    private void sendMessageSuccessful(Player player, Key key)
    {
        Iterator<String> messages = Minecart.instance.ResourceMessage.getStringList("success.active-key").iterator();

        while (messages.hasNext()) {
            String message = messages.next();
            message = this.parseText(message, player , key);

            player.sendMessage(Messaging.format(message, false, false));
        }
    }

    private void sendMessageFailed(Player player, Key key)
    {
        String message = Minecart.instance.ResourceMessage.getString("error.redeem-key");
        message = this.parseText(message, player, key);

        player.sendMessage(Messaging.format("error.internal-error", true, true));
        player.sendMessage(Messaging.format(message, true, false));
    }

    private String parseText(String text, Player player, Key key)
    {
        text = text.replace("{player.name}", player.getName());
        text = text.replace("{key.product_name}", key.getProductName());

        return text;
    }
}