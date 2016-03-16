package magic.mirror;

import com.google.gson.*;

/*
 * @author chris
 */
public class Location {
    public String longitude,
                   latitude;
    private double testLati = 39.9819, //This is the location of Philadelphia, PA
                   testLong = -75.1529; //This is used for test purposes
    private Gson data;
    
    public Location(){
        //-----TEST CODE--------------------------------------------------------
        longitude = Double.toString(testLong);
        latitude = Double.toString(testLati);
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
    
    public String getLongitude(){
        return longitude;
    }
    
    public String getLattitude(){
        return latitude;
    }
}
