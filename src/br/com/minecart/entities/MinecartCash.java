package br.com.minecart.entities;

public class MinecartCash
{
    private int quantity;
    private String command;

    public MinecartCash(int quantity, String command)
    {
        this.quantity = quantity;
        this.command = command;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public String getCommand()
    {
        return this.command;
    }
}