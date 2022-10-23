package br.com.minecart;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.minecart.utilities.HttpRequest;
import br.com.minecart.utilities.HttpRequestException;
import br.com.minecart.utilities.HttpResponse;
import br.com.minecart.utilities.Messaging;
import br.com.minecart.utilities.Utils;

public class MinecartAPI extends JavaPlugin
{
    private final static String URL = "https://api.minecart.com.br";

    private final static int INVALID_KEY = 40010;
    private final static int INVALID_SHOP_SERVER = 40011;
    private final static int DONT_HAVE_CASH = 40012;
    private final static int COMMANDS_NOT_REGISTRED = 40013;

    public final static long DELAY = 60 * 20L;

    public static ArrayList<MinecartKey> myKeys(Player player) throws Exception
    {
        String username = player.getName();

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("username", username);

        HttpResponse response = HttpRequest.httpRequest(MinecartAPI.URL + "/shop/player/mykeys", params);

        if (response.responseCode != 200) {
            throw new HttpRequestException(response);
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(response.response).getAsJsonObject();
        JsonArray productsPlayer = jsonObject.getAsJsonArray("products");

        ArrayList<MinecartKey> minecartKeys = new ArrayList<MinecartKey>();

        for (JsonElement product : productsPlayer) {
            JsonObject productObj = product.getAsJsonObject();

            Integer id = productObj.get("id").getAsInt();
            String key = productObj.get("key").getAsString();
            String productName = productObj.get("product_name").getAsString();

            minecartKeys.add(new MinecartKey(id, productName, username, key, null, 0));
        }

        return minecartKeys;
    }

    public static MinecartCash redeemCash(Player player) throws HttpRequestException
    {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("username", player.getName());

        HttpResponse response = HttpRequest.httpRequest(MinecartAPI.URL + "/shop/player/redeemcash", params);

        if (response.responseCode != 200) {
            throw new HttpRequestException(response);
        }

        JsonObject jsonObject = new JsonParser().parse(response.response).getAsJsonObject();

        int quantity = jsonObject.get("cash").getAsInt();
        String command = jsonObject.get("command").getAsString();

        return new MinecartCash(quantity, command);
    }

    public static MinecartKey redeemVip(Player player, String key) throws HttpRequestException
    {
        String username = player.getName();

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("username", username);
        params.put("key", key);

        HttpResponse response = HttpRequest.httpRequest(MinecartAPI.URL + "/shop/player/redeemvip", params);

        if (response.responseCode != 200) {
            throw new HttpRequestException(response);
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(response.response).getAsJsonObject();

        Integer id = jsonObject.get("id").getAsInt();
        String[] commands = Utils.convertJsonArrayToStringArray(jsonObject.get("commands").getAsJsonArray());

        return new MinecartKey(id, null, username, key, commands, 0);
    }

    public static ArrayList<MinecartKey> deliveryPending() throws HttpRequestException
    {
        ArrayList<MinecartKey> minecartKeys = new ArrayList<MinecartKey>();

        HttpResponse response = HttpRequest.httpRequest(MinecartAPI.URL + "/shop/delivery/pending", null);

        if (response.responseCode != 200) {
            throw new HttpRequestException(response);
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(response.response).getAsJsonObject();
        JsonArray productsPlayer = jsonObject.getAsJsonArray("products");

        for (JsonElement product : productsPlayer) {
            JsonObject productObj = product.getAsJsonObject();

            Integer id = productObj.get("id").getAsInt();
            String username = productObj.get("username").getAsString();
            String productName = productObj.get("product_name").getAsString();
            String key = productObj.get("key").getAsString();
            String[] commands = Utils.convertJsonArrayToStringArray(productObj.get("commands").getAsJsonArray());
            int deliveryAutomatic = productObj.get("delivery_automatic").getAsInt();

            minecartKeys.add(new MinecartKey(id, productName, username, key, commands, deliveryAutomatic));
        }

        return minecartKeys;
    }

    public static boolean deliveryConfirm(int[] ids)
    {
        Map<String, String> params = new LinkedHashMap<String, String>();

        for (int id : ids) {
            params.put("products[]", String.valueOf(id));
        }

        try {
            HttpResponse response = HttpRequest.httpRequest(MinecartAPI.URL + "/shop/delivery/confirm", params);

            return (response.responseCode == 200);
        } catch (HttpRequestException e) {}

        return false;
    }

    public static void processHttpError(Player player, HttpResponse response)
    {
        if (!player.hasPermission("minecart.admin")) {
            player.sendMessage(Messaging.format("error.internal-error", false, true));
            return;
        } else if (response.responseCode == 401) {
            player.sendMessage(Messaging.format("error.invalid-shopkey", false, true));
            return;
        }

        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(response.response).getAsJsonObject();

            Integer errorCode = jsonObject.get("code").getAsInt();

            if (errorCode == INVALID_KEY) {
                String kickMessage = Messaging.format("error.invalid-key", true, true);
                kickMessage = kickMessage.replace("\\n", "\n");

                player.kickPlayer(kickMessage);
            } else if (errorCode == INVALID_SHOP_SERVER) {
                player.sendMessage(Messaging.format("error.invalid-shopserver", false, true));
            } else if (errorCode == DONT_HAVE_CASH) {
                player.sendMessage(Messaging.format("error.nothing-products-cash", false, true));
            } else if (errorCode == COMMANDS_NOT_REGISTRED) {
                player.sendMessage(Messaging.format("error.commands-product-not-registred", false, true));
            } else {
                player.sendMessage(Messaging.format("error.internal-error", false, true));
            }
        } catch(Exception e) {
            player.sendMessage(Messaging.format("error.internal-error", false, true));
        }
    }
}