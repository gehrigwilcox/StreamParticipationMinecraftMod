package io.github.lougehrig10.streamparticipation.common.config;

public class Event {

    boolean enabled = true;
    int cost = 100;
    String command = "/say Hello World!";

    public Event(boolean enabled, int cost, String command){
        this.enabled = enabled;
        this.cost = cost;
        this.command = command;
    }

    public Event(int cost, String command){
        this(true,cost,command);
    }

    public boolean isEnabled() { return enabled; }

    public int getCost(){
        return cost;
    }

    public String getCommand(){
        return command;
    }
}
