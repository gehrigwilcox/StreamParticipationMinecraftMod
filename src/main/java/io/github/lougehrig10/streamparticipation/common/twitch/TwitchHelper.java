package io.github.lougehrig10.streamparticipation.common.twitch;

import com.google.gson.*;
import io.github.lougehrig10.streamparticipation.common.EnvVariables;
import io.github.lougehrig10.streamparticipation.common.registry.Exceptions;

import java.util.HashMap;
import java.util.Map;

public class TwitchHelper {

    static String[] requiredScopes = {
            "channel:read:redemptions" // Allow reading of Channel Point Redemptions
        };

    /*
    *
    * Given OAuth, will validate that user for OAuth key can use bits and channel points, has all necessary scopes, and
    * returns channel id
    *
    * */
    public static String validateOAuth(String OAuth) throws Exception{
        Map<String, String> headers = new HashMap<>();

        headers.put("Authorization", "OAuth " + OAuth);

        String message = HttpRequestHelper.httpRequest(EnvVariables.twitchOauthURL+"/validate", new HashMap<>(), headers);

        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonMessage = jsonParser.parse(message).getAsJsonObject();

            // Ensure that required scopes are enabled
            JsonArray scopes = jsonMessage.get("scopes").getAsJsonArray();
            for(String s : requiredScopes){
                if(!scopes.contains(new JsonPrimitive(s))) throw Exceptions.INVALID_OAUTH;
            }

            // Get associated Channel ID
            String channelId = jsonMessage.get("user_id").getAsString();
            if(channelId == null || channelId == "") throw Exceptions.INVALID_OAUTH;

            // TODO: Verify that Channel ID can use bits and channel points

            return channelId;
        }catch(Exception e){
            throw Exceptions.INVALID_OAUTH;
        }
    }

    public static String getOAuthURL(){
        return EnvVariables.twitchOauthURL + "/authorize" +
                        "?client_id=" + EnvVariables.applicationClientId +
                        "&redirect_uri=http://localhost" +
                        "&response_type=token" +
                        "&scope=" + String.join("%20",requiredScopes);
    }




    public static void main(String[] args){
        try {
            System.out.println(getOAuthURL());
            System.out.println(validateOAuth("n94tq38xnga24vq7tyunud2xzuibwx"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
