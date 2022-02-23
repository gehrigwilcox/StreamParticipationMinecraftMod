package io.github.lougehrig10.streamparticipation.common.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.HashMap;
import java.util.Map;

@Config(name = "streamparticipation")
public class ConfigFile implements ConfigData {

    static Map<String,Event> exampleEvents = new HashMap<>();
    static{
        exampleEvents.put("spawn/hostile/creeper",new Event(500,"/summon creeper"));
        exampleEvents.put("spawn/hostile/charged creeper",new Event(1000,"/summon creeper ~ ~ ~ {powered:1}"));
        exampleEvents.put("spawn/hostile/skeleton",new Event(500,"/summon skeleton"));
        exampleEvents.put("spawn/hostile/zombie",new Event(500,"/summon zombie"));
        exampleEvents.put("spawn/hostile/baby zombie",new Event(1000,"/summon zombie ~ ~ ~ {IsBaby:1}"));
        exampleEvents.put("spawn/hostile/spider",new Event(500,"/summon spider"));
        exampleEvents.put("spawn/hostile/wither skeleton",new Event(700,"/summon wither_skeleton"));
        exampleEvents.put("spawn/hostile/blaze",new Event(600,"/summon blaze"));
        exampleEvents.put("spawn/hostile/illusioner",new Event(1000,"/summon illusioner"));
        exampleEvents.put("spawn/hostile/vex",new Event(1000,"/summon vex"));
        exampleEvents.put("spawn/hostile/witch",new Event(1000,"/summon witch"));
        exampleEvents.put("spawn/hostile/guardian",new Event(600,"/summon guardian"));
        exampleEvents.put("spawn/hostile/endermite",new Event(300,"/summon endermite"));
        exampleEvents.put("spawn/hostile/killer bunny",new Event(1000,"/summon rabbit ~ ~ ~ {RabbitType:99}"));
        exampleEvents.put("spawn/hostile/ender dragon",new Event(5000,"/summon ender_dragon"));
        exampleEvents.put("spawn/hostile/wither boss",new Event(7500,"/summon wither"));
        exampleEvents.put("spawn/passive/pig",new Event(100,"/summon pig"));
        exampleEvents.put("spawn/passive/cow",new Event(100,"/summon cow"));
        exampleEvents.put("spawn/passive/sheep",new Event(100,"/summon sheep"));
        exampleEvents.put("spawn/passive/chicken",new Event(100,"/summon chicken"));
        exampleEvents.put("difficulty/peaceful",new Event(1000,"/difficulty peaceful"));
        exampleEvents.put("difficulty/easy",new Event(1000,"/difficulty easy"));
        exampleEvents.put("difficulty/normal",new Event(1000,"/difficulty normal"));
        exampleEvents.put("difficulty/hard",new Event(1000,"/difficulty hard"));
        exampleEvents.put("effect/no jump",new Event(700,"/effect give @s minecraft:jump_boost 60 200 true"));
        exampleEvents.put("effect/heal",new Event(700,"/effect give @s minecraft:instant_health"));
        exampleEvents.put("effect/damage",new Event(700,"/effect give @s minecraft:instant_damage"));
        exampleEvents.put("effect/hunger",new Event(300,"/effect give @s minecraft:hunger 6 255 true"));
        exampleEvents.put("effect/feed",new Event(300,"/effect give @s minecraft:saturation 1 80"));
        exampleEvents.put("effect/speed",new Event(500,"/effect give @s minecraft:speed 60 32 true"));
        exampleEvents.put("effect/nausea",new Event(500,"/effect give @s minecraft:nausea 60 1 true"));
        exampleEvents.put("effect/levitate",new Event(500,"/effect give @p levitation 10 1 true"));
        exampleEvents.put("effect/slow fall",new Event(500,"/effect give @p slow_falling 60 1 true"));
        exampleEvents.put("weather/rain",new Event(600,"/weather rain"));
        exampleEvents.put("weather/clear",new Event(600,"/weather clear"));
        exampleEvents.put("time/day",new Event(500,"/time set day"));
        exampleEvents.put("time/night",new Event(500,"/time set night"));
        exampleEvents.put("randomTP",new Event(2500,"/spreadplayers ~ ~ 500 500 true @s"));
        exampleEvents.put("set spawn",new Event(3000,"/spawnpoint"));
        exampleEvents.put("set fire",new Event(1500,"/execute at @s run fill ~-1 ~-1 ~-1 ~1 ~1 ~1 minecraft:fire replace air"));
        exampleEvents.put("send to end",new Event(4000,"/execute as @s in minecraft:the_end run teleport 0 80 0"));
        exampleEvents.put("send to nether",new Event(3500,"/execute as @s in minecraft:the_nether run teleport ~ ~ ~"));
        exampleEvents.put("send to overworld",new Event(3500,"/execute as @s in minecraft:overworld run teleport ~ ~ ~"));
        exampleEvents.put("yeet",new Event(3000,"/effect give @p levitation 5 20 true"));
    }

    Map<String,PlayerConfig> players = new HashMap<>();
    Map<String,Event> allowedEvents = exampleEvents;

}

class PlayerConfig {
    String twitchOAuth = "";
    String streamLabsOAuth = "";
    String streamElementsOAuth = "";
    Map<String,Event> events = new HashMap<>();

    public PlayerConfig(String twitchOAuth, String streamElementsOAuth, String streamLabsOAuth){
        this.twitchOAuth = twitchOAuth;
        this.streamElementsOAuth = streamElementsOAuth;
        this.streamLabsOAuth = streamLabsOAuth;
    }

    public PlayerConfig(String twitchOAuth){
        this.twitchOAuth = twitchOAuth;
    }

    public String getTwitchOAuth(){
        return this.twitchOAuth;
    }

    public String getStreamLabsOAuth(){
        return this.streamLabsOAuth;
    }

    public String getStreamElementsOAuth(){
        return this.streamElementsOAuth;
    }

    public Map<String,Event> getEvents(){
        return this.events;
    }

    public Event getEvent(String eventName){
        return this.events.get(eventName);
    }

    public void setTwitchOAuth(String twitchOAuth){
        this.twitchOAuth = twitchOAuth;
    }

    public void setStreamLabsOAuth(String streamLabsOAuth){
        this.streamLabsOAuth = streamLabsOAuth;
    }

    public void setStreamElementsOAuth(String streamElementsOAuth){
        this.streamElementsOAuth = streamElementsOAuth;
    }

    public void setEvents(Map<String,Event> events){
        this.events = events;
    }

}