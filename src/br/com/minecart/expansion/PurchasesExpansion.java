package br.com.minecart.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;

import br.com.minecart.MinecartAPI;
import br.com.minecart.MinecartPurchasePlayer;

public class PurchasesExpansion extends PlaceholderExpansion {
    @Override
    public String getAuthor() {
        return "SnowRunescape";
    }

    @Override
    public String getIdentifier() {
        return "minecart";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        ArrayList<MinecartPurchasePlayer> minecartKeys = MinecartAPI.purchases();

        if (minecartKeys != null) {
            if (params.matches("purchases_\\d+_username")) {
                int index = Integer.parseInt(params.split("_")[1]) - 1;
                if (index >= 0 && index < minecartKeys.size()) {
                    return minecartKeys.get(index).player;
                }
            }

            if (params.matches("purchases_\\d+_amount")) {
                int index = Integer.parseInt(params.split("_")[1]) - 1;
                if (index >= 0 && index < minecartKeys.size()) {
                    return minecartKeys.get(index).amount;
                }
            }
        }

        return null;
    }
}