package ru.nukkit.whereami;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import ru.nukkit.whereami.util.Message;
import ru.nukkit.whereami.util.Util;

public class WamiListener implements Listener{

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onMove (PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (!Util.isInfoEnabled(player)) return;
        if (Util.isMoveInsideBlock(player)) return;
        Util.enableInfo(player);
        Util.sendTipLoc(player);
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onChat (PlayerChatEvent event){
        if (!event.getMessage().replace(" ","").toLowerCase().contains("whereami")) return;
        if (!WhereAmI.getPlugin().checkChatMask(event.getMessage())) return;
        final Player player = event.getPlayer();
        if (!player.hasPermission("whereami.server")) return;
        Server.getInstance().getScheduler().scheduleDelayedTask(new Runnable() {
            public void run() {
                Message.CORD_SERVER.broadcast(null,'5','5',player.getName(),Util.locToString(player.getLocation()));
            }
        },1);
    }
}
