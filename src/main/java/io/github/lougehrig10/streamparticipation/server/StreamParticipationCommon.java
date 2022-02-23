package io.github.lougehrig10.streamparticipation.server;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.lougehrig10.streamparticipation.common.config.ConfigFile;
import io.github.lougehrig10.streamparticipation.common.config.ConfigManager;
import io.github.lougehrig10.streamparticipation.common.registry.Identifiers;
import io.github.lougehrig10.streamparticipation.common.twitch.TwitchHelper;
import io.github.lougehrig10.streamparticipation.server.mixinduck.ServerPlayerEntityAccess;
import io.github.lougehrig10.streamparticipation.server.packet.RegisterPlayerPacketHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.GameRules;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class StreamParticipationCommon implements ModInitializer {

    public static ConfigManager configManager;


    @Override
    public void onInitialize() {
        // Register packets
        //initPacketHandling();

        configManager = new ConfigManager();


        ServerPlayConnectionEvents.JOIN.register((handler,sender,server)->{
            String OAuth = configManager.getOAuth(handler.player.getUuidAsString());

            try {
                ((ServerPlayerEntityAccess) handler.player).setOAuth(OAuth);
            }catch(Exception e){
                ClickEvent goToHelpLink = new ClickEvent(ClickEvent.Action.OPEN_URL,"https://id.twitch.tv/oauth2/authorize?client_id=b9vaj9sp5tr4zdzvqshr6n0lrypqo6&redirect_uri=https://lougehrig10.github.io/StreamParticipationOAuthPage/&response_type=token&scope=channel:manage:redemptions%20channel:read:redemptions%20user:read:broadcast%20user:edit:broadcast");

                Style messageStyle = Style.EMPTY.withBold(true).withColor(Formatting.DARK_RED).withClickEvent(goToHelpLink);

                Text message = new LiteralText("Invalid OAuth. Please click here to get an OAuth Key.").setStyle(messageStyle);

                handler.player.sendMessage(message,false);
            }
        });

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("configSP").then(CommandManager.argument("OAuthKey", StringArgumentType.word()).executes(ctx -> {

                String OAuth = StringArgumentType.getString(ctx,"OAuthKey");

                configManager.registerOAuth(ctx.getSource().getPlayer().getUuidAsString(),OAuth);

                try {
                    ((ServerPlayerEntityAccess) ctx.getSource().getPlayer()).setOAuth(OAuth);
                }catch(Exception e){
                    ClickEvent goToHelpLink = new ClickEvent(ClickEvent.Action.OPEN_URL,"https://id.twitch.tv/oauth2/authorize?client_id=b9vaj9sp5tr4zdzvqshr6n0lrypqo6&redirect_uri=https://lougehrig10.github.io/StreamParticipationOAuthPage/&response_type=token&scope=channel:manage:redemptions%20channel:read:redemptions%20user:read:broadcast%20user:edit:broadcast");

                    Style messageStyle = Style.EMPTY.withBold(true).withColor(Formatting.DARK_RED).withClickEvent(goToHelpLink);

                    Text message = new LiteralText("Invalid OAuth. Please click here to get an OAuth Key.").setStyle(messageStyle);

                    ctx.getSource().getPlayer().sendMessage(message,false);
                }

                return 1;
            })));
        }));


        Timer time = new Timer();
        KeepAliveTask task = new KeepAliveTask();

        time.schedule(task,0,25*60000);

    }

    private void initPacketHandling(){


        RegisterPlayerPacketHandler.register();

    }
}

class KeepAliveTask extends TimerTask {
    public void run(){
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL("https://sp-verification-server.herokuapp.com/keepAlive").openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.getResponseCode();
            //conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}