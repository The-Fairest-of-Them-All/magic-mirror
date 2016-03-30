package fairestintheland.magicmirror;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class TwitterLogActivity extends AppCompatActivity {

    static final int TwitterAccount_REQUEST_CODE = 1111;
        private Button backButton;
    private Button submitButton;
    Context context;
    String consumerKey;
    String consumerSecret;
    String accessToken;
    String accessTokenSecret;
    boolean correctInfo = false;
    TwitterMessage t;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String stringMess = bundle.getString("myKey");
            try{
                Toast.makeText(context,stringMess, Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.twitter_layout);
            context= this;


            backButton = (Button)findViewById(R.id.backbutton);
            View.OnClickListener oclB = new View.OnClickListener(){
                public void onClick(View v){
                    Intent activityAIntent = new Intent (TwitterLogActivity.this,MainActivity.class);
                    activityAIntent.putExtra("accountState",String.valueOf(correctInfo));
                    activityAIntent.putExtra("consumerKey",consumerKey);
                    activityAIntent.putExtra("consumerSecret",consumerSecret);
                    activityAIntent.putExtra("accessToken", accessToken);
                    activityAIntent.putExtra("accessTokenSecret", accessTokenSecret);
                    setResult(RESULT_OK, activityAIntent);
                  finish();

                }
            };
            backButton.setOnClickListener(oclB);

            submitButton = (Button)findViewById(R.id.submitbutton);
            View.OnClickListener oclsubmit = new View.OnClickListener(){
                public void onClick(View v) {
                    EditText ck = (EditText) findViewById(R.id.ConsumerKey_editText);
                    EditText cs = (EditText) findViewById(R.id.ConsumerSecret_editText);
                    EditText at = (EditText) findViewById(R.id.AccessToken_editText);
                    EditText as = (EditText) findViewById(R.id.AccessTokenSecret_editText);


                    consumerKey = ck.getText().toString();
                    consumerSecret = cs.getText().toString();
                    accessToken = at.getText().toString();
                    accessTokenSecret = as.getText().toString();


              //      consumerKey = "bUh6sDhIGpN4UdE55litSTD8W";
               //     consumerSecret = "OlByFoaS9lJ8ewZEw9DOPGgVrby9EM6SepllWXrCnraw49r9DC";
               //     accessToken = "4889865377-JoGReMh6w6yS2PQQ8hKcVpHlaIKRH1gM4vGf6ui";
               //     accessTokenSecret = "qIvONWidLP10yKXsYyHfu1k3yzppUpxhbUc7TucF3bpp6";

                    t = new TwitterMessage(consumerKey, consumerSecret, accessToken, accessTokenSecret);
                    t.getTweet();

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                                correctInfo = t.getAccountState();
                                System.out.println("!!!!correctInfo In twitter layout!!!!   " + correctInfo);

                            Bundle bundle = new Bundle();
                            String stringMess = "Wrong Account Info!";
                            if(correctInfo){
                                stringMess ="Correct Account Info!";
                            }
                            bundle.putString("myKey",stringMess);
                            Message msg = handler.obtainMessage();
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }, 1000);
                }
            };

            submitButton.setOnClickListener(oclsubmit);

        }


}
