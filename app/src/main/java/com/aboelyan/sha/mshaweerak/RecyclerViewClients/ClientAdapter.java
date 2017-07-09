package com.aboelyan.sha.mshaweerak.RecyclerViewClients;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aboelyan.sha.mshaweerak.R;
import com.aboelyan.sha.mshaweerak.ShowShofierForClients.OnLineShofier;

import java.util.List;

/**
 * Created by Administrator on 08/07/2017.
 */

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientHolder> {

    List<ClientsModel> clientsModels;
    Context context;
    AlertDialog.Builder builder;

    public ClientAdapter(List<ClientsModel> clientsModels,Context context) {
        super();
        this.context=context;
        this.clientsModels=clientsModels;
    }



    /* public CommentAdapter(List<ListComments> mylist) {
     }
 */


    @Override
    public ClientAdapter.ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shofiers, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        ClientAdapter.ClientHolder holder = new ClientHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ClientAdapter.ClientHolder holder, final int position) {
        ClientsModel SH = clientsModels.get(position);

        holder.ShName.setText(SH.getNAME());
        holder.ShPhone.setText(SH.getPHONE());
        holder.model_car.setText(SH.getMODEL_CAR());



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {



                final String name=holder.ShName.getText().toString();
                final String phone=holder.ShPhone.getText().toString();
                final String model_car=holder.model_car.getText().toString();
                builder = new AlertDialog.Builder(ClientAdapter.this.context);


                builder.setTitle("عرض معلومات السائق");
                builder.setMessage("");
                builder.setPositiveButton("نعم ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ClientAdapter.this.context, OnLineShofier.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",name );
                        bundle.putString("phone",phone );
                        bundle.putString("model_car",model_car );

                        intent.putExtras(bundle);
                        context.startActivity(intent);


                    } });

                builder.setNegativeButton("لا ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }

                });
                builder.show();

                return true;
            }
        });

    }
    @Override
    public int getItemCount()
    {
        if(clientsModels!=null){
            return clientsModels.size();

        }
        return 0 ;

    }


   public  class ClientHolder extends RecyclerView.ViewHolder{




            TextView ShPhone;
            TextView ShName;
            TextView model_car;
        private ClientHolder(View itemView) {
            super(itemView);


            ShName = (TextView) itemView.findViewById(R.id.ShName);

            ShPhone = (TextView) itemView.findViewById(R.id.ShPhone);
            model_car = (TextView) itemView.findViewById(R.id.model_car);



        }
    }
}

