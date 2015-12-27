package ru.nukkit.whereami.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Location;
import cn.nukkit.scheduler.TaskHandler;
import ru.nukkit.whereami.WhereAmI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Util {

    private static Map<String,Location> players;
    private static TaskHandler task;


    private static void updateTask(){
        if (task == null&&players.isEmpty()) return;
        if (!players.isEmpty()&&task==null){
            task = Server.getInstance().getScheduler().scheduleRepeatingTask(new Runnable() {
                public void run() {
                    for (String playerStr : new ArrayList<String>(players.keySet())){
                        Player player = Server.getInstance().getPlayer(playerStr);
                        if (player == null||!player.isOnline()) players.remove(playerStr);
                        else sendTipLoc (player);
                    }
                }
            }, 10);

        } else if (players.isEmpty()&&task!=null){
            task.cancel();
            task = null;
        }
    }

    public static String locToString (Location loc){
        return "["+loc.getLevel().getName()+"] "+loc.getFloorX()+", "+
                loc.getFloorY()+", "+loc.getFloorZ();
    }

    public static void enableInfo(Player player) {
        players.put(player.getName(),player.getLocation());
        updateTask();
    }


    public static void disableInfo(Player player) {
        if (players.containsKey(player.getName())) players.remove(player.getName());
        updateTask();
    }

    public static boolean isInfoEnabled(Player player) {
        return players.containsKey(player.getName());
    }

    public static void init() {
        players = new HashMap<String, Location>();
        task = null;
    }

    public static boolean isOneBlock(Location loc1, Location loc2){
        if (loc1==null||loc2==null) return false;
        if (!loc1.getLevel().equals(loc2.getLevel())) return false;
        if (loc1.getFloorX()!=loc2.getFloorX()) return false;
        if (loc1.getFloorZ()!=loc2.getFloorZ()) return false;
        return loc1.getFloorY()==loc2.getFloorY();
    }

    public static boolean isMoveInsideBlock(Player player) {
        if (player == null) return false;
        if (!players.containsKey(player.getName())) return false;
        return isOneBlock(players.get(player.getName()),player.getLocation());
    }

    public static void sendTipLoc (Player player){
        Message msg =  WhereAmI.getPlugin().fullInfo() ? Message.COORD_WXYZPY : Message.COORD_WXYZ;
        player.sendTip(msg.getText('6','6',locToString(player.getPlayer()),player.getPitch(),player.getYaw()));
    }

}
