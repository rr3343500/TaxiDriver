// session class to store confidential information

package com.iwish.taxidriver.usersession;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.iwish.taxidriver.Map.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import okhttp3.WebSocket;

/**
 * Created by rrdreamtechnology on 18/01/2020.
 */

public class UserSession {


    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "UserSessionPref";

    // Sharedpref file name
    private static final String TRACKID = "trackid";

    // First time logic Check
    public static final String FIRST_TIME = "firsttime";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    //booking count (make variable public to access from outside)
    public static final int BOOKINGCOUNT = 0;

    //operaterbill (make variable public to access from outside)
    public static final float OPERATERBILL = 0;

    //incentive(make variable public to access from outside)
    public static final float INCENTIVE = 0;

    //total (make variable public to access from outside)
    public static final float TOTAL = 0;

    // Email address (make variable public to access from outside)
    public static final String DRIVERID = "driverid";

    // Email address (make variable public to access from outside)
    public static final String WEBSOCKET = "websocket";

    // Request data (make variable public to access from outside)
    public static final String REQUESTDATA = "requestdata";

    // Mobile number (make variable public to access from outside)
    public static final String KEY_MOBiLE = "mobile";
    // latitute number (make variable public to access from outside)
    public static final String LATITUTE = "latitute";

    // logitute number (make variable public to access from outside)
    public static final String LOGITUTE = "logitute";

    // streetname (make variable public to access from outside)
    public static final String STREETNAME = "streetname";

    // clientid (make variable public to access from outside)
    public static final String CLIENTID = "clientid";

    // clientid (make variable public to access from outside)
    public static final String SETLOCATION = "setlocation";

    // clientid (make variable public to access from outside)
    public static final String PICLAT = "piclat";

    // clientid (make variable public to access from outside)
    public static final String PICLONG = "piclong";

    // clientid (make variable public to access from outside)
    public static final String DROPLAT = "droplat";

    // clientid (make variable public to access from outside)
    public static final String DROPLONG = "droplong";

    // socket onoff (make variable public to access from outside)
    public static final String SOCKETONOFF = "socketonoff";

    //client address (make variable public to access from outside)
    public static final String CLIENTADDRESS = "clientaddress";


    // streetname (make variable public to access from outside)
    public static final String REQUEST = "request";


    // socketconnection (make variable public to access from outside)
    public static final String SOCKETCONNECTION = "socketconnection";


    // socketconnection (make variable public to access from outside)
    public static final String BOOKINGSTATUS = "false";


    // Mobile number (make variable public to access from outside)
    public static final String DUTY_STATUS = "status";

    //otp confirm (make variable public to access from outside)
    public static final String OTPCONFIRM = "otpconfirm";

    // user avatar (make variable public to access from outside)
    public static final String KEY_PHOTO = "photo";

    // check first time app launch
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // user avatar (make variable public to access from outside)
    public static final String RENTALCLIENTID = "rentalclientid";

    // user avatar (make variable public to access from outside)
    public static final String RENTALPICLAT = "rentalpiclat";

    // user avatar (make variable public to access from outside)
    public static final String RENTALPICLONG = "rentalpiclong";

    // user avatar (make variable public to access from outside)
    public static final String RENTALPICCITY = "rentalpiccity";

    // user avatar (make variable public to access from outside)
    public static final String RENTALTIMEDURATION = "rentaltimeduration";

    // booking  type (make variable public to access from outside)
    public static final String BOOKINGTYPE = "bookingtype";

    // booking  type (make variable public to access from outside)
    public static final String ENDTRIPSTATUS = "endtripstatus";

    // booking  type (make variable public to access from outside)
    public static final String ENDTRIPDATA = "endtripdata";

