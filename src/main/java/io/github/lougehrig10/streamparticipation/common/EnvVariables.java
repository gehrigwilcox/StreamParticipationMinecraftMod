package io.github.lougehrig10.streamparticipation.common;

public class EnvVariables {

    public static final boolean DEV_MODE = false;

    public static final String verificationServerURL = DEV_MODE ? "ws://localhost:8080" : "ws://sp-verification-server.herokuapp.com";
    public static final String twitchCustomRewardsURL = "https://api.twitch.tv/helix/channel_points/custom_rewards";
    public static final String twitchOauthURL = "https://id.twitch.tv/oauth2";
    public static final String extensionClientId = "4q5z8e66v37d6my1ip3cn15qnxmlf3";
    public static final String applicationClientId = "b9vaj9sp5tr4zdzvqshr6n0lrypqo6";

}
