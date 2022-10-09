package br.com.minecart;

public class MinecartKey
{
    private int id;
    private String key;
    private String[] commands;

    public MinecartKey(int id, String key, String[] commands)
    {
        this.id = id;
        this.key = key;
        this.commands = commands;
    }

    public int getId()
    {
        return this.id;
    }

    public String getKey()
    {
        return this.key;
    }

    public String[] getCommands()
    {
        return this.commands;
    }
}