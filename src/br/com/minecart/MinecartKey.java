package br.com.minecart;

public class MinecartKey
{
    private int id;
    private String username;
    private String key;
    private String[] commands;

    public MinecartKey(int id, String username, String key, String[] commands)
    {
        this.id = id;
        this.username = username;
        this.key = key;
        this.commands = commands;
    }

    public int getId()
    {
        return this.id;
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
}