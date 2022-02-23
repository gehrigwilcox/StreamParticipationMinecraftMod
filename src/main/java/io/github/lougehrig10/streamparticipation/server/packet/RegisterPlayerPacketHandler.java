package io.github.lougehrig10.streamparticipation.server.packet;

import io.github.lougehrig10.streamparticipation.common.registry.Identifiers;
import io.github.lougehrig10.streamparticipation.common.twitch.TwitchHelper;
import io.github.lougehrig10.streamparticipation.server.mixinduck.ServerPlayerEntityAccess;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.nio.charset.StandardCharsets;


public class RegisterPlayerPacketHandler {

    public static void register(){
        ServerPlayNetworking.registerGlobalReceiver(Identifiers.registerUserPacket,(server, player, handler, buf, responseSender)->registerPlayer(server,player,handler,buf,responseSender));
    }

    public static void registerPlayer(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        String oauthKey = buf.readString(32767);

        try {
            ((ServerPlayerEntityAccess)player).setOAuth(oauthKey);
        }catch(Exception e){

            ClickEvent goToHelpLink = new ClickEvent(ClickEvent.Action.OPEN_URL,"https://id.twitch.tv/oauth2/authorize?client_id=b9vaj9sp5tr4zdzvqshr6n0lrypqo6&redirect_uri=https://lougehrig10.github.io/StreamParticipationOAuthPage/&response_type=token&scope=channel:manage:redemptions%20channel:read:redemptions%20user:read:broadcast%20user:edit:broadcast");

            Style messageStyle = Style.EMPTY.withBold(true).withColor(Formatting.DARK_RED).withClickEvent(goToHelpLink);

            Text message = new TranslatableText("streamparticipation.invalidOAuthMessage").setStyle(messageStyle);

            player.sendMessage(message,false);
        }
    }
}
