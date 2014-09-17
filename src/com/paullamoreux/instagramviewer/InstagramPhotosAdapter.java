package com.paullamoreux.instagramviewer;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
//			String usernameAndCaption = "<font color='#0000ff'>" + photo.username + "</font> " + photo.caption; //"This <i>is</i> a <b>test</b>";
//			tvCaption.setText(Html.fromHtml(usernameAndCaption));
			tvCaption.setText(getFormattedComment(photo.username, photo.caption));
		} else {
			tvCaption.setVisibility(View.GONE);
		}

		tvUsername.setText(photo.username);
		tvLikesCount.setText(Integer.toString(photo.likesCount) + " Likes");		
		imgPhoto.setImageResource(0);
		Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
		
		//imgProfilePic.getLayoutParams().height = 150;
		imgProfilePic.setImageResource(0);
		Picasso.with(getContext()).load(photo.profilePictureUrl).into(imgProfilePic);
		
		CharSequence relativeDateString = DateUtils.getRelativeTimeSpanString(photo.createdTime * 1000);
		tvRelativeTime.setText(relativeDateString);
		
		if (photo.comments.size() > 0) {
			LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.llComments);
			linearLayout.removeAllViews();  // clear past comments since recycling layout
			TextView comment1 = new TextView(getContext());
			comment1.setText(getFormattedComment(photo.comments.get(0).username, photo.comments.get(0).text));
			comment1.setMaxLines(3);
			comment1.setEllipsize(TextUtils.TruncateAt.END);
			linearLayout.addView(comment1);
		}
		
		return convertView;
	}
	
	
	public Spanned getFormattedComment(String username, String text) {
		String usernameAndCaption = "<font color='#0000ff'>" + username + "</font> " + text; //"This <i>is</i> a <b>test</b>";
		return Html.fromHtml(usernameAndCaption);
	}
	
	
}
