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


/** used for get the Tweets from a Twitter Account*/
public class TwitterMessage {
/**
	String UserName: 	account name which we want to get tweets from 
	List<String> list: 	store all Tweets of specify Twitter account
	Handler handler :	 handle the message when it finish receiving the Tweets
*/
    final static String UserName = "cnn";
    private List<String> list;
    String consumerKey ;
    String consumerSecret;
    String accessToken ;
    String accessTokenSecret;
    List<String> accountList;
    private boolean accountState = false;
    private String tweet;
    
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            tweet = bundle.getString("myKey");
            accountState = true;
            System.out.println("!!!!!!!twitter message!!!!!!"+string);
        }
    };




    public TwitterMessage(String consumerKey,String consumerSecret,String accessToken,String accessTokenSecret){
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken= accessToken;
        this.accessTokenSecret=accessTokenSecret;

  /*     consumerKey = "bUh6sDhIGpN4UdE55litSTD8W";
         consumerSecret = "OlByFoaS9lJ8ewZEw9DOPGgVrby9EM6SepllWXrCnraw49r9DC";
         accessToken = "4889865377-JoGReMh6w6yS2PQQ8hKcVpHlaIKRH1gM4vGf6ui";
         accessTokenSecret = "qIvONWidLP10yKXsYyHfu1k3yzppUpxhbUc7TucF3bpp6";*/
    }

    /**this will use the accoutn info and try to call getTweet()**/
    public void changeAccount(String consumerKey,String consumerSecret,String accessToken,String accessTokenSecret){
        accountState = false;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken= accessToken;
        this.accessTokenSecret=accessTokenSecret;
    }


    public boolean getAccountState(){
        return accountState;
    }

    public String returnTweet() {
        return tweet;
    }

/**get tweets from the network and send it to the handler*/
    public void getTweet(){
        list = new ArrayList<String>();
            Runnable runnable = new Runnable() {
                public void run() {
                    try {

                        ConfigurationBuilder cb = new ConfigurationBuilder();
                        cb.setOAuthConsumerKey(consumerKey);
                        cb.setOAuthConsumerSecret(consumerSecret);
                        cb.setOAuthAccessToken(accessToken);
                        cb.setOAuthAccessTokenSecret(accessTokenSecret);
                        Bundle bundle = new Bundle();
                        Message msg = handler.obtainMessage();

                        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

                        //*****the endpoint to retrieve followers IDs is limited to 15 calls per 15 minutes
                        List<Status> statuses = twitter.getHomeTimeline(new Paging(1, 5));

                        for (Status statue:statuses ){
                            String str = statue.getText()+ " ";
                            str = str.replaceAll("https:.*?\\s", " ");
                            str = statue.getUser().getName() + ":   " + str;
                            list.add(str);
                        };

                        bundle.putString("myKey", list.toString());
                        msg.setData(bundle);
                        handler.sendMessage(msg);

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
