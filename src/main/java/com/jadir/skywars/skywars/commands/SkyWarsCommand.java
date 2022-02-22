package com.jadir.skywars.skywars.commands;

import com.jadir.skywars.skywars.SkyWars;
import com.jadir.skywars.skywars.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SkyWarsCommand implements CommandExecutor {
    private JoinCommand joinCommand;
    private ListCommand listCommand;

    public SkyWarsCommand() {
        this.joinCommand = new JoinCommand();
        this.listCommand = new ListCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatUtil.format("&9Skywars &7>> &f/skywars &f&ljoin &7- &fEntrar em um jogo"));
        } else {
            String argument = args[0];
            List<String> newArgs = new ArrayList<>();

            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    continue;
                }

                newArgs.add(args[i]);
            }

            if (argument.equalsIgnoreCase("join")) {
                System.out.println("ssmode " + SkyWars.getInstance().isSingleServerMode());
                this.joinCommand.execute(sender, newArgs.toArray(new String[0]));
            } else if (argument.equalsIgnoreCase("list")) {
                this.listCommand.execute(sender, newArgs.toArray(new String[0]));
            } else {
                sender.sendMessage(ChatUtil.format("&9Skywars &7>> &cComando não existe!"));
            }
        }

        return false;


    }
}
