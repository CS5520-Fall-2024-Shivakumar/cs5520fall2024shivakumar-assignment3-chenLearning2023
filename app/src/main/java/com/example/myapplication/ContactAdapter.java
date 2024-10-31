package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> contactList;
    private Context context;

    public ContactAdapter(List<Contact> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhone());

        //Call somebody
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + contact.getPhone()));
            context.startActivity(intent);
        });

        // Edit button logic
        holder.btnEdit.setOnClickListener(v -> openEditDialog(position, contact));

        holder.btnDelete.setOnClickListener(v -> {
            // Store the contact and position before removing
            Contact deletedContact = contactList.get(position);
            int deletedPosition = position;

            // Remove the contact from the list
            contactList.remove(position);
            notifyItemRemoved(position);

            // Show Snackbar with Undo option
            Snackbar snackbar = Snackbar.make(holder.itemView, "Contact deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", view -> {
                // Re-insert the deleted contact at its original position
                contactList.add(deletedPosition, deletedContact);
                notifyItemInserted(deletedPosition);
            });
            snackbar.show();
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    // Method to open edit dialog
    private void openEditDialog(int position, Contact contact) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_contact, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.et_name);
        EditText etPhone = dialogView.findViewById(R.id.et_phone);
        Button btnAdd = dialogView.findViewById(R.id.btn_add);

        // Store the original data
        String originalName = contact.getName();
        String originalPhone = contact.getPhone();

        // Set existing data
        etName.setText(originalName);
        etPhone.setText(originalPhone);
        btnAdd.setText("Update Contact");

        AlertDialog dialog = builder.create();

        // Update contact on button click
        btnAdd.setOnClickListener(v -> {
            String updatedName = etName.getText().toString();
            String updatedPhone = etPhone.getText().toString();

            if (!updatedName.isEmpty() && !updatedPhone.isEmpty()) {
                contact.setName(updatedName);
                contact.setPhone(updatedPhone);
                notifyItemChanged(position);
                dialog.dismiss();
                Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Contact updated successfully", Snackbar.LENGTH_LONG);

                // Set the Undo action on the Snackbar
                snackbar.setAction("Undo", view -> {
                    // Revert to the original data if "Undo" is tapped
                    contact.setName(originalName);
                    contact.setPhone(originalPhone);
                    notifyItemChanged(position);
                });

                snackbar.show();
            } else {
                Toast.makeText(context, "Please enter both name and phone number", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone;
        ImageButton btnEdit;
        ImageButton btnDelete;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public void testF(){
        
    }
}

