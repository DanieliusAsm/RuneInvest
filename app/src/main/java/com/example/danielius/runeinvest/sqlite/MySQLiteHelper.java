package com.example.danielius.runeinvest.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.danielius.runeinvest.api.model.Category;
import com.example.danielius.runeinvest.api.model.Item;
import com.example.danielius.runeinvest.api.model.Price;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danielius on 15/08/10.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Runescape";
    private static final int DATABASE_VERSION = 6;

    private static MySQLiteHelper helper;

    public static MySQLiteHelper getInstance(Context context){
        if(helper==null){
            helper = new MySQLiteHelper(context.getApplicationContext());
        }
        return helper;
    }

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE item(rs_id INTEGER, item_name VARCHAR(255),price VARCHAR(255),description VARCHAR(255)" +
                ",icon_url VARCHAR(255),price_change VARCHAR(255),price_change_percentage VARCHAR(255),category_id INTEGER)";
        db.execSQL(query);

        query = "CREATE TABLE my_item(id INTEGER PRIMARY KEY AUTOINCREMENT, item_id INTEGER)";
        db.execSQL(query);

        // not used. load each time.
        query = "CREATE TABLE item_graph(id INTEGER PRIMARY KEY AUTOINCREMENT, item_id INTEGER,time_milisec INTEGER, price INTEGER)";
        db.execSQL(query);

        // manually add deine
        query = "CREATE TABLE category(category_id INTEGER, category_name VARCHAR(255), icon VARCHAR(255))";
        db.execSQL(query);

        initializeCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS item");
        db.execSQL("DROP TABLE IF EXISTS my_item");
        db.execSQL("DROP TABLE IF EXISTS item_graph");
        db.execSQL("DROP TABLE IF EXISTS category"); // 38 nuo 0 iki 37

        this.onCreate(db);
    }

    public void addItems(List<Item> items){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values;

        for(Item item: items){
            int currentPrice = Integer.parseInt(item.getCurrentPrice().getItemPrice());
            int priceChange = Integer.parseInt(item.getPriceChange().getItemPrice());

            values = new ContentValues();
            values.put("rs_id", item.getItemId());
            values.put("item_name", item.getItemName());
            values.put("price",""+currentPrice);
            values.put("description",item.getItemDescription());
            values.put("icon_url", item.getIcon());
            values.put("price_change", ""+priceChange);
            double percentage = (priceChange*100)/currentPrice;
            values.put("price_change_percentage", ""+percentage);

            db.insert("item", null, values);
        }

        db.close();
    }

    public void addItemsInCategory(List<Item> items, int category){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values;

        for(Item item: items){
            //int currentPrice = Integer.parseInt(item.getCurrentPrice().getItemPrice());
            //int priceChange = Integer.parseInt(item.getPriceChange().getItemPrice());

            values = new ContentValues();
            values.put("rs_id", item.getItemId());
            values.put("item_name", item.getItemName());
            values.put("price",""+item.getCurrentPrice().getItemPrice());
            values.put("description",item.getItemDescription());
            values.put("icon_url", item.getIcon());
            values.put("price_change", ""+item.getPriceChange().getItemPrice());
            //double percentage = (priceChange*100)/currentPrice;
            //values.put("price_change_percentage", ""+percentage);
            values.put("category_id", ""+category);

            db.insert("item", null, values);
        }

        db.close();
    }

    public List<Item> getItemsInCategory(int categoryId){
        List<Item> items = new ArrayList<Item>();
        String query = "SELECT * FROM item WHERE category_id = ?";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{""+categoryId});

        if(cursor.getCount()==0){
            return null;
        }

        if(cursor.moveToFirst()){
            do{
                Item item = new Item();
                item.setItemId(cursor.getString(0));
                item.setItemName(cursor.getString(1));
                Price price = new Price();
                price.setItemPrice(cursor.getString(2));
                item.setCurrentPrice(price);
                item.setItemDescription(cursor.getString(3));
                item.setIcon(cursor.getString(4));
                price = new Price();
                price.setItemPrice(cursor.getString(5));
                item.setPriceChange(price);
                items.add(item);

            }while(cursor.moveToNext());
        }
        return items;
    }


    public void updateBasket(Item basket){
        SQLiteDatabase db = getWritableDatabase();
        //int id = basket.getId();
        //String name = basket.getBasketName();

        ContentValues values = new ContentValues();
        //values.put("basket_name",name);
        //db.update("basket", values, "id=?", new String[]{"" + id});
        db.close();
    }

    public List<Category> getCategories(){
        List<Category> categories = new ArrayList<Category>();
        String query = "SELECT * FROM category";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                Category category = new Category();
                category.setName(cursor.getString(1));

                categories.add(category);
            }while(cursor.moveToNext());
        }
        return categories;
    }

// todo custom icon urls
    private void initializeCategories(SQLiteDatabase db) {
        ContentValues values;

        String[] categories = {"Miscellaneous",
                "Ammo", "Arrows", "Bolts", "Construction materials", "Construction projects", "Cooking ingredients",
                "Costumes", "Crafting materials", "Familiars", "Farming produce", "Fletching materials", "Food and drink",
                "Herblore materials", "Hunting equipment", "Hunting produce", "Jewellery", "Mage armour", "Mage weapons",
                "Melee armour - low level", "Melee armour - mid level", "Melee armour - high level", "Melee weapons - low level",
                "Melee weapons - mid level", "Melee weapons - high level", "Mining and smithing", "Potions", "Prayer armour",
                "Prayer materials", "Range armour", "Range weapons", "Runecrafting", "Runes, Spells and Teleports", "Seeds",
                "Summoning scrolls", "Tools and containers", "Woodcutting product", "Pocket items"};

        for(int i=0;i<38;i++){
            values = new ContentValues();

            values.put("category_id",i);
            values.put("category_name",categories[i]);
            db.insert("category", null, values);
        }
    }

}
