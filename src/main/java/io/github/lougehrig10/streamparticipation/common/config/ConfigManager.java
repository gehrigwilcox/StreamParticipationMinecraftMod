package io.github.lougehrig10.streamparticipation.common.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import java.util.Map;

public class ConfigManager {

    public static ConfigHolder<ConfigFile> config;

    public ConfigManager(){
        AutoConfig.register(ConfigFile.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ConfigFile.class);
    }

    public String getOAuth(String uuid){
        PlayerConfig player = config.getConfig().players.get(uuid);

        if(player == null){
            return "";
        }

        return player.twitchOAuth;
    }

    public void registerOAuth(String uuid, String oAuth){
        PlayerConfig player = config.getConfig().players.get(uuid);

        if(player == null){
            config.getConfig().players.put(uuid,new PlayerConfig(oAuth));
        }else {
            player.setTwitchOAuth(oAuth);
        }
        config.save();
    }

    public Map<String,Event> getEvents(String uuid){
        PlayerConfig player = config.getConfig().players.get(uuid);

        if(player == null) return config.getConfig().allowedEvents;
        Map<String,Event> events = player.getEvents();

        if(events.isEmpty()){
            return config.getConfig().allowedEvents;
        }
        return events;
    }

    public void setEvents(String uuid, Map<String,Event> events){
        PlayerConfig player = config.getConfig().players.get(uuid);

        if(player == null) return;
        player.setEvents(events);
        config.save();
    }

    public Map<String,Event> getAllowedEvents(){
        return config.getConfig().allowedEvents;
    }
}
