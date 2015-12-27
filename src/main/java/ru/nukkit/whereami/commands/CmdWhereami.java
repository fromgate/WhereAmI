package ru.nukkit.whereami.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.whereami.WhereAmI;
import ru.nukkit.whereami.util.Message;
import ru.nukkit.whereami.util.Util;

@CmdDefine(command = "whereami", alias = "wami,coord", subCommands = {}, permission = "whereami.command", description = Message.CMD_WAMI_DESC )
public class CmdWhereami extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (args.length>0) return false;
        Message msg =  WhereAmI.getPlugin().fullInfo() ? Message.COORD_WXYZPY : Message.COORD_WXYZ;
        return Message.YOUR_COORD.print(player,msg.getText("NOCOLOR", Util.locToString(player.getLocation()),player.pitch,player.yaw));
    }
}
