package com.example.android.contactregapp;

import android.app.ListActivity;
import android.os.Bundle;
import java.util.List;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class contactListActivity extends AppCompatActivity {
    private ContactsDataSource datasource;
    private TextView displayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        displayList = (TextView) findViewById(R.id.contactList);

        datasource = new ContactsDataSource(this);
        datasource.open();

        readData();
    }

    public void readData() {
        List<Contact> values = datasource.getAllContacts();
        for (Contact contact : values){
            displayList.append(contact.getFirstName() + " ");
            displayList.append(contact.getLastName() + "\n");
            displayList.append(contact.getPicPath() + "\n");
        }
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();

    }
}
