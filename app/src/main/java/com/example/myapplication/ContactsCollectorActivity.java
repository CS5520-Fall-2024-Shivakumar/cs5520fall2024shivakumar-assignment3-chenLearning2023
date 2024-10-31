package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ContactsCollectorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddContact;
    private ContactAdapter contactAdapter;
    private List<Contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacts_collector);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //some dummy Data
        contactList.add(new Contact("John Doe", "1234567890"));
        contactList.add(new Contact("Jane Smith", "2345678901"));
        contactList.add(new Contact("Emily Johnson", "3456789012"));
        contactList.add(new Contact("Michael Brown", "4567890123"));

        recyclerView = findViewById(R.id.recyclerView_contacts);
        fabAddContact = findViewById(R.id.fab_add_contact);

        contactAdapter = new ContactAdapter(contactList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(contactAdapter);

        fabAddContact.setOnClickListener(v -> openAddContactDialog());

    }

    private void openAddContactDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_contact, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.et_name);
        EditText etPhone = dialogView.findViewById(R.id.et_phone);
        Button btnAdd = dialogView.findViewById(R.id.btn_add);

        AlertDialog dialog = builder.create();

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String phone = etPhone.getText().toString();

            if (!name.isEmpty() && !phone.isEmpty()) {
                contactList.add(new Contact(name, phone));
                contactAdapter.notifyDataSetChanged();
                showSnackbar("Contact added successfully", "Undo",  view -> {
                    //"Undo" the add action
                    contactList.remove(contactList.size() - 1);
                    contactAdapter.notifyDataSetChanged();
                });
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter both name and phone number", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }

    private void showSnackbar(String message, String actionText, View.OnClickListener action) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.recyclerView_contacts), message, Snackbar.LENGTH_LONG);
        snackbar.setAction(actionText, action);
        snackbar.show();
    }
}