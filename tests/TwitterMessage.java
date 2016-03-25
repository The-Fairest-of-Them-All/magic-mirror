package twittermessage;

import java.util.List;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterMessage {

    final static String consumerKey = "bUh6sDhIGpN4UdE55litSTD8W";
    final static String consumerSecret = "OlByFoaS9lJ8ewZEw9DOPGgVrby9EM6SepllWXrCnraw49r9DC";
    final static String accessToken = "4889865377-JoGReMh6w6yS2PQQ8hKcVpHlaIKRH1gM4vGf6ui";
    final static String accessTokenSecret = "qIvONWidLP10yKXsYyHfu1k3yzppUpxhbUc7TucF3bpp6";
    final static String UserName = "cnn";

    public static void main(String[] args) {

        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey(consumerKey);
            cb.setOAuthConsumerSecret(consumerSecret);
            cb.setOAuthAccessToken(accessToken);
            cb.setOAuthAccessTokenSecret(accessTokenSecret);

            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
            List<Status> statuses = twitter.getHomeTimeline(new Paging(1, 5));

//List<Status> statuses = twitter.getUserTimeline(UserName,new Paging(1,3));
//twitter.updateStatus("this is send by Java project! not human");
            statuses.stream().forEach((statue) -> {
                String str = statue.getText()+ " ";
                str = str.replaceAll("https:.*?\\s", " ");
                System.out.println(statue.getUser().getName() + ":   " + str);
            });

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
