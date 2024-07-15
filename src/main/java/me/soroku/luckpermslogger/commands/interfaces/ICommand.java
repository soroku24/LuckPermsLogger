package me.soroku.luckpermslogger.commands.interfaces;

import com.velocitypowered.api.command.SimpleCommand;
import me.soroku.luckpermslogger.LuckPermsLogger;


public interface ICommand extends SimpleCommand {

    String getCommand();
    String[] getAliases();

    default LuckPermsLogger getPlugin(){
        return null;
    };
}
