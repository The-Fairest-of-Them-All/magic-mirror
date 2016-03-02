package magic.mirror;

import com.google.gson.*;
        
/*
 * @author chris
 */
public class Weather {
    private Location location;
    private int hiTemp,
                loTemp,
                currentTemp;
    private Gson data;
    
    private String apiKey = "b7b8655200fa16e0ad22c7dd6604dce5";
        
    public Weather(){
        location = new Location();
    }
    
    public Weather(Gson data){
        this.data = data;
        location = new Location(this.data);
    }
}
