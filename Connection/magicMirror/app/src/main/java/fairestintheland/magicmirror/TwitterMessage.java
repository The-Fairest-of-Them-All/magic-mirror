package fairestintheland.magicmirror;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterMessage {

    final static String UserName = "cnn";
    private List<String> list;
    private String tweet;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            for(int i =0;i<10;i++){
                System.out.println("!!!!!!!twitter message!!!!!!"+string);
            }
            tweet = bundle.getString("myKey");
        }
    };

    public String returnTweet() {
        return tweet;
    }

    public void getTweet(){
        list = new ArrayList<String>();


            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                         String consumerKey = "bUh6sDhIGpN4UdE55litSTD8W";
                         String consumerSecret = "OlByFoaS9lJ8ewZEw9DOPGgVrby9EM6SepllWXrCnraw49r9DC";
                         String accessToken = "4889865377-JoGReMh6w6yS2PQQ8hKcVpHlaIKRH1gM4vGf6ui";
                         String accessTokenSecret = "qIvONWidLP10yKXsYyHfu1k3yzppUpxhbUc7TucF3bpp6";
                    ConfigurationBuilder cb = new ConfigurationBuilder();
                    cb.setOAuthConsumerKey(consumerKey);
                    cb.setOAuthConsumerSecret(consumerSecret);
                    cb.setOAuthAccessToken(accessToken);
                    cb.setOAuthAccessTokenSecret(accessTokenSecret);

                        Bundle bundle = new Bundle();
                        Message msg = handler.obtainMessage();

                    Twitter twitter = new TwitterFactory(cb.build()).getInstance();
                    List<Status> statuses = twitter.getHomeTimeline(new Paging(1, 5));

                        for (Status statue:statuses ){
                            String str = statue.getText()+ " ";
                            str = str.replaceAll("https:.*?\\s", " ");
                            str = statue.getUser().getName() + ":   " + str;
                            list.add(str);
                        };

                        bundle.putString("myKey", list.toString());
                        msg.setData(bundle);
                        handler.sendMessage(msg); //received by handleMessage

                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                }
            };
            Thread mythread = new Thread(runnable);
            mythread.start();


//List<Status> statuses = twitter.getUserTimeline(UserName,new Paging(1,3));
//twitter.updateStatus("this is send by Java project! not human");


    }

}
