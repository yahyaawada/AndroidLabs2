package com.example.yahya.androidlabs2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    // Definitions: Database name, table name, version

    private static final String DATABASE_NAME = "Messages.db" ;
    private static final String MESSAGE_TABLE = "MESSAGES_TABLE" ;
    private static final int DATABASE_VERSION = 3 ;

    // column names
 /*
    It is important to define an identifier for each entry in the table using _id.
    A number of Android classes and methods rely on this definition. */

    public static final  String KEY_ID = "_id";
    public static final  String KEY_MESSAGE ="MESSAGE";

    ListView lv;
    EditText ed;
    Button button;
    ArrayList<String> messages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        lv = (ListView)findViewById(R.id.listView);
        ed = (EditText)findViewById(R.id.editTextChat);
        button = (Button)findViewById(R.id.sendButton);

        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter =new ChatAdapter( this );
        lv.setAdapter (messageAdapter);

        //Create an object for opening a database:
        ChatDatabaseHelper cdh = new ChatDatabaseHelper(this);
        final SQLiteDatabase db = cdh.getWritableDatabase();

        //This does a query and stores the results:
        Cursor results = db.query(false, MESSAGE_TABLE, new String[] {KEY_ID, KEY_MESSAGE},
                null, null , null, null, null, null);

        results.moveToFirst();
        //How many rows in the results:
        int numResults = results.getCount();
        for(int i=0; i<numResults; i++){
            messages.add(i, results.getString(results.getColumnIndex(KEY_MESSAGE)));
            Log.i("Results: ", "Messages: " + messages.get(i));
            results.moveToNext();
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message;

                message = ed.getText().toString();
                messages.add(ed.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
                ed.setText("");

                //Use a content values object to insert them in the database
                ContentValues newData = new ContentValues();
                newData.put(KEY_MESSAGE, message);

                //Then insert
                db.insert(MESSAGE_TABLE, "" , newData);
            }
        });
    }


    private class ChatAdapter extends ArrayAdapter<String>{
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return messages.size();
        }

        public String getItem(int position){
            return messages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }
    }

    public class ChatDatabaseHelper extends SQLiteOpenHelper{

        public ChatDatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("CREATE TABLE " + MESSAGE_TABLE + " ( "+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ KEY_MESSAGE+ " text);");
            Log.i("ChatDatabaseHelper", "Calling onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS "+ MESSAGE_TABLE); //delete what was there previously
            onCreate(db);
            Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion="+ newVersion);
        }

    }

    protected void onDestroy(){
        super.onDestroy();
        final String ACTIVITY_NAME = "ChatWindow";
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

}
