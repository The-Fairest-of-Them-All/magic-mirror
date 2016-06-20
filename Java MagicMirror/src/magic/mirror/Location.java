package magic.mirror;

import com.google.gson.*;

/*
 * location object which used for getting the current Location
 */
public class Location {
	
	/**
	String currentLongitude: 	longitude of the current location
	String currentLatitude: 	latitude of the current location

	*/
	
	
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
    
	/**update Longitude*/
    private void setLongitude(){
        //TODO Parse GSON OBJ
    }
    
	/**update latitude*/
    private void setLatitude(){
        //TODO Parse GSON OBJ
    }
    
	/**return Longitude*/
    public String getLongitude(){
        return longitude;
    }
    
	/**return latitude*/
    public String getLattitude(){
        return latitude;
    }
}
