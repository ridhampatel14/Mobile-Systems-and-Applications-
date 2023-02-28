package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import edu.stevens.cs522.base.DateUtils;
import edu.stevens.cs522.base.InetAddressUtils;
import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.contracts.PeerContract;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ViewPeerActivity.class.getCanonicalName();

    public static final String PEER_KEY = "peer";

    private Peer peer;

    /*
     * UI for messages sent by this peer
     */
    private ListView messageList;

    private SimpleCursorAdapter messagesAdapter;

    static final private int LOADER_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        peer = getIntent().getParcelableExtra(PEER_KEY);
        if (peer == null) {
            throw new IllegalArgumentException("Expected peer as intent extra");
        }

        // TODO Set the fields of the UI
        TextView username = (TextView) findViewById(R.id.view_user_name);
        TextView timestamp = (TextView) findViewById(R.id.view_timestamp);
        TextView location = (TextView) findViewById(R.id.view_location);
        messageList=(ListView)findViewById(R.id.message_list);
        username.setText(getString(R.string.view_user_name,peer.name));
        timestamp.setText(getString(R.string.view_timestamp,formatTimestamp(peer.timestamp)));
        location.setText(getString(R.string.view_location, peer.latitude, peer.longitude));

        // TODO use SimpleCursorAdapter (with flags=0 and null initial cursor) to display the messages received.
        // You can use android.R.simple_list_item_1 as layout for each row.
        String[] from=new String[]{MessageContract.MESSAGE_TEXT};
        int[] to=new int[]{R.id.peer_message_list};
        this.messagesAdapter=new SimpleCursorAdapter(this,R.layout.peer_message_list,null,from,to,0);
        messageList.setAdapter(messagesAdapter);

        // TODO Use loader manager to initiate a query of the database
        // Make sure to use the Jetpack library, not the deprecated core implementation.
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    private static String formatTimestamp(Date timestamp) {
        LocalDateTime dateTime = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                String selection = (MessageContract.SENDER + "=?");
                String[] selectionArgs = { peer.name };
                // TODO use a CursorLoader to initiate a query on the database
                return new CursorLoader(this, MessageContract.CONTENT_URI,null,selection,selectionArgs,null);

            default:
                throw new IllegalStateException(("Unexpected loader id: " + id));
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // TODO populate the UI with the result of querying the provider
        this.messagesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // TODO reset the UI when the cursor is empty
        this.messagesAdapter.swapCursor(null);
    }
}
