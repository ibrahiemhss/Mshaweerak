package com.aboelyan.sha.mshaweerak.RecyclerVeiwShofiers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aboelyan.sha.mshaweerak.R;

import java.util.List;

/**
 * Created by Administrator on 28/06/2017.
 */

public class ShAdapter extends RecyclerView.Adapter<ShAdapter.MyHolder> {

    List<BookModels> bookModelses;
    Context context;

    public ShAdapter(List<BookModels> bookModelses,Context context) {
        super();
        this.context=context;
        this.bookModelses=bookModelses;
    }



    /* public CommentAdapter(List<ListComments> mylist) {
     }
 */


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shofiers, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        BookModels SH = bookModelses.get(position);

        holder.whereFace.setText(SH.getFace());
        holder.traveTimeBB.setText(SH.getTraveTime());
        holder.user_id.setText(SH.getUser_id());
        holder.car_id.setText(SH.getCar_id());
    }
    @Override
    public int getItemCount()
    {
        if(bookModelses!=null){
            return bookModelses.size();

        }
        return 0 ;

    }



    public class MyHolder extends RecyclerView.ViewHolder{

        // Typeface customTypeOne = Typeface.createFromAsset(itemView.getContext().getAssets(), "DroidNaskh-Regular.ttf");




        TextView whereFace;
        TextView traveTimeBB;
        TextView user_id;
        TextView car_id;

        MyHolder(View itemView) {
            super(itemView);

            whereFace = (TextView) itemView.findViewById(R.id.whereFace);
            traveTimeBB = (TextView) itemView.findViewById(R.id.traveTimeBB);
            user_id = (TextView) itemView.findViewById(R.id.user_id);
            car_id = (TextView) itemView.findViewById(R.id.car_id);
        }

    }}