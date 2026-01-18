package br.com.minecart;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.minecart.core.MinecartAPI;
import br.com.minecart.core.utilities.http.HttpResponse;
import br.com.minecart.utilities.Messaging;

public class MinecartHttpResponseTranslateMessage extends JavaPlugin
{
    public final static long DELAY = 60 * 20L;

    public static void processHttpError(Player player, HttpResponse response)
    {
        String message = MinecartHttpResponseTranslateMessage.messageHttpError(player, response);

        if (response.responseCode == 401 && !player.hasPermission("minecart.admin")) {
            message =  Messaging.format("error.internal-error", false, true);
        }

        player.sendMessage(message);
    }

    public static String messageHttpError(CommandSender player, HttpResponse response)
    {
        if (response.responseCode == 401) {
            return Messaging.format("error.invalid-shopkey", false, true);
        }

        try {
            JsonObject jsonObject = JsonParser.parseString(response.response).getAsJsonObject();

            Integer errorCode = jsonObject.get("code").getAsInt();

            switch (errorCode) {
                case MinecartAPI.INVALID_KEY:
                    return Messaging.format("error.invalid-key", false, true);
                case MinecartAPI.INVALID_SHOP_SERVER:
                    return player.hasPermission("minecart.admin") ?
                        Messaging.format("error.invalid-shopserver", false, true) :
                        Messaging.format("error.internal-error", false, true);
                case MinecartAPI.DONT_HAVE_CASH:
                    return Messaging.format("error.nothing-products-cash", false, true);
                case MinecartAPI.COMMANDS_NOT_REGISTRED:
                    return Messaging.format("error.commands-product-not-registred", false, true);
            }
        } catch (Exception e) {}

        return Messaging.format("error.internal-error", false, true);
    }
}