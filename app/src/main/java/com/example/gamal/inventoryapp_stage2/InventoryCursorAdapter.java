package com.example.gamal.inventoryapp_stage2;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class InventoryCursorAdapter extends CursorAdapter {

    private Context c;

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c);
        this.c = context;
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final int columnIdIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
        final String productID = cursor.getString(columnIdIndex);
        int productQuantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        final String productQuantity = cursor.getString(productQuantityColumnIndex);

        TextView productName = (TextView) view.findViewById(R.id.productName);
        TextView quantity = (TextView) view.findViewById(R.id.quantity);
        TextView price = (TextView) view.findViewById(R.id.price);
        Button scale = view.findViewById(R.id.scale);
        scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity Activity = (MainActivity) context;
                Activity.productSaleCount(Integer.valueOf(productID), Integer.valueOf(productQuantity));
            }
        });

        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        String itemName = cursor.getString(nameColumnIndex);
        String itemPrice = cursor.getString(priceColumnIndex);
        String itemQuantity = cursor.getString(quantityColumnIndex);


        productName.setText(itemName);
        quantity.setText(itemQuantity);
        price.setText(itemPrice);
    }
}
