package br.com.minecart;

public class MinecartKey
{
    private int id;
    private String productName;
    private String username;
    private String key;
    private String[] commands;
    private int automaticDelivery;

    public MinecartKey(int id, String productName, String username, String key, String[] commands, int automaticDelivery)
    {
        this.id = id;
        this.productName = productName;
        this.username = username;
        this.key = key;
        this.commands = commands;
        this.automaticDelivery = automaticDelivery;
    }

    public int getId()
    {
        return this.id;
    }

    public String getProductName()
    {
        return this.productName;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getKey()
    {
        return this.key;
    }

    public String[] getCommands()
    {
        return this.commands;
    }

    public int getAutomaitcDelivery()
    {
        return this.automaticDelivery;
    }
}