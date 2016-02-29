package magic.mirror;

import com.google.gson.*;

/*
 * @author chris
 */
public class Location {
    public double longitude,
                   latitude;
    private double testLong = 39.9500, //This is the location of Philadelphia, PA
                   testLati = 75.1667; //This is used for test purposes
    private Gson data;
    
    public Location(){
        //-----TEST CODE--------------------------------------------------------
        longitude = testLong;
        latitude = testLati;
        //-----END TEST CODE----------------------------------------------------
    }
    
    public Location(Gson data){
        this.data = data;
        setLongitude();
        setLatitude();
    }
    
    private void setLongitude(){
        //TODO Parse GSON OBJ
    }
    
    private void setLatitude(){
        //TODO Parse GSON OBJ
    }
    
    public double getLongitude(){
        return longitude;
    }
    
    public double getLattitude(){
        return latitude;
    }
}
