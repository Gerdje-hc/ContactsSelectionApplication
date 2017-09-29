package com.apps.gerdjes.contactsselectionapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ContactIntentActivity extends AppCompatActivity {

    private List<ContactObject> contactsList;
    private int RUNTIME_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate you view
        setContentView(R.layout.activity_content_intent);
        ListView intentListView = (ListView) findViewById(R.id.listView1);

        contactsList = new ArrayList<>();

        contactsList.add(new ContactObject("Android One", "111-1111-1111", "www.AndroidATC.com"));
        contactsList.add(new ContactObject("Android Two", "222-2222-2222", "www.AndroidATC.com"));
        contactsList.add(new ContactObject("Android Three", "333-3333-3333", "www.AndroidATC.com"));
        contactsList.add(new ContactObject("Android Four", "444-4444-4444", "www.AndroidATC.com"));

        List<String> listName = new ArrayList<>();
        for (int i = 0; i < contactsList.size(); i++) {
            listName.add(contactsList.get(i).getName());
        }
        // initialize the ArrayAdapter Object

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ContactIntentActivity.this, android.R.layout.simple_list_item_1, listName);
        intentListView.setAdapter(adapter);
        intentListView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ContactIntentActivity.this, ContactPageActivity.class);
                i.putExtra("Object", contactsList.get(position));
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            return;
        }

        Bundle resultdata = data.getExtras();
        String value = resultdata.getString("value");
        switch (resultCode) {
            case Constants.PHONE:
                //IMPLICIT intent to make a call
                makeCall(value);

                break;
            case Constants.WEBSITE:
                //Implicit intent to visit website
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + value)));
                break;
        }

    }
    // We are calling this methode to check the permission status

    private void makeCall(String value) {
        // getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        // if permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + value)));

        } else {
            requestCallPermission();
        }
    }

    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {

            // if the user has denied the permission previously your code will come to this block
            // here you can explain why you need this permission
            // Explain here why you need this permission
            explainPermissionDialog();
        }
        //and finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, RUNTIME_PERMISSION_CODE);

    }


    //This method will be called when the user will tap on allow or deny


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); {
            //Checking the request code of our request
            if (requestCode == RUNTIME_PERMISSION_CODE) {

                //if permission is granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Displaying a Toast

                    Toast.makeText(this, "Permission granted now you can make the phone caal", Toast.LENGTH_LONG).show();
                } else {
                    // Displaying anither toast if permission is not granted
                    Toast.makeText(this, "Oops you just denied permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void explainPermissionDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //set title

        alertDialogBuilder.setMessage("This app requires Call permission to make phone calls. Please grant the permission")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }

                );
        // Create alet dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        //show it
        alertDialog.show();;

    }
}

