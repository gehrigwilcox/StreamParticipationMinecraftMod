package io.github.lougehrig10.streamparticipation.server.bitserver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.github.lougehrig10.streamparticipation.common.EnvVariables;
import io.github.lougehrig10.streamparticipation.common.config.Event;
import io.github.lougehrig10.streamparticipation.server.StreamParticipationCommon;
import io.github.lougehrig10.streamparticipation.server.minecrafthelpers.WorldHandler;
import io.github.lougehrig10.streamparticipation.server.mixinduck.ServerPlayerEntityAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class VerificationServerConnection extends WebSocketClient {

    static URI serverUri;

    ServerPlayerEntity player;


    static {
        try {
            serverUri = new URI(EnvVariables.verificationServerURL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public VerificationServerConnection(PlayerEntity player, Map<String,String> httpHeaders){
        super(serverUri,httpHeaders);
        this.player = (ServerPlayerEntity) player;
        connect();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        /*  On connect, send player config  */
        send("config " + new Gson().toJson(StreamParticipationCommon.configManager.getEvents(player.getUuidAsString())));
    }

    @Override
    public void onMessage(String message) {

        if(message.startsWith("redeem ")){
            try{
                message = message.replace("redeem ","");

                JsonObject json = new Gson().fromJson(message, JsonObject.class);

                Event event = ((ServerPlayerEntityAccess)player).getBitEvent(json.get("event").getAsString());

                // Make sure payment cost equals event cost
                if(!event.isEnabled() || event.getCost() != json.get("cost").getAsInt()) return;


                /*WorldHandler.runCommand(player,"/data modify storage streamparticipation userDisplayName set value " + json.get("user").getAsString());
                WorldHandler.runCommand(player,"/data modify storage streamparticipation rewardCost set value " + event.getCost());
                String[] temp = json.get("event").getAsString().split("/");
                WorldHandler.runCommand(player,"/data modify storage streamparticipation rewardTitle set value " + temp[temp.length-1].replaceAll("\\s+",""));*/
                WorldHandler.runCommand(player,StreamParticipationCommon.configManager.getAllowedEvents().get(json.get("event").getAsString()).getCommand());
                /*WorldHandler.runCommand(player,"/data remove storage streamparticipation userDisplayName");
                WorldHandler.runCommand(player,"/data remove storage streamparticipation rewardCost");
                WorldHandler.runCommand(player,"/data remove storage streamparticipation rewardTitle");*/
            }catch (NullPointerException e){}

        }else if(message.startsWith("newConfig ")){
            message = message.replace("newConfig ","");

            Gson gson = new Gson();

            Map<String, Event> bits = gson.fromJson(message,new TypeToken<Map<String,Event>>(){}.getType());

            ((ServerPlayerEntityAccess)player).updateBitConfig(bits);
        }else if(message.startsWith("getConfig")){
            send("config " + new Gson().toJson(StreamParticipationCommon.configManager.getEvents(player.getUuidAsString())));
        }else if(message.startsWith("ping")){
            send("pong");
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

    public void disconnect(){

        this.close();
    }
}
