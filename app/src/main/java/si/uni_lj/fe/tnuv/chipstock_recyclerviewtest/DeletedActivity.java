package si.uni_lj.fe.tnuv.chipstock_recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

public class DeletedActivity extends AppCompatActivity {

    Context ctx = this;
    static RecyclerAdapter myAdapter;
    RecyclerView recyclerView;

    //restores item from trash to main
    public static void restoreItem(int idx){
        //get component to be restored
        ComponentClass delComp = MainActivity.cListDeleted.get(idx);
        //write it to normal list
        MainActivity.cList.add(delComp);
        //remove it from main list
        MainActivity.cListDeleted.remove(idx);
        //myAdapter.notifyItemRemoved(idx);
        myAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted);

        Toolbar myToolbar = findViewById(R.id.deletedToolbar_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set recycler view adapter and pass list
        recyclerView = findViewById(R.id.deletedRecyclerID);
        myAdapter = new RecyclerAdapter(this, MainActivity.cListDeleted);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.displayDeletedFlag = 0;
        MainActivity.saveToJson(ctx);
    }
}