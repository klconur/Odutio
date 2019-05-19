package com.okb.odutio.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.okb.odutio.R;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

    private CursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create adapter once
        Context context = getActivity();
        int layout = android.R.layout.simple_list_item_1;
        Cursor c = null; // there is no cursor yet
        int flags = 0; // no auto-requery! Loader requeries.
        mAdapter = new SimpleCursorAdapter(context, layout, c, FROM, TO, flags);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // each time we are started use our listadapter
        setListAdapter(mAdapter);
        // and tell loader manager to start loading
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        String contactID = cursor.getString(0);
        Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",

                new String[]{contactID},
                null);

        List<String> numbers = new ArrayList<>();
        if (cursorPhone.moveToFirst()) {
            do {
                String number = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                numbers.add(number);
            } while (cursorPhone.moveToNext());
        }

        cursorPhone.close();
        if (numbers.size() == 0) {
            Toast.makeText(getActivity(), R.string.no_number, Toast.LENGTH_SHORT).show();
        } else if (numbers.size() == 1) {
            Intent intent = new Intent();
            intent.putExtra("phone_number", numbers.get(0));
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        } else {
            showListAlert(numbers);
        }
    }

    private void showListAlert(List<String> numbers) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle(R.string.select_number);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(numbers);

        builderSingle.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("phone_number", arrayAdapter.getItem(which));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        builderSingle.show();
    }

    // columns requested from the database
    private static final String[] PROJECTION = {
            Contacts._ID, // _ID is always required
            Contacts.DISPLAY_NAME_PRIMARY // that's what we want to display
    };

    // and name should be displayed in the text1 textview in item layout
    private static final String[] FROM = { Contacts.DISPLAY_NAME_PRIMARY };
    private static final int[] TO = { android.R.id.text1 };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // load from the "Contacts table"
        Uri contentUri = Contacts.CONTENT_URI;

        // no sub-selection, no sort order, simply every row
        // projection says we want just the _id and the name column
        return new CursorLoader(getActivity(),
                contentUri,
                PROJECTION,
                null,
                null,
                Contacts.DISPLAY_NAME_PRIMARY + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Once cursor is loaded, give it to adapter
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // on reset take any old cursor away
        mAdapter.swapCursor(null);
    }
}