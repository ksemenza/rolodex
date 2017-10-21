package com.guinproductions.behaviorrolodex.recyclerview;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.guinproductions.behaviorrolodex.R;
import com.guinproductions.behaviorrolodex.model.Profile;

/**
 * Created by guinp on 10/20/2017.
 */

public class ProfileRV extends RecyclerView.ViewHolder{

   public TextView fnameView;
   public TextView lnameView;
  public   ImageView imageView;

    public ProfileRV(View itemView) {
        super(itemView);

        fnameView = (TextView) itemView.findViewById(R.id.view_fname);
        lnameView = (TextView) itemView.findViewById(R.id.view_lname);
        imageView = (ImageView) itemView.findViewById(R.id.view_image);

    }

    public void bindToProfile(Profile profile){
        fnameView.setText(profile.fname);
        lnameView.setText(profile.lname);
        imageView.setImageURI(Uri.parse(profile.imageview));
    }
}
