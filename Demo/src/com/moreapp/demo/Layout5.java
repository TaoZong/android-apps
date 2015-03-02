package com.moreapp.demo;

import java.io.File;
import java.io.InputStream;
import java.net.URL;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.moreapp.demo.R;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.ViewGroup;

//Profile -> Update Avatar

public class Layout5 extends Activity {
	private ImageView image;
	private static String PhotoName = "";
	private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0; 
    private ProgressBar pb;
    private TextView text1;
    private String url;
    private String resString;
    private Bitmap d;
    String pid, relationship;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);		
		setContentView(R.layout.fragment_layout5);
		Bundle b = this.getIntent().getExtras();
        pid = b.getString("pid");
        relationship = b.getString("relationship");
		Button select = (Button)findViewById(R.id.button1);
		Button upload = (Button)findViewById(R.id.button2);
		Button cancel = (Button)findViewById(R.id.button3);
		pb = (ProgressBar)findViewById(R.id.progressBar1);
		text1 = (TextView)findViewById(R.id.textView21);
		d = null;
		image = (ImageView)findViewById(R.id.imageView1);
		
		url = "";
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpGet httpget = new HttpGet(SignUpAndSignIn.urlHead + "/profile?pid=" + pid);
				try {
					HttpResponse response = SignUpAndSignIn.client.execute(httpget);
					HttpEntity resEntity = response.getEntity();
			        resString = EntityUtils.toString(resEntity);
			        Log.d("VIEW AVATAR STATUS:", resString);
				} catch (Exception e) {
					
				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			
		}
		JSONObject jobject;
		try {
			jobject = new JSONObject(resString);
			url = jobject.getJSONObject("basic_info").getString("avatar220");
			Thread t2 = new Thread( new Runnable() {
				public void run() {
					try {
				        InputStream is = (InputStream) new URL(url).getContent();
				        d = BitmapFactory.decodeStream(is);
				        is.close();
				    } catch (Exception e) {
				    	Log.d("EXCEPTION:",e.toString());
				    }
				}
			});
			
			t2.start();
			try {
				t2.join();
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
			
			if(d != null) image.setImageBitmap(d);
		}
		catch(JSONException e) {
			
		}
		
		
		select.setOnClickListener(new View.OnClickListener()
        {
  
            @Override
            public void onClick(View v) {
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                startActivityForResult(getAlbum, IMAGE_CODE);
        
            }
              
        });
		
		upload.setOnClickListener(new View.OnClickListener()
        {
  
            @Override
            public void onClick(View v) {
            	new ProgressTask().execute();            	
            }
        });
		
		cancel.setOnClickListener(new View.OnClickListener()
        { 
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent();
    			intent.setClass(Layout5.this,
    					ProfileDetails.class);
    			Bundle bundle = new Bundle();
    			bundle.putString("pid", pid);
    			bundle.putString("relationship", relationship);
    			intent.putExtras(bundle);
    			startActivity(intent);
    			Layout5.this.finish();
            }
        });
	}
	
	private class ProgressTask extends AsyncTask <Void,Void,Void>{
	    @Override
	    protected void onPreExecute(){
	    	text1.setText("Uploading Avatar......");
	        pb.setVisibility(View.VISIBLE);
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	    	Intent intent = new Intent();
			intent.setClass(Layout5.this,
					ProfileDetails.class);
			Bundle bundle = new Bundle();
			bundle.putString("pid", pid);
			bundle.putString("relationship", relationship);
			intent.putExtras(bundle);
			startActivity(intent);
			Layout5.this.finish();
	    }

		@Override
		protected Void doInBackground(Void... params) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					File file = new File(PhotoName);
					FileBody fileBody = new FileBody(file);
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead + "/profile/basic-info");
					MultipartEntityBuilder entity = MultipartEntityBuilder.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					try {
						entity.addPart("avatar", fileBody);
						entity.addTextBody("pid", pid, ContentType.TEXT_PLAIN);
						post.setEntity(entity.build());
						HttpResponse response = SignUpAndSignIn.client.execute(post);
						HttpEntity resEntity = response.getEntity();
				        String resString = EntityUtils.toString(resEntity);
				        Log.d("UPDATE AVATAR STATUS:", resString);
					} catch (Exception e) {
//						e.printStackTrace();
					}
				}
            });
            t.start();
            try {
				t.join();
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
			return null;
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
        if (resultCode != -1) {        
            return;
        }
        if (requestCode == IMAGE_CODE) {
           try {  
               Uri selectedImage = data.getData();  
               String[] filePathColumn = { MediaStore.Images.Media.DATA };  
   
               Cursor cursor = getContentResolver().query(selectedImage,  
                       filePathColumn, null, null, null);  
               cursor.moveToFirst();  
   
               int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
               String picturePath = cursor.getString(columnIndex);  
               PhotoName = picturePath;
               cursor.close();  
               image.setImageBitmap(BitmapFactory.decodeFile(picturePath));  
           } catch (Exception e) {  
//               e.printStackTrace();  
           }
        }
	}

}
