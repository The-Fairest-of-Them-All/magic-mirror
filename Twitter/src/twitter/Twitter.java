/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;
import twitter4j.*;
import twitter4j.conf.*;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 *
 * @author KatieHorn
 */
public class Twitter {

  
    /**
     * @param args the command line arguments
     
     * 
     * */
    
    protected static twitter4j.Twitter twitter;
    private static ConfigurationBuilder configBuilder;
    
    
            
    public static void main(String[] args) {
         configBuilder = new ConfigurationBuilder();
        configBuilder.setDebugEnabled(true);        
        configBuilder.setOAuthConsumerKey("mjS328CVTGbQJJfRZSlAIEJzz");
        configBuilder.setOAuthConsumerSecret("sWAkGBIy9vC0WNPA4vbyVFWO0SDIDahrMgl1Dh4khgzvumBsFf");
        configBuilder.setOAuthAccessToken("4889865377-ZQZsdEYL0zpmTCRh3O8OjY246lUPJnMgVpyg4DA");
        configBuilder.setOAuthAccessTokenSecret("YuvrYjRKYPEC8cD5CKD2CvZ1zYPItngU0oXdLDjjroah2");
             try {
            // gets Twitter instance with default credentials
            twitter4j.Twitter twitter = new TwitterFactory(configBuilder.build()).getInstance();
            User user = twitter.verifyCredentials();
            List<Status> statuses = twitter.getHomeTimeline();
            System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }
    }


              
    
