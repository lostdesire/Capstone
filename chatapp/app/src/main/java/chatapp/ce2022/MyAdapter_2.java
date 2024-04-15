package chatapp.ce2022;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyAdapter_2 extends RecyclerView.Adapter<MyAdapter_2.MyViewHolder> {
    private ArrayList<MsgData2> mDataset;


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public TextView textUid;
        public MyViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.tvChat);
            textUid = v.findViewById(R.id.user);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(mDataset.get(position).getPosition() == 0){
            return 1;
        } else{
            return 2;
        }
    }

    public MyAdapter_2(ArrayList<MsgData2> myDataset){mDataset = myDataset;}

    @Override
    public MyAdapter_2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view,parent,false);

        if(viewType == 1){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view_2,parent,false);
        }

        /*v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);*/








        MyViewHolder vh = new MyViewHolder(v);
        return vh;

    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){

        holder.textView.setText(mDataset.get(position).getContent());
        holder.textUid.setText(""+mDataset.get(position).getName());
        Log.d("VIEWHOLDER",mDataset.get(position).getName());
    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }
}
