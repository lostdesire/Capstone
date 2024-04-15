package chatapp.ce2022;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private ArrayList<MsgData2> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public MyViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.tvChat);
        }
    }



    public MyAdapter(ArrayList<MsgData2> myDataset){
        mDataset = myDataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        MyAdapter.MyViewHolder vh = new MyAdapter.MyViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position){
        holder.textView.setText(mDataset.get(position).getContent());
    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }
}
