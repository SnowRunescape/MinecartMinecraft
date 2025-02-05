package br.com.minecart.entities;

public class MinecartKey
{
    private int id;
    private String productName;
    private String username;
    private String key;
    private String[] commands;
    private int deliveryAutomatic;

    public MinecartKey(int id, String productName, String username, String key, String[] commands, int deliveryAutomatic)
    {
        this.id = id;
        this.productName = productName;
        this.username = username;
        this.key = key;
        this.commands = commands;
        this.deliveryAutomatic = deliveryAutomatic;
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

    public int getDeliveryAutomaitc()
    {
        return this.deliveryAutomatic;
    }
}