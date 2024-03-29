package com.f20.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    DatabaseHelper mDatabase;
    List<Contact> contactList;
    List<Contact> searchResults;
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeController swipeController;
    SearchView sv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = new DatabaseHelper(MainActivity.this);
        contactList = new ArrayList<>();
        searchResults = new ArrayList<>();
        configureRecyclerView();
        loadContacts();

        ImageButton btnAdd = findViewById(R.id.btn_add_contact);
        btnAdd.setImageResource(R.drawable.add_person);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
                intent.putExtra("id", -1);
                startActivity(intent);
            }
        });

        sv = findViewById(R.id.sv_contacts);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchDetails(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchDetails(s);
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
        sv.clearFocus();
    }

    private void configureRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_contacts);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                deleteContact(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void loadContacts() {
        contactList.clear();
        Cursor cursor = mDatabase.getAllContact();
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        cursor.getInt(0),       //id
                        cursor.getString(1) ,   //fname
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4)
                );
                contactList.add(contact);
            } while (cursor.moveToNext());
            cursor.close();
            Log.i(TAG, "loadFolder: " + "has total of contacts --> " + String.valueOf(contactList.size()));

            mAdapter = new ContactAdapter(this, contactList);
            recyclerView.setAdapter(mAdapter);
            updateContactCount(contactList.size());
        }

    }

    private void deleteContact(int position) {
        final Contact contact = mAdapter.getData().get(position);
        if(mDatabase.deleteContact(contact.getId())) {
            mAdapter.removeItem(position);
        }
        loadContacts();
    }


    private void updateContactCount(int num) {
        TextView tvCount = findViewById(R.id.tv_count);
        tvCount.setText("Total number of contacts: " + String.valueOf(num));
    }

    private void searchDetails(String input) {
        searchResults.clear();
        for(int i = 0; i<contactList.size(); ++i) {
            if(contactList.get(i).getFname().contains(input) || contactList.get(i).getFname().equalsIgnoreCase(input) ||
            contactList.get(i).getLname().contains(input) || contactList.get(i).getLname().equalsIgnoreCase(input) ||
            contactList.get(i).getAddress().contains(input) || contactList.get(i).getAddress().equalsIgnoreCase(input)) {
                searchResults.add(contactList.get(i));
            }
        }
        if(searchResults.size() == 0)
            Toast.makeText(MainActivity.this, "No results found for search input", Toast.LENGTH_SHORT).show();
        else {
            mAdapter = new ContactAdapter(this, searchResults);
            recyclerView.setAdapter(mAdapter);
            updateContactCount(searchResults.size());
        }
    }
}
