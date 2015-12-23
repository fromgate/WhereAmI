package ru.nukkit.whereami;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.TextFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WhereAmI extends PluginBase implements Listener {

    private Map<String,Location> players;
    private TaskHandler task;


    @Override
    public void onEnable(){
        this.players = new HashMap<String, Location>();
        this.task = null;
        getServer().getPluginManager().registerEvents(this,this);
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onMove (PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (!players.containsKey(player.getName())) return;
        if (isOneBlock(players.get(player.getName()), player.getLocation())) return;
        players.put(player.getName(),player.getLocation());
        player.sendTip(TextFormat.GOLD+locToString(player.getLocation()));
    }

    private boolean isOneBlock(Location loc1, Location loc2){
        if (loc1==null||loc2==null) return false;
        if (!loc1.getLevel().equals(loc2.getLevel())) return false;
        if (loc1.getFloorX()!=loc2.getFloorX()) return false;
        if (loc1.getFloorZ()!=loc2.getFloorZ()) return false;
        return loc1.getFloorY()==loc2.getFloorY();
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onChat (PlayerChatEvent event){
        if (!event.getMessage().replace(" ","").toLowerCase().contains("whereami")) return;
        final Player player = event.getPlayer();
        if (!player.hasPermission("whereami.server")) return;

        this.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
            public void run() {
                getServer().broadcastMessage(TextFormat.LIGHT_PURPLE+"[Server] Dear "+player.getName()+" your location is: "+locToString(player.getLocation()));
            }
        },1);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;
        if (player==null) return false;
        if (args.length==0&&player.hasPermission("whereami.command")){
            player.sendMessage(TextFormat.YELLOW+"Your coordinates: "+locToString(player.getLocation()));
        } else if (args[0].matches("(?i)(on|off)")&&player.hasPermission("whereami.info")){
            if (args[0].equalsIgnoreCase("on")){
                players.put(player.getName(),player.getLocation());
                updateTask();
            } else {
                if (players.containsKey(player.getName())) players.remove(player.getName());
                updateTask();
            }
            player.sendMessage(TextFormat.YELLOW+"Coordinate informer is "+(players.containsKey(player.getName())? "enabled": "disabled"));
        } else return false;
        return true;
    }


    private void updateTask(){
        if (task == null&&players.isEmpty()) return;
        if (!this.players.isEmpty()&&task==null){
            task = this.getServer().getScheduler().scheduleRepeatingTask(new Runnable() {
                public void run() {
                    for (String playerStr : new ArrayList<String>(players.keySet())){
                        Player player = getServer().getPlayer(playerStr);
                        if (player == null||!player.isOnline()) players.remove(playerStr);
                        else player.sendTip(TextFormat.GOLD+locToString(player.getLocation()));
                    }
                }
            }, 10);

        } else if (players.isEmpty()&&task!=null){
            task.cancel();
            task = null;
        }
    }

    private String locToString (Location loc){
        return "["+loc.getLevel().getName()+"] "+loc.getFloorX()+", "+
                loc.getFloorY()+", "+loc.getFloorZ();
    }









}
