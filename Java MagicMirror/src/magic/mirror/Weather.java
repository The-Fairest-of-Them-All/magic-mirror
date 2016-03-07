package magic.mirror;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.github.dvdme.ForecastIOLib.FIODaily;
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
    ForecastIO fio;
    FIOCurrently current;
    FIODaily daily;
    private String apiKey = "b7b8655200fa16e0ad22c7dd6604dce5";
        
    public Weather(){
        fio = new ForecastIO(apiKey);
        setupFIO();
        location = new Location();
        getWeather();
    }
    
    public Weather(Gson data){
        this.data = data;
        location = new Location(this.data);
        fio = new ForecastIO(apiKey);
        setupFIO();
        getWeather();
    }
    
    private void setupFIO(){
        fio.setLang(ForecastIO.LANG_ENGLISH);
        fio.setUnits(ForecastIO.UNITS_US);
    }
    
    private void getWeather(){
        fio.getForecast(location.latitude, location.longitude);
        current = new FIOCurrently(fio);
        daily = new FIODaily(fio);
    }
}
