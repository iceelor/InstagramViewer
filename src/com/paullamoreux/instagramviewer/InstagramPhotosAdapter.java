package com.paullamoreux.instagramviewer;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
	public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		InstagramPhoto photo = getItem(position);
		
		if (convertView == null) {
			// create a new view from scratch, otherwise use the view passed in
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
		}
		
		TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
		TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
		CircularImageView imgProfilePic = (CircularImageView) convertView.findViewById(R.id.imgProfilePic);

		if (photo.caption != null) {
			tvCaption.setVisibility(View.VISIBLE);
			String usernameAndCaption = "<font color='#0000ff'>" + photo.username + "</font> " + photo.caption; //"This <i>is</i> a <b>test</b>";
			tvCaption.setText(Html.fromHtml(usernameAndCaption));
		} else {
			tvCaption.setVisibility(View.GONE);
		}

		tvUsername.setText(photo.username);
		tvLikesCount.setText(Integer.toString(photo.likesCount));		
		imgPhoto.setImageResource(0);
		Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
		
		//imgProfilePic.getLayoutParams().height = 150;
		imgProfilePic.setImageResource(0);
		Picasso.with(getContext()).load(photo.profilePictureUrl).into(imgProfilePic);
		
		CharSequence relativeDateString = DateUtils.getRelativeTimeSpanString(photo.createdTime * 1000);
		tvRelativeTime.setText(relativeDateString);
		
		return convertView;
	}
	
	
}
