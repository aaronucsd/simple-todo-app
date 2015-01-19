package com.codepath.simpletodo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


/**
 * Created by long on 1/13/15.
 */
public class ToDoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
