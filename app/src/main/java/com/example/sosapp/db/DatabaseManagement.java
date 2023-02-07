package com.example.sosapp.db;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.sosapp.db.Table.Instances;
import com.example.sosapp.helper.Helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public final class DatabaseManagement extends SQLiteOpenHelper {
    @NotNull
    private Activity context;

    public DatabaseManagement(@NotNull Activity context) {
        super(context, new Instances(context).DATABASE, null, 1);
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public void onCreate(@Nullable SQLiteDatabase db) {
        String query = "CREATE TABLE " + new Instances(context).TABLE_NAME + " (" + Instances.PHONE_NUMBER + " PRIMARY KEY ," + Instances.NAME + " VARCHAR(255))";
        if (db != null) {
            db.execSQL(query);
        }
    }

    public void onUpgrade(@Nullable SQLiteDatabase db, int p1, int p2) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS " + new Instances(context).TABLE_NAME);
        }
        this.onCreate(db);
    }

    public boolean insertData(@NotNull Helper helper) {
        Intrinsics.checkNotNullParameter(helper, "helper");

        boolean var2;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Instances.PHONE_NUMBER, helper.getNumber());
            contentValues.put(Instances.NAME, helper.getName());
            long result = db.insert(new Instances(context).TABLE_NAME, null, contentValues);
            if (result == 0L) {
                throw new Exception();
            }var2 = true;
        } catch (Exception var6) {
            var2 = false;
        }
        return var2;
    }

    @SuppressLint({"Range"})
    @NotNull
    public List<Helper> readData() {
        List<Helper> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + new Instances(context).TABLE_NAME;
        Cursor result = db.rawQuery(query, null);
        if (result.moveToFirst()) {
            do {
                Helper helper = new Helper("", "");
                String var10001 = result.getString(result.getColumnIndex(Instances.PHONE_NUMBER));
                Intrinsics.checkNotNullExpressionValue(var10001, "result.getString(result.…(Instances.PHONE_NUMBER))");
                helper.setNumber(var10001);
                var10001 = result.getString(result.getColumnIndex(Instances.NAME));
                Intrinsics.checkNotNullExpressionValue(var10001, "result.getString(result.…umnIndex(Instances.NAME))");
                helper.setName(var10001);
                list.add(helper);
            } while(result.moveToNext());
        }
        db.close();
        return list;
    }

    public boolean delete(@NotNull Helper helper) {
        Intrinsics.checkNotNullParameter(helper, "helper");
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(new Instances(context).TABLE_NAME, Instances.PHONE_NUMBER + "=?" , new String[]{helper.getNumber()});
            db.close();
            return true;
        } catch (Exception var4) {
            Toast.makeText(this.context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean update(@NotNull Helper helper , Helper oldHelper){
        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(Instances.NAME , helper.getName());
            contentValues.put(Instances.PHONE_NUMBER , helper.getNumber());
            db.update(new Instances(context).TABLE_NAME, contentValues , Instances.PHONE_NUMBER + "=?" , new String[] {oldHelper.getNumber()});
            db.close();
            return true;
        }
        catch (Exception e) {
            Toast.makeText(context , "Failed to update" , Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}

