package com.example.android.contactregapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class ContactsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_FIRSTNAME, MySQLiteHelper.COLUMN_LASTNAME, MySQLiteHelper.COLUMN_PATH};

    public ContactsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            contactList.add(contact);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return contactList;
    }

    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(cursor.getLong(0));
        contact.setFirstName(cursor.getString(1));
        contact.setLastName(cursor.getString(2));
        contact.setPicPath(cursor.getString(3));
        return contact;
    }

    public Contact createContact(String firstName, String lastName, String picPath) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_FIRSTNAME, firstName);
        values.put(MySQLiteHelper.COLUMN_LASTNAME, lastName);
        values.put(MySQLiteHelper.COLUMN_PATH, picPath);

        long insertId = database.insert(MySQLiteHelper.TABLE_CONTACTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Contact newContact = cursorToContact(cursor);
        cursor.close();
        return newContact;
    }
}
