package io.github.lougehrig10.streamparticipation.server.mixins;

import com.mojang.authlib.GameProfile;
import io.github.lougehrig10.streamparticipation.common.config.Event;
import io.github.lougehrig10.streamparticipation.server.StreamParticipationCommon;
import io.github.lougehrig10.streamparticipation.server.bitserver.VerificationServerConnection;
import io.github.lougehrig10.streamparticipation.server.mixinduck.ServerPlayerEntityAccess;
import io.github.lougehrig10.streamparticipation.common.twitch.TwitchHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerEntityAccess {

    @Shadow public abstract void sendMessage(Text message, boolean actionBar);

    Map<String, Event> bitEvents;
    Map<String, Event> pointEvents;
    String OAuth;
    String channelId;

    //ChannelPointsListener cpListener;
    VerificationServerConnection vsConnection;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    /*
    *
    *   Verifies that OAuth is valid and recieves channelID.
    *   Connects to Verification server and Channel Point Listener.
    *
    *   The server connections are websocket connections. We attach these connections with the
    *   PlayerEntity Object so that the websocket connections end when the PlayerEntity object disappears.
    *
    *   Bit Events and Point Events get loaded from the stored config file
    *
    * */
    public void setOAuth(String OAuth) throws Exception{

        //if(cpListener != null) cpListener.disconnect();

        if(vsConnection != null) vsConnection.disconnect();

        this.channelId = TwitchHelper.validateOAuth(OAuth);
        this.OAuth = OAuth;

        //this.cpListener = new ChannelPointsListener(this);

        Map<String,String> headers = new HashMap<>();
        headers.put("OAuth",OAuth);

        this.vsConnection = new VerificationServerConnection(this,headers);
        updateBitConfig(StreamParticipationCommon.configManager.getEvents(this.getUuidAsString()));
    }

    public Event getBitEvent(String event){
        return bitEvents.get(event);
    }

    public Event getPointEvent(String event){
        return pointEvents.get(event);
    }

    public String getChannelId(){
        return channelId;
    }

    public String getOAuth(){
        return OAuth;
    }

    public void updateConfig(Map<String, Event> bitEvents, Map<String, Event> pointEvents){
        updateBitConfig(bitEvents);
        updatePointConfig(pointEvents);
    }

    public void updateBitConfig(Map<String, Event> bitEvents){
        this.bitEvents = bitEvents;
        StreamParticipationCommon.configManager.setEvents(this.getUuidAsString(),bitEvents);
    }
    public void updatePointConfig(Map<String, Event> pointEvents){
        this.pointEvents = pointEvents;
    }

    @Inject(method="onDisconnect()V",at=@At("RETURN"))
    public void onOnDisconnect(CallbackInfo info){
        // When player disconnects, close websocket connections so garbage collector can clean up
        //if(cpListener != null) cpListener.disconnect();
        if(vsConnection != null) vsConnection.disconnect();
    }
}
