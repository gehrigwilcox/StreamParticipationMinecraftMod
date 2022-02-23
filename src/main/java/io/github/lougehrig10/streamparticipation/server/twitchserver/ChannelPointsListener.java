package io.github.lougehrig10.streamparticipation.server.twitchserver;

import com.google.gson.*;
import io.github.lougehrig10.streamparticipation.common.twitch.ChannelPointReward;
import io.github.lougehrig10.streamparticipation.server.minecrafthelpers.WorldHandler;
import io.github.lougehrig10.streamparticipation.server.mixinduck.ServerPlayerEntityAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

public class ChannelPointsListener extends WebSocketClient {


    static String[] topics = {
            "channel-points-channel-v1.%s", // Get notified when custom reward is redeemed
    };
    static URI twitchWSURI;


    boolean receivePong = false;
    ServerPlayerEntity player;


    static {
        try {
            twitchWSURI = new URI("wss://pubsub-edge.twitch.tv");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public ChannelPointsListener(PlayerEntity player){
        super(twitchWSURI);
        this.player = (ServerPlayerEntity) player;
        connect();

        /*
        *
        * Setup callback. Send PING once every 4 minutes.
        * If don't recieve PONG within 11 seconds, reconnect
        * as per https://dev.twitch.tv/docs/pubsub#connection-management
        *
        * */

        new Timer().schedule(new TimerTask(){
            @Override
            public void run(){
                if(getReadyState() != ReadyState.OPEN) return;
                receivePong = false;
                send("{\"type\":\"PING\"}");
				new Timer().schedule(new TimerTask(){
					@Override
					public void run(){
						System.out.println("Receive Pong: " + receivePong);
						if(!receivePong) reconnect();
					}
				},11000);
            }
        },0,240000);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

        // Set up registration
        JsonObject data = new JsonObject();
        JsonArray jsonTopicsArray = new JsonArray();
        for(int i = 0; i < topics.length; i++){
            jsonTopicsArray.add(String.format(topics[i],((ServerPlayerEntityAccess)player).getChannelId()));
        }
        data.add("topics",jsonTopicsArray);
        data.addProperty("auth_token",((ServerPlayerEntityAccess)player).getOAuth());

        JsonObject message = new JsonObject();
        message.addProperty("type", "LISTEN");
        message.addProperty("nonce", "streamparticipation");
        message.add("data",data);

        send(new GsonBuilder().create().toJson(message));
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
        JsonParser parser = new JsonParser();
        JsonObject jsonMessage = parser.parse(message).getAsJsonObject();

        String type = jsonMessage.get("type").getAsString();

        // PONG response
        if(type.equals("PONG")) receivePong = true;

        // if response from twitch, make sure it isn't an error
        if(type.equals("RESPONSE")) {
            if (jsonMessage.get("error").getAsString().equals("")) return;
            System.out.println("ERROR FROM TWITCH: " + jsonMessage.get("error").getAsString());
            return;
        }

        // If message from twitch, send to appropriate handler
        if(type.equals("MESSAGE")){
            JsonObject jsonData = jsonMessage.get("data").getAsJsonObject();
            String topic = jsonData.get("topic").getAsString();
            JsonObject jsonDataMessage = parser.parse(jsonData.get("message").getAsString()).getAsJsonObject();

            if(topic.startsWith("channel-points-channel")){
                if(jsonDataMessage.get("type").getAsString().equals("reward-redeemed")){
                    redeemChannelPointReward(new Gson().fromJson(jsonDataMessage.get("data").getAsJsonObject().get("redemption"),ChannelPointReward.class));
                }
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection Closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }


    /*
    *
    * When disconnecting, should disable all custom rewards so viewers can't redeem them
    *
    * */
    public void disconnect(){
        this.close();
    }

    // Get relevant data about reward and forward it on to event manager
    private void redeemChannelPointReward(ChannelPointReward reward){

        // Get configured command
        String command = ((ServerPlayerEntityAccess)player).getPointEvent(reward.getId()).getCommand();

        // Make some data about the reward available to the game
        WorldHandler.runCommand(player,"/data modify storage streamparticipation userDisplayName set value " + reward.getUser().getDisplay_name());
        WorldHandler.runCommand(player,"/data modify storage streamparticipation rewardTitle set value " + reward.getReward().getTitle());
        WorldHandler.runCommand(player,"/data modify storage streamparticipation rewardPrompt set value " + reward.getReward().getPrompt());
        WorldHandler.runCommand(player,"/data modify storage streamparticipation rewardCost set value " + reward.getReward().getCost());
        WorldHandler.runCommand(player,"/data modify storage streamparticipation userInput set value " + reward.getUser_input());

        // Run the configured command
        WorldHandler.runCommand(player,command);
    }
}
