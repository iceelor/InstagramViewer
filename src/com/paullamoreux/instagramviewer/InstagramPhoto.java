package com.paullamoreux.instagramviewer;

import java.util.ArrayList;

public class InstagramPhoto {
	public String username;
	public String caption;
	public String imageUrl;
	public int imageHeight;
	public int imageWidth;
	public int likesCount;
	public long createdTime;
	public String profilePictureUrl;
	public ArrayList<InstagramComment> comments;
}
