package si.uni_lj.fe.tnuv.chipstock_recyclerviewtest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder > {

    Context context;
    ArrayList<ComponentClass> compList = new ArrayList<ComponentClass>();
    int numComponents = 0;

    public RecyclerAdapter(Context ct, ArrayList<ComponentClass> list){
        context = ct;
        compList = list;
        numComponents = list.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Log.d("ADBG", String.valueOf(position));

        //get data fields from component in array
        ComponentClass comp = compList.get(position); //get one component object
        int id = comp.id;
        String name = comp.name;
        String description = comp.description;
        String partnum = comp.partNumber;
        String ordernum = comp.orderNumber;
        String stockloc = comp.stockLocation;
        String notes = comp.notes;
        int instock = comp.inStock;

        //set textviews
        holder.nametxt.setText(name);
        holder.desctxt.setText(description);
        holder.partnum.setText(partnum);
        holder.stocknumtxt.setText(String.valueOf(instock));

        //holder.myImg.setImageResource(R.drawable.img);

        //click listener when scrolview row is clicked
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.displayDeletedFlag == 1){
                    MainActivity.restoreItem(holder.getAdapterPosition());
                }
                else{
                    Intent intent = new Intent(context, componentEditActivity.class);
                    intent.putExtra("idx", holder.getAdapterPosition());

                    if(MainActivity.displayDeletedFlag == 0){
                        //add flag if deleted list is displayed
                        intent.putExtra("deletedList", 1);
                    }
                    else{
                        intent.putExtra("deletedList", 0);
                    }

                    context.startActivity(intent);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return compList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nametxt, desctxt, partnum, stocknumtxt;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            //get element ids
            super(itemView);
            nametxt = itemView.findViewById(R.id.name_txt);
            desctxt = itemView.findViewById(R.id.desc_txt);
            partnum = itemView.findViewById(R.id.mnfnum_txt);
            stocknumtxt = itemView.findViewById(R.id.stocknum_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
