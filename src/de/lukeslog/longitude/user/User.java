package de.lukeslog.longitude.user;

import com.google.android.gms.maps.model.LatLng;

import android.graphics.Bitmap;

public class User {

	String uid="";
	String name="";
	Bitmap bmp;
	LatLng position;
	
	public User(String uid)
	{
		this.uid=uid;
		//TODO: If no bmp is given... set a default one
	}
	
	public User(String uid, Bitmap bmp)
	{
		this.uid=uid;
		this.bmp=bmp;
	}
	
	public String getUID()
	{
		return uid;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Bitmap getAvatar()
	{
		return bmp;
	}
	
	public LatLng getPosition()
	{
		return position;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}
	
	public void setBmp(Bitmap bmp)
	{
		this.bmp=bmp;
	}
	
	public void setPosition(double lat, double lon)
	{
		this.position= new LatLng(lat, lon);
	}
	
	public void setPosition(LatLng p)
	{
		this.position= p;
	}
	
	public void renewGravatar()
	{
		
	}
}