    // Constructor
    public UserSession(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession( String mobile ,String driverid){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
//        editor.putString(KEY_NAME, name);

  //       Storing driverid in pref
        editor.putString(DRIVERID, driverid);

        // Storing phone number in pref
        editor.putString(KEY_MOBiLE, mobile);
        // storing duty status in pref
        editor.putString(DUTY_STATUS,"false");

        // Storing image url in pref
//        editor.putString(KEY_PHOTO, photo);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(this.isLoggedIn()){

            Intent intent= new Intent(context,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(DRIVERID, pref.getString(DRIVERID, null));

        // user phone number
        user.put(KEY_MOBiLE, pref.getString(KEY_MOBiLE, null));

        // user avatar
        user.put(KEY_PHOTO, pref.getString(KEY_PHOTO, null)) ;

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_LOGIN,false);
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public Boolean getFirstTime() {
        return pref.getBoolean(FIRST_TIME, true);
    }

    public void setFirstTime(Boolean n){
        editor.putBoolean(FIRST_TIME,n);
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    /**
     *  get user mobile
     * **/
    public String get_mobile() {
        String usename = pref.getString("mobile","");
        return usename;
    }

    /**
     * set user status
     * **/
    public void set_current_duty_status(String status)
    {
        editor.putString(DUTY_STATUS,status);
        editor.commit();
    }

    /**
     * get user status
     * **/
    public String get_duty_status(){
        return pref.getString(DUTY_STATUS, null);
    }

    /**
     * set current location
     * **/
    public void setLocation(String lat,String lon ,String streetname)
    {
        editor.putString(LATITUTE,lat);
        editor.putString(LOGITUTE,lon);
        editor.putString(STREETNAME,streetname);
    }

    /**
     * get user location latitute
     * **/
    public Double getLatitute()
    {
        return Double.parseDouble(pref.getString(LATITUTE,null));
    }

    /**
     * get user loaction logitute
     * **/
    public Double getLogitute()
    {
        return Double.parseDouble(pref.getString(LOGITUTE,null));
    }

    /**
     * get user street loaction
     * **/
    public String getStreetname()
    {
        return pref.getString(STREETNAME,null);
    }

    /**
     * set Socketconnection
     *
     * @return**/
    public boolean getSocketConnection()
    {
        return Boolean.valueOf(pref.getString(SOCKETCONNECTION,null));
    }

    /**
     * set Socketconnection
     * **/
    public void setSocketConnection(Boolean aBoolean)
    {
        editor.putString(SOCKETCONNECTION,String.valueOf(aBoolean)).commit();
    }


    public void setbooking(String found){
        editor.putString(REQUEST,found).commit();

    }
    public String getbooking() {
        if (pref.getString(REQUEST, null) == "null") {
            return "no";
        }else {

            return pref.getString(REQUEST, null);
        }

    }

    public void setsocket(WebSocket webSocket){
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        WebSocket json = gson.fromJson((JsonReader) webSocket, WebSocket.class);
//        String jsonString = gson.toJson(json);
//        Log.e("tetet",jsonString);
//        editor.putString(WEBSOCKET, jsonString).apply();
    }

    public WebSocket getsocket(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = pref.getString(WEBSOCKET, null);
        return gson.fromJson(json, WebSocket.class);
    }


    public  void  Booking(String booking)
    {
        editor.putString(BOOKINGSTATUS,booking).commit();
    }


    public  Boolean getbookingstatus()
    {
        return Boolean.valueOf(pref.getString(BOOKINGSTATUS,null));
    }

    public  void set_requestdata( String string)
    {
      editor.putString(REQUESTDATA,string).commit();
    }

    public  String getRequestdata()
    {
        if(pref.getString(REQUESTDATA,null)==null) {
            return "null";
        }
        else {
            return pref.getString(REQUESTDATA,null);
        }
    }


    public  void setsocketonoff( String string)
    {
        editor.putString(SOCKETONOFF,string).commit();
    }

    public  Boolean getsocketonoff()
    {
       return Boolean.valueOf(pref.getString(SOCKETONOFF,null));
    }

    public  void setClientid(String client)
    {
        editor.putString(CLIENTID,client).commit();
    }

    public String getClientID()
    {
      return  pref.getString(CLIENTID,null);
    }


    public  void setpiclat(String client)
    {
        editor.putString(PICLAT,client).commit();
    }

    public  void setpiclong(String client)
    {
        editor.putString(PICLONG,client).commit();
    }

    public  void setdroplat(String client)
    {
        editor.putString(DROPLAT,client);
        editor.commit();
    }

    public void setdroplong(String client)
    {
        editor.putString(DROPLONG,client).commit();
    }


    public String getpiclat()
    {
        return  pref.getString(PICLAT,null);
    }
    public String getpiclong()
    {
        return  pref.getString(PICLONG,null);
    }


    public String getdroplat()
    {
        return  pref.getString(DROPLAT,null);
    }
    public String getdroplong()
    {
        return  pref.getString(DROPLONG,null);
    }

    public void setLocationStatus(Boolean setloca)
    {
      editor.putString(SETLOCATION, String.valueOf(setloca)).commit();
    }

    public Boolean getLocationStatus()
    {
        return Boolean.valueOf(pref.getString(SETLOCATION,null));
    }


    public void setClientaddress(String clientaddress)
    {
       editor.putString(CLIENTADDRESS,clientaddress).commit();
    }

    public String getClientaddress()
    {
        return pref.getString(CLIENTADDRESS,null);
    }

    public void setotpconfirm(boolean optconfirm)
    {
       editor.putString(OTPCONFIRM, String.valueOf(optconfirm)).commit();
    }

    public Boolean getotpconfirm()
    {
        return Boolean.valueOf(pref.getString(OTPCONFIRM,null));
    }


    public void setRentaldetail(String clientid ,String piclat, String piclong , String piccity, String time ){

        editor.putString(RENTALCLIENTID,clientid);
        editor.putString(RENTALPICLAT,piclat);
        editor.putString(RENTALPICLONG,piclong);
        editor.putString(RENTALPICCITY,piccity);
        editor.putString(RENTALTIMEDURATION,time);
        // commit changes
        editor.commit();
    }

    public void  setBookingtype(String book)
    {
       editor.putString(BOOKINGTYPE,book).commit();
    }

    public String getBookingtype()
    {
        return pref.getString(BOOKINGTYPE,null);
    }


    public HashMap<String, String> getRentaldetail(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(RENTALCLIENTID, pref.getString(RENTALCLIENTID, null));

        // user email id
        user.put(RENTALPICLAT, pref.getString(RENTALPICLAT, null));

        // user phone number
        user.put(RENTALPICLONG, pref.getString(RENTALPICLONG, null));

        // user avatar
        user.put(RENTALPICCITY, pref.getString(RENTALPICCITY, null)) ;

        user.put(RENTALTIMEDURATION, pref.getString(RENTALTIMEDURATION, null)) ;

        // return user
        return user;
    }

    public void setendTripstatus(Boolean aBoolean)
    {
     editor.putString(ENDTRIPSTATUS, String.valueOf(aBoolean)).commit();
    }

    public Boolean getendTripstatus()
    {
        return Boolean.parseBoolean(pref.getString(ENDTRIPSTATUS,null));
    }

    public void settripdata(String data)
    {
        editor.putString(ENDTRIPDATA, data).commit();
    }

    public String gettripdata()
    {
        return pref.getString(ENDTRIPDATA,null);
    }



    public void settotalbooking(int booking ,int  operatorbill, int  incentive){
        int booking1= pref.getInt(String.valueOf(BOOKINGCOUNT),0)+booking;
        float operatorbill1=pref.getFloat(String.valueOf(OPERATERBILL),0)+operatorbill;
        float incentive1=INCENTIVE+incentive;
        float total1=pref.getFloat(String.valueOf(INCENTIVE),0)+operatorbill;
        editor.putInt(String.valueOf(BOOKINGCOUNT),booking1);
        editor.putFloat(String.valueOf(OPERATERBILL),operatorbill1);
        editor.putFloat(String.valueOf(INCENTIVE),incentive1);
        editor.putFloat(String.valueOf(TOTAL),total1);
        // commit changes
        editor.commit();
    }


    public HashMap<String, String> gettotalbooking(){
        HashMap<String, String> user = new HashMap<>();

        user.put("booking", String.valueOf(pref.getInt(String.valueOf(BOOKINGCOUNT), 0)));

        // user email id
        user.put("operater", String.valueOf(pref.getFloat(String.valueOf(OPERATERBILL), 0)));

        // user phone number
        user.put("incentive", String.valueOf(pref.getFloat(String.valueOf(INCENTIVE), 0)));

        // user avatar
        user.put("total", String.valueOf(pref.getFloat(String.valueOf(TOTAL), 0))) ;

        // return user
        return user;
    }


    public void settrackid(String data)
    {
        editor.putString(TRACKID, data).commit();
    }

    public String gettrackid()
    {
        return pref.getString(TRACKID,null);
    }
}