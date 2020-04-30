package com.allright.android_filterrecyclerviewusingsearchviewintoolbar;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder> {

    public ArrayList<Model> os_version;


    public ModelAdapter(ArrayList<Model> arrayList) {
        os_version = arrayList;
    }

    @Override
    public ModelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_row, null );

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder( view );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ModelAdapter.ViewHolder holder, int position) {

        holder.nameView.setText( os_version.get( position ).getName() );

    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;

        public ViewHolder(View view) {
            super( view );

            nameView = (TextView) view.findViewById( R.id.txt_Name );
        }
    }

    public void setFilter(ArrayList<Model> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll( arrayList );
        notifyDataSetChanged();
    }
}