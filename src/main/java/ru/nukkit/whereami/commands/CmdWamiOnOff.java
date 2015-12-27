package ru.nukkit.whereami.commands;


import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.whereami.util.Message;
import ru.nukkit.whereami.util.Util;

@CmdDefine(command = "whereami", alias = "wami,coord", subCommands ={"(?i)on|off"} , permission = "whereami.info", description = Message.INFO_DESC )
public class CmdWamiOnOff extends Cmd{


    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (args[0].equalsIgnoreCase("on")){
            Util.enableInfo(player);
        } else {
            Util.disableInfo (player);
        }
        return Message.INFO_STATE.print(sender,(Util.isInfoEnabled(player) ? Message.ENABLED : Message.DISABLED));
    }
}
