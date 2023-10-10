package com.example.sqliteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, "customer.db", null, 1) {
    public final var CUSTOMER_TABLE = "CUSTOMER_TABLE"
    public final var COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME"
    public final var COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE"
    public final var COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER"
    public final var COLUMN_ID = "ID"

    override fun onCreate(db: SQLiteDatabase?) {
        var createTableStatement: String =
            "CREATE TABLE " + CUSTOMER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_AGE + " INT, " + COLUMN_ACTIVE_CUSTOMER + " TEXT)"

        db?.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addOne(customerModel: CustomerModel): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName())
        cv.put(COLUMN_CUSTOMER_AGE, customerModel.getAge())
        Log.d("Debug", customerModel.getActive().toString())
        cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.getActive().toString())

        val insert = db.insert(CUSTOMER_TABLE, null, cv)
        val numberToCompare: Long = -1
        if (insert == numberToCompare)
            return false
        return true
    }

    fun getEveryone(): List<CustomerModel> {
        val returnList: ArrayList<CustomerModel> = ArrayList<CustomerModel>()
        val queryString: String = "SELECT * FROM " + CUSTOMER_TABLE

        val db: SQLiteDatabase = this.readableDatabase

        val cursor: Cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val customerID = cursor.getInt(0)
                val customerName: String = cursor.getString(1)
                val customerAge: Int = cursor.getInt(2)
                val customerActive: Boolean = cursor.getString(3) == "true"

                val newCustomer: CustomerModel =
                    CustomerModel(customerID, customerName, customerAge, customerActive)
                returnList.add(newCustomer)
            } while (cursor.moveToNext())
        } else {

        }
        cursor.close()
        db.close()
        return returnList
    }

    fun getFiltered(filter: String): List<CustomerModel> {
        val returnList: ArrayList<CustomerModel> = ArrayList<CustomerModel>()
        val queryString: String =
            "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_CUSTOMER_NAME + " LIKE '%" + filter + "%' OR " + COLUMN_CUSTOMER_AGE + " LIKE '%" + filter + "%' OR " + COLUMN_ACTIVE_CUSTOMER + " LIKE '%" + filter + "%' OR " + COLUMN_ID + " LIKE '%" + filter + "%' ;"

        val db: SQLiteDatabase = this.readableDatabase

        val cursor: Cursor = db.rawQuery(queryString, null)

        if(cursor.moveToFirst()){
            do{
                val customerID = cursor.getInt(0)
                val customerName: String = cursor.getString(1)
                val customerAge: Int = cursor.getInt(2)
                val customerActive: Boolean = cursor.getString(3) == "true"

                val newCustomer: CustomerModel =
                    CustomerModel(customerID, customerName, customerAge, customerActive)
                returnList.add(newCustomer)
            }while(cursor.moveToNext())
        } else {

        }
        cursor.close()
        db.close()
        return returnList
    }

    fun deleteOne(customerModel: CustomerModel): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val queryString: String =
            "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_ID + " = " + customerModel.getId()

        val cursor: Cursor = db.rawQuery(queryString, null)

        return cursor.moveToFirst()
    }

    fun updateOne(customerModel: CustomerModel, newName: String, newAge: Int, newActive: Boolean): Boolean {
        Log.d("Debug", newActive.toString())
        val db: SQLiteDatabase = this.writableDatabase
        val queryString: String =
            "UPDATE " + CUSTOMER_TABLE + " SET " + COLUMN_CUSTOMER_NAME + " = '" + newName + "', " + COLUMN_CUSTOMER_AGE + " = " + newAge + ", " + COLUMN_ACTIVE_CUSTOMER + " = '" + newActive.toString() + "' WHERE " + COLUMN_ID + " = " + customerModel.getId() + ";"
        val cursor: Cursor = db.rawQuery(queryString, null)
        return cursor.moveToFirst()
    }
}