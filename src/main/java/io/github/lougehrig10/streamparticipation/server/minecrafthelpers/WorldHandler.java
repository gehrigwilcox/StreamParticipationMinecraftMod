package io.github.lougehrig10.streamparticipation.server.minecrafthelpers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class WorldHandler {

    public static void runCommand(ServerPlayerEntity player, String command){
        player.getServer().getCommandManager().execute(player.getCommandSource().withLevel(4),command);
    }
}
