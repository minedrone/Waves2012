package com.waves.rss;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class WavesRssFeedsActivity extends ListActivity  {
	private final String TAG = "WavesRssFeedsActivity";
	List<RSSDescription> updates;
	String data;
	SQLiteRss info;
	ArrayAdapter<RSSDescription> ap;
	//ListView lv;
	//ViewGroup vg = (ViewGroup) findViewById (R.id.);
	//List list =(list)findViewById(R.id.)

	String s;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        ImageView myImage = (ImageView) findViewById(R.id.ivWavesBackground);
        myImage.setAlpha(75);

        //lv = (ListView) findViewById(R.id.list1);
        
        // new UpdateClass().execute();
        // Initializing instance variables
        
        // Binding data
    	updates=new ArrayList<RSSDescription>();
    	info = new SQLiteRss(this);
		try {
			info.open();
			updates = info.getData();
			info.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			Toast.makeText(this , "error in access "+e, Toast.LENGTH_SHORT).show();
		}
		
		ap = new RSSArrayAdapter(this, updates);
		setListAdapter(ap);

    	
        //next = (Button)findViewById(R.id.button1);
        //next.setOnClickListener(this);
        	
    }	

    public void resetList(){
    	Log.d(TAG,"Inside Resetlist");
    	info = new SQLiteRss(this);
		try {
			Log.d(TAG,"Inside Try Resetlist");
	    	
			info.open();
			updates = info.getData();
			info.close();
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
//			Toast.makeText(this , "error in access "+e, Toast.LENGTH_SHORT).show();
		}
//		Log.d(TAG, updates.get(0).toString());
//		Log.d(TAG, updates.get(1).toString());
		//Log.d(TAG, updates.get(0).toString());
		
		

    }
    
    public class UpdateClass extends AsyncTask<ArrayAdapter<List>, Integer, String>{
    	Dialog d = new Dialog(WavesRssFeedsActivity.this);
    	ProgressDialog pd = new ProgressDialog(WavesRssFeedsActivity.this);
    	int interneterror = 0 ;
    	protected void onPreExecute() {    		
    		      super.onPreExecute();    		
    		     // Dialog d = new Dialog(WavesRssFeedsActivity.this);
    		     pd.setMessage("Downloading....");
    		      pd.show();
    		      //d.setTitle("Downloading.....");
    		      //d.show();    		
    		   }

    	
    	@Override
    	protected String doInBackground(ArrayAdapter<List>... arg0) {    		
    		// TODO Auto-generated method stub        
    		GetRssFeedData rssData = new GetRssFeedData(WavesRssFeedsActivity.this);
    		interneterror =  rssData.getData();
    		//if(interneterror==10)
    		//{
    		
    		publishProgress(interneterror);
    		//}
    		
    		
    		Log.d(TAG,"end of do in background");
    		
    		return "All Done";
    	}
    	
    	protected void onProgressUpdate(Integer... values) {
    			super.onProgressUpdate(values);
    			pd.dismiss();
    			if(values[0]==10){
    				Log.d(TAG, "inside if interneterror == 10 ");
    				new AlertDialog.Builder(WavesRssFeedsActivity.this)
    			    .setTitle("Internet Access Not Found")
    			    .setMessage("Data could not be refreshed.")
    			    .show();

    				Log.d(TAG,"on progress update error case ended");
    	    		
    			}
    			else
    			{
    				
    				Log.d(TAG,"Reset is getting called from inside on progress update");
    				resetList();
    				Log.d(TAG,"Notify data change is getting called from on progress update");
    				ap = new RSSArrayAdapter(WavesRssFeedsActivity.this, updates);
    				setListAdapter(ap);
    				//ap.notifyDataSetChanged();
    				
    	    		
    			}
    			
    			Log.d(TAG,"on progress update ended");
        		
    		    
    		      
    		   }

    	protected void onPostExecute(String result) {
    		super.onPostExecute(result);
    		//setListAdapter(ap);
    		Log.d(TAG,"Post Execute before change to adapter");
  
    		
    		//resetList();
    		/*WavesRssFeedsActivity.this.runOnUiThread(new Runnable(){
    			public void run(){
    				Log.d(TAG, "in runonuithread");
    				ap.notifyDataSetChanged();
    				
    				
    			}
    		});*/
    		
    		pd.dismiss();
    		

    		
    		
    		Log.d(TAG, "Completed UI Thread");
    		//setListAdapter(ap);
    		//showDialog("Downloading done");
    		Log.d(TAG,"Post Execute after change to adapter");
    			   }
    		
    		
    }	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        
        case R.id.Refresh:
        	Log.d("CustomTag","RefreshSelected selected");
        	try {
    			new UpdateClass().execute();    				
    		} catch (Exception e) {
    			Toast.makeText(this , "error in intent "+e, Toast.LENGTH_LONG).show();
    			}finally{
    				   		    	
    			}            
            return true;
            
        case R.id.AboutUs:
        	Intent i =  new Intent(WavesRssFeedsActivity.this, ContactInformation.class);
        	startActivity(i);
        	
        	
        	return true;
        
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	/* void onClick(View v) {
	// TODO Auto-generated method stub
	switch(v.getId()){
	case R.id.button1:
		try {
			new UpdateClass().execute();
			/*Thread timer = new Thread(){
				public void run(){
					try{
//						WavesRssFeedsActivity waves = new WavesRssFeedsActivity();
//						waves.startUpClass();
						sleep(1000);
						Log.d(TAG,"Sleep Done");
						resetList();
						Log.d(TAG,"After Resetlist");
				    	
					} catch(InterruptedException e){
						e.printStackTrace();
					}finally {
						}
				}
				
	
			};
					timer.start();
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this , "error in intent "+e, Toast.LENGTH_LONG).show();
			}finally{
				//Log.d(TAG,"Calling Resetlist");
		    	
				//Log.d(TAG,"After Resetlist");
		    	
			}
		break;
		}	*/
   
	
	
}