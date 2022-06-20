package si.uni_lj.fe.tnuv.chipstock_recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Context ctx = this;
    //init component list arrays
    static ArrayList<ComponentClass> cList = new ArrayList<ComponentClass>(); //main component data storage arrayList
    static ArrayList<ComponentClass> cListDeleted = new ArrayList<ComponentClass>(); //deleted component data storage arrayList
    static ArrayList<ComponentClass> cListSearch = new ArrayList<ComponentClass>();
    static int displayDeletedFlag = 0; //has value of 1 when deleted items are shown
    static RecyclerAdapter myAdapter;
    //GSON instance
    Gson gson = new Gson();





    //saves json to shared preferences (list save when app is closed)
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
        //refresh list (data may have changed)
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainToolbar_id);
        setSupportActionBar(myToolbar);

        //here, load up cList and cListDeleted from json (from shared prefs)
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


        //on checked change listener for trash button
//        ToggleButton trashButton = findViewById(R.id.btnTrash_id);
//        trashButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked){
//                    fab.hide();
//                    displayDeletedFlag = 1;
//                    //TextView listname = findViewById(R.id.listText_id);
//                    //listname.setText("Deleted items. Tap item to restore.");
//                    myAdapter = new RecyclerAdapter(ctx, cListDeleted);
//                    recyclerView.setAdapter(myAdapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
//
//                }
//                else{
//                    fab.show();
//                    displayDeletedFlag = 0;
//                    //TextView listname = findViewById(R.id.listText_id);
//                    //listname.setText("Main stock list");
//                    myAdapter = new RecyclerAdapter(ctx, cList);
//                    recyclerView.setAdapter(myAdapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
//                }
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cListSearch.clear();
                for (int n = 0; n < cList.size()-1; n++) {
                    ComponentClass cmp = cList.get(n);
                    if(Pattern.compile(Pattern.quote(newText), Pattern.CASE_INSENSITIVE).matcher(cmp.name).find()){
                        cListSearch.add(cmp);
                    }
                    else if(Pattern.compile(Pattern.quote(newText), Pattern.CASE_INSENSITIVE).matcher(cmp.description).find()){
                        cListSearch.add(cmp);
                    }
                    else if(Pattern.compile(Pattern.quote(newText), Pattern.CASE_INSENSITIVE).matcher(cmp.partNumber).find()){
                        cListSearch.add(cmp);
                    }
                    else if(Pattern.compile(Pattern.quote(newText), Pattern.CASE_INSENSITIVE).matcher(cmp.orderNumber).find()){
                        cListSearch.add(cmp);
                    }
                    else if(Pattern.compile(Pattern.quote(newText), Pattern.CASE_INSENSITIVE).matcher(cmp.stockLocation).find()){
                        cListSearch.add(cmp);
                    }
                    else if(Pattern.compile(Pattern.quote(newText), Pattern.CASE_INSENSITIVE).matcher(cmp.notes).find()){
                        cListSearch.add(cmp);
                    }
                }
                myAdapter = new RecyclerAdapter(ctx, cListSearch);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
                return false;
            }
        });
        return true;
    }

    //Handling Action Bar button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            //Back button
//            case android.R.id.home:
//                //If this activity started from other activity
//                finish();
//                return true;

            case R.id.action_settings:
                intent = new Intent(ctx, SettingActivity.class);
                ctx.startActivity(intent);
                return true;

            case R.id.action_deleted:
                displayDeletedFlag = 1;
                intent = new Intent(ctx, DeletedActivity.class);
                ctx.startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }







}

