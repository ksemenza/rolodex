package com.guinproductions.behaviorrolodex.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.guinproductions.behaviorrolodex.R;
import com.guinproductions.behaviorrolodex.activity.ProfileView;
import com.guinproductions.behaviorrolodex.model.Profile;

import java.util.List;

/**
 * Created by guinp on 10/20/2017.
 */

public class ProfileRV extends RecyclerView.Adapter<ProfileRV.ViewHolder>  {

    private List<Profile> profiles;
    public static final String profileID = "profileid";
    public static final String fNameRef = "fname";
    public static final String lNameRef = "lname";
    public static final String notedRef = "noted";
    public static final String imageRef = "imageview";

    Profile profile;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

      public Profile profile;


        public TextView fnameView;
        public TextView lnameView;
        public ImageView imageView;
        private final Context context;


        public ViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            fnameView = (TextView) itemView.findViewById(R.id.view_fname);
            lnameView = (TextView) itemView.findViewById(R.id.view_lname);
            imageView = (ImageView) itemView.findViewById(R.id.view_image);


            itemView.setOnClickListener(this);

        }

        public void bind(Profile profile) {
            this.profile = profile;


            fnameView.setText(profile.fname);
            lnameView.setText(profile.lname);
//            imageView.setImageURI(Uri.parse(profile.imageview));
        }



        @Override
        public void onClick(View v) {

            Context mContext = v.getContext();




            final Intent i;
            switch (getAdapterPosition()){

            default: i = new Intent(context, ProfileView.class);

            }

            i.putExtra(profileID, profile.getProfileid());
            i.putExtra(fNameRef, profile.getFname());
            i.putExtra(lNameRef, profile.getLname());
            i.putExtra(notedRef, profile.getNoted());
            i.putExtra(imageRef, profile.getImageview());

            context.startActivity(i);


        }

        }



    public ProfileRV(List<Profile> profiles) {
        this.profiles = profiles;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.bind(profiles.get(position));



    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void updateList(List<Profile> profiles){

        if (profiles.size() != this.profiles.size() || !this.profiles.containsAll(profiles)){
            this.profiles = profiles;
            notifyDataSetChanged();
        }

    }

    public void removeItem(int position){
        profiles.remove(position);
        notifyItemRemoved(position);
    }

    public Profile getItem(int position){return profiles.get(position);}





}


