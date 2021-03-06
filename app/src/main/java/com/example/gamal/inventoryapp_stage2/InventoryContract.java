package com.example.gamal.inventoryapp_stage2;

import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {


    public static final String CONTENT_AUTHORITY = "com.example.gamal.inventoryapp_stage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";

    private InventoryContract() {

    }


    public static final class InventoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        public final static String TABLE_NAME = "inventory";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_PRODUCT_NAME = "name";

        public final static String COLUMN_PRICE = "price";

        public final static String COLUMN_QUANTITY = "quantity";

        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";

        public final static String COLUMN_SUPPLIER_PHONE = "supplier_phone";
    }
}
