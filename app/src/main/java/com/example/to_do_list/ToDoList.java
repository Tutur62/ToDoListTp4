package com.example.to_do_list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class ToDoList extends AppCompatActivity {
    static ArrayAdapter<String> aa;
    public static final int INSERT_ID = Menu.FIRST;
    private int mNoteNumber = 0;
    private NotesDbAdapter mDbHelper;
    static private final ArrayList<String> todoItems = new ArrayList<String>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        final EditText champs = (EditText) findViewById(R.id.champ_saisie);
        final ListView liste1 = (ListView) findViewById(R.id.liste);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button button = (Button) findViewById(R.id.button);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        fillData();
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNote();
                fillData();
            }
        });
        liste1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int  position, long id) {
                mDbHelper.deleteNote(id);
                fillData();
            }
        });
    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {mDbHelper.deleteAll();
                    mDbHelper.deleteAll();
                    fillData();
                }
            }).setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.setMessage(R.string.test);
            builder.setTitle(R.string.confirmation);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void createNote() {
        String noteName = "Note " + mNoteNumber++;
        final EditText champs = (EditText) findViewById(R.id.champ_saisie);

        mDbHelper.createNote(champs.getText().toString(), champs.getText().toString());
    }
    private void fillData() {
        final ListView liste1 = (ListView) findViewById(R.id.liste);
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllNotes();
        startManagingCursor(c);

        String[] from = new String[] { NotesDbAdapter.KEY_TITLE };
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to);
         liste1.setAdapter(notes);
    }
}
