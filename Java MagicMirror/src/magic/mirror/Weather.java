package magic.mirror;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.google.gson.*;
        
/*
 * @author chris
 used for receive and store the weather information.
 */
public class Weather {
	
	/**
	Location location:	 current location of the device 
	Int hiTemp: highest temperature of today
	Int lotemp: lowest temperature of today
	currentTemp: current temperature of today
	Gson data: store the data for location
	ForecaseIO fio: contain Forecast setting
	FIOCurrently current: current weather information
	FIODaily daily:  several days’ weather information
	String apiKey: used for access Forecast 
	*/
	
	
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
    
	/**set up FIO features*/
    private void setupFIO(){
        fio.setLang(ForecastIO.LANG_ENGLISH);
        fio.setUnits(ForecastIO.UNITS_US);
    }
    
	/**update the weather information*/
    private void getWeather(){
        fio.getForecast(location.latitude, location.longitude);
        current = new FIOCurrently(fio);
        daily = new FIODaily(fio);
    }
}
