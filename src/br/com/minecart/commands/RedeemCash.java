package br.com.minecart.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minecart.Minecart;
import br.com.minecart.MinecartHttpResponseTranslateMessage;
import br.com.minecart.core.CommandFailureLogger;
import br.com.minecart.core.MinecartAPI;
import br.com.minecart.core.entities.Cash;
import br.com.minecart.core.utilities.http.HttpRequestException;
import br.com.minecart.utilities.Messaging;

public class RedeemCash implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player player = (Player) sender;

        try {
            Cash cash = MinecartAPI.redeemCash(player.getName());

            if (cash.getQuantity() > 0) {
                return this.deliverCash(player, cash);
            }
        } catch(HttpRequestException e) {
            MinecartHttpResponseTranslateMessage.processHttpError(player, e.getResponse());
        }

        return false;
    }

    private boolean deliverCash(Player player, Cash cash)
    {
        String command = cash.getCommand();

        if (Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)) {
            String msg = Minecart.instance.ResourceMessage.getString("success.redeem-cash");
            msg = this.parseText(msg, player , cash);

            player.sendMessage(Messaging.format(msg, true, false));
            return true;
        } else {
            String msg = Minecart.instance.ResourceMessage.getString("error.redeem-cash");
            msg = this.parseText(msg, player , cash);

            player.sendMessage(Messaging.format("error.internal-error", true, true));
            player.sendMessage(Messaging.format(msg, true, false));

            CommandFailureLogger.executeCommand(command);
            return false;
        }
    }

    private String parseText(String text, Player player, Cash cash)
    {
        text = text.replace("{player.name}", player.getName());

        return text;
    }
}