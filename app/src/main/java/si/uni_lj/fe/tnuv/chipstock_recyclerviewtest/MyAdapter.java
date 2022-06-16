package si.uni_lj.fe.tnuv.chipstock_recyclerviewtest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder > {

    String data1[], data2[];
    Context context;

    public MyAdapter(Context ct,String nm[], String dsc[]){
        context = ct;
        data1 = nm;
        data2 = dsc;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //inflates with row elements
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mytext1.setText(data1[position]);
        holder.mytext2.setText(data2[position]);
        //holder.myImg.setImageResource(R.drawable.img);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, componentEditActivity.class);
                intent.putExtra("idx", holder.getAdapterPosition());
                intent.putExtra("new", false);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mytext1, mytext2;
        ImageView myImg;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mytext1 = itemView.findViewById(R.id.name_txt);
            mytext2 = itemView.findViewById(R.id.desc_txt);
            //myImg = itemView.findViewById(R.id.my_img_view);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
