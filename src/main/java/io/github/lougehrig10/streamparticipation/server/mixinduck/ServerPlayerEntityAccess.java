package io.github.lougehrig10.streamparticipation.server.mixinduck;

import io.github.lougehrig10.streamparticipation.common.config.Event;

import java.util.Map;

public interface ServerPlayerEntityAccess {

    public void setOAuth(String OAuth) throws Exception;
    public Event getBitEvent(String event);
    public Event getPointEvent(String event);
    public String getChannelId();
    public String getOAuth();
    public void updateConfig(Map<String, Event> bitEvents, Map<String, Event> pointEvents);
    public void updateBitConfig(Map<String, Event> bitEvents);
    public void updatePointConfig(Map<String, Event> pointEvents);
}
