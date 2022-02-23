package io.github.lougehrig10.streamparticipation.common.twitch;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ChannelPointReward {

    String id;
    User user;
    String channel_id;
    String redeemed_at;
    Reward reward;
    String user_input;
    String status;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public String getRedeemed_at() {
        return redeemed_at;
    }

    public Reward getReward() {
        return reward;
    }

    public String getUser_input() {
        return user_input;
    }

    public String getStatus() {
        return status;
    }

    public static class User {
        String id;
        String login;
        String display_name;

        public String getId() {
            return id;
        }

        public String getLogin() {
            return login;
        }

        public String getDisplay_name() {
            return display_name;
        }
    }

    public static class Reward {
        String id;
        String channel_id;
        String title;
        String prompt;
        int cost;
        boolean is_user_input_required;
        boolean is_sub_only;
        Image image;
        Image default_image;
        String backgroundColor;
        boolean is_enabled;
        boolean is_paused;
        boolean is_in_stock;
        MaxPerStream max_per_stream;
        boolean should_redemptions_skip_request_queue;

        public String getId() {
            return id;
        }

        public String getChannel_id() {
            return channel_id;
        }

        public String getTitle() {
            return title;
        }

        public String getPrompt() {
            return prompt;
        }

        public int getCost() {
            return cost;
        }

        public boolean is_user_input_required() {
            return is_user_input_required;
        }

        public boolean is_sub_only() {
            return is_sub_only;
        }

        public Image getImage() {
            return image;
        }

        public Image getDefault_image() {
            return default_image;
        }

        public Color getBackgroundColor() {
            return Color.decode(backgroundColor);
        }

        public boolean is_enabled() {
            return is_enabled;
        }

        public boolean is_paused() {
            return is_paused;
        }

        public boolean is_in_stock() {
            return is_in_stock;
        }

        public MaxPerStream getMax_per_stream() {
            return max_per_stream;
        }

        public boolean should_redemptions_skip_request_queue() {
            return should_redemptions_skip_request_queue;
        }

        public static class Image {
            String url_1x;
            String url_2x;
            String url_4x;

            public URL getUrl_1x() throws MalformedURLException {
                return new URL(url_1x);
            }

            public URL getUrl_2x() throws MalformedURLException {
                return new URL(url_2x);
            }

            public URL getUrl_4x() throws MalformedURLException {
                return new URL(url_4x);
            }
        }

        public static class MaxPerStream {
            boolean is_enabled;
            int maxPerStream;

            public boolean isIs_enabled() {
                return is_enabled;
            }

            public int getMaxPerStream() {
                return maxPerStream;
            }
        }
    }
}