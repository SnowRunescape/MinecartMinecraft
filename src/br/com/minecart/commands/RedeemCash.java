package br.com.minecart.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartAPI;
import br.com.minecart.entities.MinecartCash;
import br.com.minecart.storage.LOGStorage;
import br.com.minecart.utilities.HttpRequestException;
import br.com.minecart.utilities.Messaging;

public class RedeemCash implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player player = (Player) sender;

        try {
            MinecartCash minecartCash = MinecartAPI.redeemCash(player);

            if (minecartCash.getQuantity() > 0) {
                return this.deliverCash(player, minecartCash);
            }
        } catch(HttpRequestException e) {
            MinecartAPI.processHttpError(player, e.getResponse());
        }

        return false;
    }

    private boolean deliverCash(Player player, MinecartCash minecartCash)
    {
        String command = minecartCash.getCommand();

        if (Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)) {
            String msg = Minecart.instance.ResourceMessage.getString("success.redeem-cash");
            msg = this.parseText(msg, player , minecartCash);

            player.sendMessage(Messaging.format(msg, true, false));
            return true;
        } else {
            String msg = Minecart.instance.ResourceMessage.getString("error.redeem-cash");
            msg = this.parseText(msg, player , minecartCash);

            player.sendMessage(Messaging.format("error.internal-error", true, true));
            player.sendMessage(Messaging.format(msg, true, false));

            LOGStorage.executeCommand(command);
            return false;
        }
    }

    private String parseText(String text, Player player, MinecartCash minecartCash)
    {
        text = text.replace("{player.name}", player.getName());

        return text;
    }
}