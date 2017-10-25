package com.guinproductions.behaviorrolodex.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guinproductions.behaviorrolodex.R;
import com.guinproductions.behaviorrolodex.activity.BehaviorActivity;
import com.guinproductions.behaviorrolodex.model.Behavior;

import java.util.List;

/**
 * Created by guinp on 10/24/2017.
 */

public class BehaviorList extends RecyclerView.Adapter<BehaviorList.ViewHolder>  {

private List<Behavior> behaviors;
public static final String behaviorID = "behaviorid";
public static final String descBehavior = "description";


public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    Behavior behavior;
    public TextView behaviorView;
    private final Context context;


    public ViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        behaviorView = (TextView) itemView.findViewById(R.id.behavior_view);


        itemView.setOnClickListener(this);

    }
    public void bind(Behavior behavior) {
        this.behavior = behavior;

        behaviorView.setText(behavior.getDescription());
    }



    @Override
    public void onClick(View v) {
        final Intent i;
        switch (getAdapterPosition()){

            default: i = new Intent(context, BehaviorActivity.class);

        }

        i.putExtra(behaviorID, behavior.getBehaviorid());
        i.putExtra(descBehavior, behavior.getDescription());

        context.startActivity(i);
    }

}
    public BehaviorList(List<Behavior> behaviors) {
        this.behaviors = behaviors;
    }


    @Override
    public BehaviorList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_behavior, parent, false);


        return new BehaviorList.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BehaviorList.ViewHolder holder, int position) {


        holder.bind(behaviors.get(position));



    }

    @Override
    public int getItemCount() {
        return behaviors.size();
    }

    public void updateList(List<Behavior> behaviors){

        if (behaviors.size() != this.behaviors.size() || !this.behaviors.containsAll(behaviors)){
            this.behaviors = behaviors;
            notifyDataSetChanged();
        }

    }

    public void removeItem(int position){
        behaviors.remove(position);
        notifyItemRemoved(position);
    }

    public Behavior getItem(int position){return behaviors.get(position);}





}
