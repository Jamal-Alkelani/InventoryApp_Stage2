package com.example.gamal.inventoryapp_stage2;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditItem extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private EditText productName;
    private EditText price;
    private EditText quantity;
    private EditText supplierName;
    private EditText supplierPhone;
    private Button decButton;
    private Button incButton;
    private Button supplyButton;
    private Button delete;
    private Uri mCurrentUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        decButton = findViewById(R.id.Decrease);
        incButton = findViewById(R.id.Increase);
        supplyButton = findViewById(R.id.supply);
        delete = findViewById(R.id.delete);

        productName = findViewById(R.id.productName_Edit);
        price = findViewById(R.id.price_Edit);
        quantity = findViewById(R.id.quantity_Edit);
        supplierName = findViewById(R.id.supplierName_Edit);
        supplierPhone = findViewById(R.id.supplierPhone_Edit);

        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        if (mCurrentUri == null) {
            setTitle("Insert New Item");
            decButton.setVisibility(View.GONE);
            incButton.setVisibility(View.GONE);
            supplyButton.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);

        } else {
            setTitle("Update Item");
            decButton.setVisibility(View.VISIBLE);
            incButton.setVisibility(View.VISIBLE);
            supplyButton.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(0, null, this);
        }

        decButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                if (quantity.getText().toString().equals("0")) {
                    Toast.makeText(getApplicationContext(), "You have no Items to decrease", Toast.LENGTH_SHORT);
                } else {
                    values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, Integer.parseInt(quantity.getText().toString()) - 1);
                    int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
                }


            }
        });

        incButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, Integer.parseInt(quantity.getText().toString()) + 1);
                int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, Integer.parseInt(quantity.getText().toString()) + 1);
                int rowsAffected = getContentResolver().delete(mCurrentUri, null, null);
                finish();
            }
        });

        supplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + supplierPhone.getText().toString()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        invalidateOptionsMenu();
    }

    private void saveItem() {
        String nameString = productName.getText().toString().trim();
        String priceString = price.getText().toString().trim();
        String quantityString = quantity.getText().toString().trim();
        String supplierPhoneString = supplierPhone.getText().toString().trim();
        String supplierNameString = supplierName.getText().toString().trim();


        if (mCurrentUri == null &&
                TextUtils.isEmpty(nameString) || TextUtils.isEmpty(priceString) ||
                TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(supplierPhoneString) || TextUtils.isEmpty(supplierNameString)) {
            Toast.makeText(getApplicationContext(), "Oops some info. is missing", Toast.LENGTH_SHORT).show();
            return;
        }


        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE, priceString);
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantityString);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneString);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME, supplierNameString);


        if (mCurrentUri == null) {

            Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
        } else {
            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_update) {
            saveItem();
            // Exit activity
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE,
                InventoryContract.InventoryEntry.COLUMN_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentUri,         // Query the content URI
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE);

            // Extract out the value from the Cursor for the given column index
            String xname = cursor.getString(nameColumnIndex);
            int xprice = cursor.getInt(priceColumnIndex);
            int xquantity = cursor.getInt(quantityColumnIndex);
            String xsupplierName = cursor.getString(supplierNameColumnIndex);
            int xsupplierPhone = cursor.getInt(supplierPhoneColumnIndex);
            // Update the views on the screen with the values from the database
            productName.setText(xname);
            price.setText(xprice + "");
            quantity.setText(Integer.toString(xquantity));
            supplierName.setText(xsupplierName);
            supplierPhone.setText(xsupplierPhone + "");


        }
    }

    @Override

    public void onLoaderReset(Loader<Cursor> loader) {
        productName.setText("");
        price.setText("");
        quantity.setText("");
        supplierName.setText("");
        supplierPhone.setText("");
    }
}
