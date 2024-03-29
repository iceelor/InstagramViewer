package com.paullamoreux.instagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
	
	private SwipeRefreshLayout swipeContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos);
		
		photos = new ArrayList<InstagramPhoto>();
		aPhotos = new InstagramPhotosAdapter(this, photos);
		lvPhotos = (ListView) findViewById(R.id.lvPhotos);
		lvPhotos.setAdapter(aPhotos);

		fetchPopularPhotos();
		
		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            	fetchPopularPhotos();
            } 
        });
	}


	private void fetchPopularPhotos() {
		// https://api.instagram.com/v1/media/popular?client_id=8e50b9f199d84972a23c9740f24c10d1
		String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(popularUrl, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				
				photos.clear();
				aPhotos.notifyDataSetChanged();
				
				JSONArray photosJSON = null;
				try {
					photosJSON = response.getJSONArray("data");
					for (int i = 0; i < photosJSON.length(); i++) {
						
						JSONObject photoJSON = photosJSON.getJSONObject(i);
						InstagramPhoto photo = new InstagramPhoto();
						photo.username = photoJSON.getJSONObject("user").getString("username");
						photo.profilePictureUrl = photoJSON.getJSONObject("user").getString("profile_picture");
						
						if (photoJSON.optJSONObject("caption") != null) {
							photo.caption = photoJSON.getJSONObject("caption").getString("text");
						} else {
							photo.caption = null;
						}
						
						photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
						photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
						photo.imageWidth = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
						photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
						photo.createdTime = photoJSON.getLong("created_time");
						
						JSONArray commentsJSON = photoJSON.getJSONObject("comments").getJSONArray("data");
						int numComments = commentsJSON.length();
						
						InstagramComment comment;
						JSONObject commentJSON;
						
						if (numComments > 0) {
							photo.comments = new ArrayList<InstagramComment>();

							if (numComments > 1) {
								comment = new InstagramComment();
								commentJSON = commentsJSON.getJSONObject(numComments - 2);
								comment.username = commentJSON.getJSONObject("from").getString("username");
								comment.text = commentJSON.getString("text");
								photo.comments.add(comment);
							}

							comment = new InstagramComment();
							commentJSON = commentsJSON.getJSONObject(numComments - 1);
							comment.username = commentJSON.getJSONObject("from").getString("username");
							comment.text = commentJSON.getString("text");
							photo.comments.add(comment);
						}
						
						photos.add(photo);
					}
					aPhotos.notifyDataSetChanged();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				swipeContainer.setRefreshing(false);

				
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				swipeContainer.setRefreshing(false);
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
