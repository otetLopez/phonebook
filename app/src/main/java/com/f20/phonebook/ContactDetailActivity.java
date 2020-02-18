package com.f20.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetailActivity extends AppCompatActivity {

    public static final String TAG = "ContactDetailActivity";
    EditText et_fname;
    EditText et_lname;
    EditText et_phone;
    EditText et_addr;

    DatabaseHelper mDatabase;
    private int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_phone = findViewById(R.id.et_phone);
        et_addr = findViewById(R.id.et_addr);

        mDatabase = new DatabaseHelper(ContactDetailActivity.this);
        Intent intent = getIntent();
        id = (Integer) intent.getExtras().get("id");
        if(id >= 0) {
            configureView();
        }


        Button btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_fname.getText().toString().isEmpty() || et_lname.getText().toString().isEmpty() ||
                et_phone.getText().toString().isEmpty() || et_addr.getText().toString().isEmpty()) {
                    Toast.makeText(ContactDetailActivity.this, "Cannot save.  Make sure all fields are filled.", Toast.LENGTH_SHORT).show();
                } else {
                    Contact contact = new Contact(et_fname.getText().toString(), et_lname.getText().toString(),
                            Integer.valueOf(et_phone.getText().toString()), et_addr.getText().toString());
                    if(id < 0) {
                        //new contact
                        if( mDatabase.addContact(contact)) {
                            clearFields();
                            Toast.makeText(ContactDetailActivity.this, "Contact saved to Database.", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(ContactDetailActivity.this, "Error when saving to Database.", Toast.LENGTH_SHORT).show();
                    } else {
                        if(mDatabase.updateContact(id, contact)) {
                            clearFields();
                            Toast.makeText(ContactDetailActivity.this, "Changes saved to Database.", Toast.LENGTH_SHORT).show();
                        } else
                        Toast.makeText(ContactDetailActivity.this, "Error when updating to Database.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void configureView() {
        Cursor cursor = mDatabase.getContact(id);
        Contact contact = new Contact();
        if (cursor.moveToFirst()) {
            contact = (new Contact(
                    cursor.getInt(0),       //nid
                    cursor.getString(1),    //fname
                    cursor.getString(2),    //lname
                    cursor.getInt(3),       //phone
                    cursor.getString(4)     //addr
            ));
            cursor.close();
        }
        et_fname.setText(contact.getFname());
        et_lname.setText(contact.getLname());
        et_phone.setText(String.valueOf(contact.getPhone()));
        et_addr.setText(contact.getAddress());
    }

    private void clearFields() {
        et_fname.setText("");
        et_lname.setText("");
        et_phone.setText("");
        et_addr.setText("");
    }
}
