package magic.mirror;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.google.gson.*;
import com.eclipsesource.json.*;
/*
 * @author chris
 */
public class Weather {
    private Location location;
    private String hiTemp = "",
                   loTemp = "",
                   currentTemp = "",
                   conditions = "",
                   percipitation = "";
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
  //      setWeather();
    }
    
    public Weather(Gson data){
        this.data = data;
        location = new Location(this.data);
        fio = new ForecastIO(apiKey);
        setupFIO();
        getWeather();
        setWeather();
    }
    
    private void setupFIO(){
        fio.setLang(ForecastIO.LANG_ENGLISH);
        fio.setUnits(ForecastIO.UNITS_US);
    }
    
    private void getWeather(){
        fio.getForecast(location.latitude, location.longitude);
        current = new FIOCurrently(fio);
    }
    
    private void setWeather(){
        hiTemp = Double.toString(current.get().temperatureMax());
        loTemp = current.get().temperatureMin().toString();
        currentTemp = current.get().temperature().toString();
        //conditions = current.get().;
        percipitation = current.get().precipProbability().toString() + "%";
    }
    
    public void printCurrently(){
//        //Print currently data
//        System.out.println("\nCurrently\n");
//        String [] f  = current.get().getFieldsArray();
//        for(int i = 0; i<f.length;i++)
//        System.out.println(f[i]+": "+current.get().getByKey(f[i]));
        System.out.println("Hi: " + hiTemp);
        System.out.print(current.get().temperature());
//        System.out.println("Lo: ");
//        System.out.print(loTemp);
//        System.out.println("Current: ");
//        System.out.print(currentTemp);
//        System.out.println("Conditions: ");
//        System.out.print(conditions);
//        System.out.println("Precip: ");
//        System.out.print(percipitation);
    }
}
