package br.com.minecart.helpers;

import java.util.ArrayList;

import br.com.minecart.AutomaticDelivery;
import br.com.minecart.Minecart;
import br.com.minecart.MinecartKey;

public class MinecartKeyHelper
{
    public static ArrayList<MinecartKey> filterByAutomaticDelivery(ArrayList<MinecartKey> minecartKeys)
    {
        ArrayList<MinecartKey> tempMinecartKeys = new ArrayList<MinecartKey>();

        for (MinecartKey minecartKey : minecartKeys) {
            if (
                    minecartKey.getDeliveryAutomaitc() == AutomaticDelivery.ANYTIME || (
                            !Minecart.instance.preventLoginDelivery &&
                            PlayerHelper.playerOnline(minecartKey.getUsername())
                    ) || (
                            Minecart.instance.preventLoginDelivery &&
                            PlayerHelper.playerOnline(minecartKey.getUsername()) &&
                            PlayerHelper.playerTimeOnline(minecartKey.getUsername()) > Minecart.instance.TIME_PREVENT_LOGIN_DELIVERY
                    )
            ) {
                tempMinecartKeys.add(minecartKey);
            }
        }

        return tempMinecartKeys;
    }

    public static int[] getMinecartKeyIds(ArrayList<MinecartKey> minecartKeys)
    {
        int counter = 0;
        int quantity = minecartKeys.size();

        int[] ids = new int[quantity];

        for (MinecartKey minecartKey : minecartKeys) {
            ids[counter] = minecartKey.getId();
            counter++;
        }

        return ids;
    }
}