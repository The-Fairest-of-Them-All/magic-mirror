package JSONClasses;

import com.google.gson.*;

/*
 * location object which used for getting the current Location
 */

/**
 * Used to hold the user's latitude and longitude which are passed by the Android device.
 * @author Owner
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
    
    /**
     * No arg constructor which initializes lat and long to default values.
     * @author Chris
     */
    public Location(){
        //-----TEST CODE--------------------------------------------------------
        longitude = Double.toString(testLong);
        latitude = Double.toString(testLati);
        //-----END TEST CODE----------------------------------------------------
    }
    
    /**
     * Constructor which takes Gson data representing the lat and long.
     * 
     * @param data, a Gson object containing the longitude and latitude.
     * @author Chris
     */
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
    
    /**
     *
     * @param lat
     */
    public void setLatitudeUsingString(String lat) {
        this.latitude = lat;
    }
    
    /**
     *
     * @param longi
     */
    public void setLongitudeUsingString(String longi) {
        this.longitude = longi;
    }
    
	/**
         * 
         * @return Longitude
         */
    public String getLongitude(){
        return longitude;
    }
    
	/**
         * 
         * @return Latitude
         */
    public String getLattitude(){
        return latitude;
    }
}
