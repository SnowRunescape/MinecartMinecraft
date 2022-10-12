package br.com.minecart.utilities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Utils
{
    public static String[] convertJsonArrayToStringArray(JsonArray jsonArray)
    {
        List<String> arrayList = new ArrayList<String>();

        for (JsonElement teste : jsonArray) {
            arrayList.add(teste.getAsString());
        }

        return arrayList.toArray(new String[arrayList.size()]);
    }
}