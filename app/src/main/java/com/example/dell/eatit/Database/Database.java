package com.example.dell.eatit.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.dell.eatit.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 1/26/2018.
 */

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME="Eatme.db";
    private static final int DB_VER=1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts()
    {
        SQLiteDatabase db=getReadableDatabase();
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();
        String[] sqlSelect={"ProductId","ProductName","Quantitiy","Price","Discount"};
        String sqlTable="OrderDetail";
        qb.setTables(sqlTable);
        Cursor c=qb.query(db,sqlSelect,null,null,null,null,null);
        final List<Order> results=new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                results.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantitiy")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))));
            }while(c.moveToNext());
        }
        return  results;
    }

    public void addToCart(Order order)
    {
        SQLiteDatabase db=getReadableDatabase();
        String querry=String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantitiy,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
        order.getProductId(),order.getProductName(),order.getQuantitiy(),order.getPrice(),order.getDiscount());
        db.execSQL(querry);
    }
    public void CleanCart()
    {
        SQLiteDatabase db=getReadableDatabase();
        String querry=String.format("DELETE FROM OrderDetail");
        db.execSQL(querry);

    }
}
