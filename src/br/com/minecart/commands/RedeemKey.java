package br.com.minecart.commands;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartAPI;
import br.com.minecart.MinecartKey;
import br.com.minecart.helpers.PlayerHelper;
import br.com.minecart.storage.LOGStorage;
import br.com.minecart.utilities.HttpRequestException;
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
            MinecartKey minecartKey = MinecartAPI.redeemKey(player, key);
            this.delivery(player, minecartKey);
            return true;
        } catch (HttpRequestException e) {
            MinecartAPI.processHttpError(player, e.getResponse());
        }

        return false;
    }

    private void delivery(Player player, MinecartKey minecartKey)
    {
        if (this.executeCommands(player, minecartKey)) {
            this.sendMessageSuccessful(player, minecartKey);
        } else {
            this.sendMessageFailed(player, minecartKey);
        }
    }

    private Boolean executeCommands(Player player, MinecartKey minecartKey)
    {
        Boolean result = true;

        for (String command : minecartKey.getCommands()) {
            if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)) {
                LOGStorage.executeCommand(command);
                result = false;
            }
        }

        return result;
    }

    private void sendMessageSuccessful(Player player, MinecartKey minecartKey)
    {
        Iterator<String> messages = Minecart.instance.ResourceMessage.getStringList("success.active-key").iterator();

        while (messages.hasNext()) {
            String message = messages.next();
            message = this.parseText(message, player , minecartKey);

            player.sendMessage(Messaging.format(message, false, false));
        }
    }

    private void sendMessageFailed(Player player, MinecartKey minecartKey)
    {
        String message = Minecart.instance.ResourceMessage.getString("error.redeem-key");
        message = this.parseText(message, player, minecartKey);

        player.sendMessage(Messaging.format("error.internal-error", true, true));
        player.sendMessage(Messaging.format(message, true, false));
    }

    private String parseText(String text, Player player, MinecartKey minecartKey)
    {
        text = text.replace("{player.name}", player.getName());
        text = text.replace("{key.product_name}", minecartKey.getProductName());

        return text;
    }
}