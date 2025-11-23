package com.skupina1.location.userLocation;

//class that contains the
//id --> long
//user_id -->string (mongodb objectId)
//lat , lng --> double
//used in the GET and PUT request
//use it to fetch the data in a DTO object
public class  UserLocationDTO extends LocationDTO{
    private long id ;
    private String user_id ;
    public UserLocationDTO(){
    }
    public UserLocationDTO(long id, String user_id ,  double lng, double lat){
        super(lng,lat);
        this.id = id;
        this.user_id = user_id;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUser_id() {
        return this.user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}