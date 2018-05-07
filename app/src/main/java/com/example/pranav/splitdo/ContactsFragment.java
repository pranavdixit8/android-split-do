package com.example.pranav.splitdo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactsFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    public static final String TAG = ContactsFragment.class.getSimpleName();

    private String mSearchString;

    private final static String[] FROM_COLUMNS = {
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };

    private final static int[] TO_IDS = { android.R.id.text1 };

    ListView mContactsList;
    long mContactId;
    String mContactKey;
    Uri mContactUri;
    private SimpleCursorAdapter mCursorAdapter;

    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY

            };



    private static final int CONTACT_ID_INDEX = 0;

    private static final int LOOKUP_KEY_INDEX = 1;


    private static final String SELECTION =

                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" ;

    private String[] mSelectionArgs = { mSearchString };



    public ContactsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_contact_list,
                container, false);

        mContactsList =
                (ListView) view.findViewById(android.R.id.list);

        mCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.contacts_list_item,
                null,
                FROM_COLUMNS, TO_IDS,
                0);

        mContactsList.setAdapter(mCursorAdapter);
        mContactsList.setOnItemClickListener(this);


        getLoaderManager().initLoader(0, null, this);
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d(TAG, "onDestroyView: " + mContactsList);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        mSelectionArgs[0] = "%" + mSearchString + "%";
        return new CursorLoader(
                getActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                SELECTION,
                mSelectionArgs,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);
        Log.d(TAG, "onLoadFinished: "  +mSearchString);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);

    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//        Cursor cursor = parent.getAdapter().getCursor();
//        // Move to the selected contact
//        cursor.moveToPosition(position);
//        // Get the _ID value
//        mContactId = getLong(CONTACT_ID_INDEX);
//        // Get the selected LOOKUP KEY
//        mContactKey = getString(LOOKUP_KEY_INDEX);
//        // Create the contact's content Uri
//        mContactUri = ContactsContract.Contacts.getLookupUri(mContactId, mContactKey);
//        /*
//         * You can use mContactUri as the content URI for retrieving
//         * the details for a contact.
//         */

//    }

    public void setSearchString(String text) {
        mSearchString = text;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = ((SimpleCursorAdapter) adapterView.getAdapter()).getCursor();

        cursor.moveToPosition(i);
        Log.d(TAG, "onItemClick: ");
        mContactId = cursor.getLong(CONTACT_ID_INDEX);
        Toast.makeText(getContext(),mContactId +"",Toast.LENGTH_SHORT).show();
        mContactKey = cursor.getString(LOOKUP_KEY_INDEX);
        mContactUri = ContactsContract.Contacts.getLookupUri(mContactId, mContactKey);


        /*
         * You can use mContactUri as the content URI for retrieving
         * the details for a contact.
         */




    }
}



