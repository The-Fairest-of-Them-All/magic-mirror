package magic.mirror;

/**
 *
 * @author chris
 */
public class Helpful_Hints {
    int rainChance, tempHi, tempLo, currentTemp;
    boolean rainBool = false;
    String conditions = "";
    Weather weather;
    
    /**
     *
     * @author Chris
     */
    public Helpful_Hints(){
        
    }
    
    /**
     *
     * @param weather to be interpreted.
     * @author Chris
     */
    public Helpful_Hints(Weather weather){
        this.weather = weather;
        init();
    }
    
    private void init(){
        rainChance = toInt(weather.getPrecipitation()); 
        conditions = weather.getConditions();
        tempHi = toInt(weather.getHiTemp());
        tempLo = toInt(weather.getLoTemp());
        currentTemp = toInt(weather.getCurrentTemp());
        if(conditions.contains("rain") || conditions.contains("Rain")){
            rainBool = true;
        }
    }
    
    private String chooseStatement(){
        String help = "";
        if(rainBool){
            help = "Bring an umbrella today :/)";
        }
        else if(rainChance > 40){
            help = "Think about wearing a raincoat :/)";
        }
        else if(tempHi > 63 && !rainBool){
            help = "It's going to be a beautiful day!";
        }
        else if(currentTemp < 63){
            help = "You might want a jacket";
        }
        else if (tempHi < 40){
            help = "Bundle up, it's a bit chilly!";            
        }
        return help;
    }
    
    private int toInt(String s){
        int ans = Integer.parseInt(s);
        return ans;
    }
    
    /**
     *
     * @return String to be printed. Advises the user on what to wear.
     * @author Chris
     */
    public String getStatement(){
        return chooseStatement();
    }
}
