package io.github.lougehrig10.streamparticipation.common.registry;

import net.minecraft.util.Identifier;

public class Identifiers {

    public static final String NAMESPACE = "streamparticipation";

    public static final Identifier registerUserPacket = new Identifier(NAMESPACE,"register");
    public static final Identifier updateFunctionsPacket = new Identifier(NAMESPACE,"update_functions");
    public static final Identifier updateEventsPacket = new Identifier(NAMESPACE,"update_event");



}
