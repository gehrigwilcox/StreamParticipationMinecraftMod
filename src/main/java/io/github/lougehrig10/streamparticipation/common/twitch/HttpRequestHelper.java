package io.github.lougehrig10.streamparticipation.common.twitch;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestHelper {

    public static String httpRequest(String server, Map<String,String> query, Map<String,String> headers) throws Exception{

        URL url = new URL(query.keySet().stream().map(key -> {
            try {
                return key + "=" + URLEncoder.encode(query.get(key), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }).collect(Collectors.joining("&",server+"?","")));


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        headers.forEach((a,b)->{
            conn.setRequestProperty(a,b);
        });

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();

    }

    public static void httpPatch(String server, String data, Map<String,String> query, Map<String,String> headers) throws Exception{

        URL url = new URL(query.keySet().stream().map(key -> {
            try {
                return key + "=" + URLEncoder.encode(query.get(key), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }).collect(Collectors.joining("&",server,"")));


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PATCH");
        headers.forEach((a,b)->{
            conn.setRequestProperty(a,b);
        });
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        try(OutputStream os = conn.getOutputStream()){
            os.write(data.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void httpPost(String server, String data, Map<String,String> headers) throws Exception{

        HttpURLConnection conn = (HttpURLConnection) new URL(server).openConnection();

        conn.setRequestMethod("POST");
        headers.forEach((a,b)->{
            conn.setRequestProperty(a,b);
        });
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        try(OutputStream os = conn.getOutputStream()){
            os.write(data.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void httpDelete(String server, Map<String,String> query, Map<String,String> headers) throws Exception{
        URL url = new URL(query.keySet().stream().map(key -> {
            try {
                return key + "=" + URLEncoder.encode(query.get(key), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }).collect(Collectors.joining("&",server,"")));


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        headers.forEach((a,b)->{
            conn.setRequestProperty(a,b);
        });
    }

    public static void httpPut(String server, Map<String,String> headers, String data) throws Exception{
        HttpURLConnection conn = (HttpURLConnection) new URL(server).openConnection();

        conn.setRequestMethod("PUT");
        headers.forEach((a,b)->{
           conn.setRequestProperty(a,b);
        });
        conn.setDoOutput(true);
        try(OutputStream os = conn.getOutputStream()){
            os.write(data.getBytes(StandardCharsets.UTF_8));
        }
    }
}
