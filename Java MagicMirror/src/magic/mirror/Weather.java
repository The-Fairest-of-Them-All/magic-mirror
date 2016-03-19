package magic.mirror;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.google.gson.*;
import com.eclipsesource.json.*;
/**
 * @author chris
 */
public class Weather {
    private Location location;
    private String hiTemp = "",
                   loTemp = "",
                   currentTemp = "",
                   conditions = "",
                   precipitation = "";
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
        setWeather();
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
        daily = new FIODaily(fio);
    }
    /**
      * Keys in Current:
      * summary
      * percipProbability
      * visibility
      * percipIntensity
      * percipIntesityError
      * icon
      * cloudCover
      * windBearing
      * apparentTemperature
      * pressure
      * dewPoint
      * ozone
      * nearestStormDistance
      * percipType
      * temperature
      * humidity
      * time
      * windSpeed
      */
    private void setWeather(){
        currentTemp = current.get().temperature().toString();   //This works
        conditions = daily.getDay(0).getByKey("summary");       //This works
        hiTemp = daily.getDay(0).getByKey("temperatureMax");
        loTemp = daily.getDay(0).getByKey("temperatureMin");
        precipitation = Double.toString(daily.getDay(0).precipProbability() * 100) + "%";
    }
    /**
     * Keys in Daily:
     * apparentTemperatureMinTime
     * apparentTemperatureMin
     * apparentTemperatureMaxTime
     * apparentTemperatureMax
     * cloudCover
     * dewPoint
     * humidity
     * icon
     * moonPhase
     * ozone
     * precipIntensityMaxTime
     * precipIntensityMax
     * precipIntensity
     * precipType
     * pressure
     * temeratureMinTime
     * temperatureMin
     * temperatureMaxTime
     * temperatureMax
     * summary
     * sunriseTime
     * sunsetTime
     * time
     * visibility
     * windBearing
     * windSpeed
     */
    public void printCurrently(){
        System.out.print("Current Conditions: " + conditions);
        System.out.print("\nCurrent Temp: " + currentTemp);
        System.out.print("\nHi temp: " + hiTemp);
        System.out.print("\nLo Temp: " + loTemp);
        System.out.print("\nPrecipitation: " + precipitation);
    }
    
    public String getHiTemp(){
        return hiTemp;
    }
    
    public String getLoTemp(){
        return loTemp;
    }
    
    public String getCurrentTemp(){
        return currentTemp;
    }
    
    public String getConditions(){
        return conditions;
    }
    
    public String precipitation(){
        return precipitation;
    }
}
