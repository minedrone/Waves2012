package com.waves.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class GetRssFeedData{
	private final Context WavesContext;
	private final String TAG = "GetRSSFeedData";
	String s;
	List headlines;
	List links;
	public GetRssFeedData(Context context){
		 headlines = new ArrayList();
		 links = new ArrayList();
		 WavesContext=context;
	}
	public InputStream getInputStream(URL url) {
		   try {
		       return url.openConnection().getInputStream();
	   } catch (IOException e) {
		       return null;
		     }
	}
	public int getData(){
		try {
    	    URL url = new URL("http://www.bits-waves.org/2012/ticker");
    	    Log.d(TAG, "inside getdata()");
    	    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    	    factory.setNamespaceAware(false);
    	    XmlPullParser xpp = factory.newPullParser();
    	    
    	    Log.d(TAG, "xml Variables created");
            // We will get the XML from an input stream
    	    
    	    try {
				xpp.setInput(getInputStream(url), "UTF_8");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				Log.d(TAG, "inside catch of getting url");
	            	
				return 10;
			}
    	 
    	    Log.d(TAG, "url accessed");
    	    boolean insideItem = false;
    	    boolean uidsuccess = false;
    	 
    	        // Returns the type of current event: START_TAG, END_TAG, etc..
    	    int eventType = xpp.getEventType();
    	    //boolean didItWork=true;
    	    String guid;
    	    int uid=-1,uid_prefs,max=-1;
    	    
    	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WavesContext);
    	    uid_prefs=prefs.getInt("uid", -1);
    	    
	        if(uid_prefs==(-1)){
	        	Log.d("prefs", "pref var doesn't exist");
    	     while (eventType != XmlPullParser.END_DOCUMENT) {
    	        if (eventType == XmlPullParser.START_TAG) {
    	 
    	            if (xpp.getName().equalsIgnoreCase("item")) {
    	                insideItem = true;
    	            } 
    	            else if (xpp.getName().contains("guid")){
    	            	if(insideItem)
    	            	{
    	            		guid=xpp.nextText();
    	            		try {
								uid=Integer.parseInt(guid);
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								Log.d(TAG, "could not parse");
							}
    	            		if(uid>max){
    	            			max=uid;
    	            		}
    	            		
    	            	}
    	            }
    	            else if (xpp.getName().equalsIgnoreCase("description")) {
    	                if (insideItem)
    	                {
    	                	s=xpp.nextText();
    	                	s=s.trim();
    	                	Log.e("GetRssFeedData", s);
    	                	
    	               	
    	                try {
							SQLiteRss entry = new SQLiteRss(WavesContext);
							entry.open();
							entry.createEntry(s,uid);
							entry.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//didItWork=false;
						}
    	                	
                        headlines.add(s); //extract the headline
                        
    	                }
    	            } else if (xpp.getName().equalsIgnoreCase("link")) {
    	                if (insideItem)
    	                    links.add(xpp.nextText()); //extract the link of article
               }
    	        }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
    	            insideItem=false;
    	        }
    	     
    	 
    	        eventType = xpp.next(); //move to next element
    	    }
    	     
    	     SharedPreferences.Editor editor = prefs.edit();
    	     editor.putInt("uid", max);
    	     editor.commit();
    	     
	       }
	      else{
	    	  Log.d("prefs", "pref var exists");
	    	  int flag=0;
	    	  while (eventType != XmlPullParser.END_DOCUMENT) {
	    	        if (eventType == XmlPullParser.START_TAG) {
	    	 
	    	            if (xpp.getName().equalsIgnoreCase("item")) {
	    	                insideItem = true;
	    	            } 
	    	            else if (xpp.getName().contains("guid")){
	    	            	if(insideItem)
	    	            	{
	    	            		guid=xpp.nextText();
	    	            		try {
									uid=Integer.parseInt(guid);
								} catch (NumberFormatException e) {
									// TODO Auto-generated catch block
									Log.d(TAG, "could not parse");
								}
	    	            		
	    	            		if(uid>uid_prefs){
	    	            			uidsuccess=true;
	    	            			if(uid>max){
	    	            				max=uid;
	    	            				flag=1;
	    	            			}
	    	            		}
	    	            		else{
	    	            			uidsuccess=false;
	    	            		}
	    	            		
	    	            		
	    	            	}
	    	            }
	    	            else if (xpp.getName().equalsIgnoreCase("description")) {
	    	                if ((insideItem)&&(uidsuccess))
	    	                {
	    	                	s=xpp.nextText();
	    	                	s=s.trim();
	    	                	Log.e("GetRssFeedData", s);
	    	                	
	    	               	
	    	                try {
								SQLiteRss entry = new SQLiteRss(WavesContext);
								entry.open();
								entry.createEntry(s,uid);
								entry.close();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								//didItWork=false;
							}
	    	                	
	                        headlines.add(s); //extract the headline
	                        
	    	                }
	    	            } else if (xpp.getName().equalsIgnoreCase("link")) {
	    	                if (insideItem)
	    	                    links.add(xpp.nextText()); //extract the link of article
	               }
	    	        }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
	    	            insideItem=false;
	    	        }
	    	     
	    	 
	    	        eventType = xpp.next(); //move to next element
	    	    } 
	    	  
	    	  if(flag==1){
	    	     SharedPreferences.Editor editor = prefs.edit();
	    	     editor.putInt("uid", max);
	    	     editor.commit();
	    	  }
	      }
			
    	 
    	 
    	} catch (MalformedURLException e) {
    	    e.printStackTrace();
    	} catch (XmlPullParserException e) {
    	    e.printStackTrace();
    	} catch (IOException e) {
   	    e.printStackTrace();
    	}
		return 1;
	}
	

}
