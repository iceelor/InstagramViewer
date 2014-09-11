package com.paullamoreux.instagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PhotosActivity extends Activity {
	public static final String CLIENT_ID = "8e50b9f199d84972a23c9740f24c10d1";
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotosAdapter aPhotos;
	private ListView lvPhotos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos);
		fetchPopularPhotos();

	}

	private void fetchPopularPhotos() {
		photos = new ArrayList<InstagramPhoto>();
		
		aPhotos = new InstagramPhotosAdapter(this, photos);
		lvPhotos = (ListView) findViewById(R.id.lvPhotos);
		lvPhotos.setAdapter(aPhotos);
		
		// https://api.instagram.com/v1/media/popular?client_id=8e50b9f199d84972a23c9740f24c10d1
		String popularUrl = "https://api.instagram.com/v1/media/popular?client_id="
				+ CLIENT_ID;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(popularUrl, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {

				JSONArray photosJSON = null;
				try {
					photosJSON = response.getJSONArray("data");
					for (int i = 0; i < photosJSON.length(); i++) {
						
						JSONObject photoJSON = photosJSON.getJSONObject(i);
						InstagramPhoto photo = new InstagramPhoto();
						photo.username = photoJSON.getJSONObject("user")
								.getString("username");
						
						if (photoJSON.getJSONObject("caption") != null) {
							photo.caption = photoJSON.getJSONObject("caption").getString("text");
						}
						
						photo.imageUrl = photoJSON.getJSONObject("images")
								.getJSONObject("standard_resolution")
								.getString("url");
						photo.imageHeight = photoJSON.getJSONObject("images")
								.getJSONObject("standard_resolution")
								.getInt("height");
						photo.likesCount = photoJSON.getJSONObject("likes")
								.getInt("count");
						
						photos.add(photo);
					}
					aPhotos.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Log.i("INFO", responseString);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
