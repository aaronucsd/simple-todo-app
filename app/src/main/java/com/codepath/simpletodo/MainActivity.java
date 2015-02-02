package com.codepath.simpletodo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity  implements EditItemDialog.EditNameDialogListener {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;
    int pos_curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();//load items during onCreate and assign to 'items'
        //items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        //items.add("First Item");
        //items.add("Second Item");
        setupListViewListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View v){
        //handler to the input field
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        //get the text from the input
        String itemText = etNewItem.getText().toString();
        //assign the text to the list with the list's adaptor
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        //save items when a new list item is added
        writeItems();
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View item, int pos, long id) {
                items.remove(pos);//remove that item from pos
                itemsAdapter.notifyDataSetChanged();//refreshes the adaptor for the list
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int pos, long id) {
                String value = itemsAdapter.getItem(pos);
                pos_curr = pos;
                //launchComposeView(value);
                showEditDialog(value);
            }
        });

    }

    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        catch (IOException e){
            items = new ArrayList<String>();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /*
    * http://guides.codepath.com/android/Using-DialogFragment
    * http://developer.android.com/reference/android/app/DialogFragment.html#BasicDialog
    * */
    private void showEditDialog(String value) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialog editNameDialog = EditItemDialog.newInstance("Update and item");
        editNameDialog.setTextValue(value);
        editNameDialog.show(fm, "fragment_edit_item");
    }

    //Launches Edit view page
    public void launchComposeView(String value) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("item_value", value);
        //startActivity(i); // brings up the second activity
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        //Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();

        // Toast the name to display temporarily on screen
        Toast.makeText(this, inputText, Toast.LENGTH_SHORT).show();
        items.set(this.pos_curr, inputText);//update with saved value
        itemsAdapter.notifyDataSetChanged();//refreshes the adaptor for the list
        writeItems();//saveItems?
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String item_value = data.getExtras().getString("item_value");
            // Toast the name to display temporarily on screen
            Toast.makeText(this, item_value, Toast.LENGTH_SHORT).show();
            items.set(this.pos_curr, item_value);//update with saved value
            itemsAdapter.notifyDataSetChanged();//refreshes the adaptor for the list
            writeItems();//saveItems?
        }
    }
}
