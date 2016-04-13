package magic.mirror;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.google.gson.*;
import com.eclipsesource.json.*;
/**
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
    private String hiTemp = "",
                   loTemp = "",
                   currentTemp = "",
                   conditions = "",
                   precipitation = "";
    private Gson data;
    private ForecastIO fio;
    private FIOCurrently current;
    private FIODaily daily;
    private String apiKey = "b7b8655200fa16e0ad22c7dd6604dce5";
    
    /**
     * @author Chris
     */
    public Weather(){
        fio = new ForecastIO(apiKey);
        setupFIO();
        location = new Location();
        getWeather();
        setWeather();
    }
    
    /**
     * 
     * @param data, a Gson object containing the location information.
     */
    public Weather(Gson data){
        this.data = data;
        location = new Location(this.data);
        fio = new ForecastIO(apiKey);
        setupFIO();
        getWeather();
        setWeather();
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
        precipitation = Double.toString(daily.getDay(0).precipProbability() * 100);
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
     * @author Chris
     * 
     */
    public void printCurrently(){
        System.out.print("Current Conditions: " + conditions);
        System.out.print("\nCurrent Temp: " + currentTemp);
        System.out.print("\nHi temp: " + hiTemp);
        System.out.print("\nLo Temp: " + loTemp);
        System.out.print("\nPrecipitation: " + precipitation + "%");
    }
    
    /**
     * 
     * @return String containing the high temp for the day.
     */
    public String getHiTemp(){
        return hiTemp;
    }
    
    /**
     * 
     * @return String containing the low temperature for the day.
     */
    public String getLoTemp(){
        return loTemp;
    }
    
    /**
     * 
     * @return String containing the current temperature.
     */
    public String getCurrentTemp(){
        return currentTemp;
    }
    
    /**
     * 
     * @return String containing the conditions for the day.
     */
    public String getConditions(){
        return conditions;
    }
    
    /**
     * 
     * @return String containing the precipitation percentage.
     */
    public String getPrecipitation(){
        return precipitation;
    }
}
