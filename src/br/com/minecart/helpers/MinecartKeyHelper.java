package br.com.minecart.helpers;

import java.util.ArrayList;

import br.com.minecart.Minecart;
import br.com.minecart.core.PlayerSessionManager;
import br.com.minecart.core.entities.Key;
import br.com.minecart.scheduler.sources.AutomaticDelivery;

public class MinecartKeyHelper
{
    public static ArrayList<Key> filterByAutomaticDelivery(ArrayList<Key> keys)
    {
        ArrayList<Key> tempMinecartKeys = new ArrayList<Key>();

        for (Key key : keys) {
            if (
                    key.getDeliveryAutomaitc() == AutomaticDelivery.ANYTIME || (
                    !Minecart.instance.preventLoginDelivery
                    && PlayerHelper.playerOnline(key.getUsername())
                ) || (
                    Minecart.instance.preventLoginDelivery
                    && PlayerHelper.playerOnline(key.getUsername())
                    && PlayerSessionManager.getInstance().getSessionDuration(key.getUsername()) > Minecart.instance.TIME_PREVENT_LOGIN_DELIVERY
                )
            ) {
                tempMinecartKeys.add(key);
            }
        }

        return tempMinecartKeys;
    }
}