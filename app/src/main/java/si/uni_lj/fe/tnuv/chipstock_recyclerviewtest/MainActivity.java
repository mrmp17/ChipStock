package si.uni_lj.fe.tnuv.chipstock_recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

//koraki za usposobit recycler view


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Context ctx = this;
    static ArrayList<ComponentClass> cList = new ArrayList<ComponentClass>(); //main component data storage arrayList
    static ArrayList<ComponentClass> cListDeleted = new ArrayList<ComponentClass>(); //deleted component data storage arrayList
    static int displayDeletedFlag = 0; //has value of 1 when deleted items are shown
    static RecyclerAdapter myAdapter;
    Gson gson = new Gson();

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

    public static void saveToJson(Context ctx){
        //write json and save to sharedperfs
        Gson gson = new Gson();
        String cListJson = gson.toJson(cList);
        String cListDeletedJson = gson.toJson(cListDeleted);
        SharedPreferences sp = ctx.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("cList", cListJson);
        editor.putString("cListDeleted", cListDeletedJson);
        editor.apply();
    }


    @Override
    protected void onResume() {
        super.onResume();
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //here, load up cList and cListDeleted from json
        SharedPreferences sp = ctx.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        if(sp.contains("cList") && sp.contains("cListDeleted")){
            String cListJson = sp.getString("cList", "");
            String cListDeletedJson = sp.getString("cListDeleted", "");
            cList = (ArrayList<ComponentClass>) gson.fromJson(cListJson,
                    new TypeToken<ArrayList<ComponentClass>>() {
                    }.getType());
            cListDeleted = (ArrayList<ComponentClass>) gson.fromJson(cListDeletedJson,
                    new TypeToken<ArrayList<ComponentClass>>() {
                    }.getType());
        }



        //set recycler view adapter and pass
        recyclerView = findViewById(R.id.recyclerID);
        myAdapter = new RecyclerAdapter(this, cList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Intent intent = new Intent(this, componentEditActivity.class);
        //startActivity(intent);

        //on click listener for add component FAB
        FloatingActionButton fab = findViewById(R.id.addFab_id);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentClass newcomp = new ComponentClass();
                cList.add(newcomp);
                Intent intent = new Intent(ctx, componentEditActivity.class);
                intent.putExtra("idx", cList.size()-1);
                intent.putExtra("new", true);
                ctx.startActivity(intent);
            }
        });

        //on click listener for settings button
        ImageButton settingsButton = findViewById(R.id.btnSettings_id);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, SettingActivity.class);
                ctx.startActivity(intent);
            }
        });

        //on click listener for backup button
        ImageButton backupButton = findViewById(R.id.btnBackup_id);
        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, BackupActivity.class);
                ctx.startActivity(intent);
            }
        });

        //on checked change listener for trash button
        ToggleButton trashButton = findViewById(R.id.btnTrash_id);
        trashButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    fab.hide();
                    displayDeletedFlag = 1;
                    TextView listname = findViewById(R.id.listText_id);
                    listname.setText("Deleted items. Tap item to restore.");
                    myAdapter = new RecyclerAdapter(ctx, cListDeleted);
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ctx));

                }
                else{
                    fab.show();
                    displayDeletedFlag = 0;
                    TextView listname = findViewById(R.id.listText_id);
                    listname.setText("Main stock list");
                    myAdapter = new RecyclerAdapter(ctx, cList);
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
                }
            }
        });
    }






}
