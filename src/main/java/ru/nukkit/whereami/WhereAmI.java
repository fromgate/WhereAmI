package ru.nukkit.whereami;

import cn.nukkit.plugin.PluginBase;
import ru.nukkit.whereami.commands.Commander;
import ru.nukkit.whereami.util.Message;
import ru.nukkit.whereami.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhereAmI extends PluginBase {

    private static WhereAmI instance;

    private boolean fullInfo;
    String chatMask;

    private Pattern chatMaskPattern;

    public void cfgUpdate(){
        instance = this;
        this.getDataFolder().mkdirs();
        this.reloadConfig();
        fullInfo =getConfig().getBoolean("where-am-i.show-full-info",false);
        getConfig().set("where-am-i.show-full-info",fullInfo);
        chatMask =getConfig().getString("where-am-i.chat-regex-mask","(?i)where\\s+am\\s+i");
        getConfig().set("where-am-i.chat-regex-mask",chatMask);
        chatMaskPattern = Pattern.compile(chatMask);
        this.saveConfig();
    }


    @Override
    public void onEnable(){
        Message.init(this);
        Commander.init(this);
        cfgUpdate();
        Util.init();
        getServer().getPluginManager().registerEvents(new WamiListener(),this);
    }


    public static WhereAmI getPlugin() {
        return instance;
    }

    public boolean checkChatMask(String message){
        Matcher m = chatMaskPattern.matcher(message);
        return m.find();
    }

    public boolean fullInfo(){
        return this.fullInfo;
    }
}
