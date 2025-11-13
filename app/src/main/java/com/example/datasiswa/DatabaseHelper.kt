package com.example.datasiswa

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DataSiswa.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "siswa"

        // Kolom tabel
        private const val COLUMN_NISN = "nisn"
        private const val COLUMN_NAMA = "nama"
        private const val COLUMN_TTL = "ttl"
        private const val COLUMN_JK = "jenisKelamin"
        private const val COLUMN_ALAMAT = "alamat"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val queryTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_NISN TEXT PRIMARY KEY,
                $COLUMN_NAMA TEXT,
                $COLUMN_TTL TEXT,
                $COLUMN_JK TEXT,
                $COLUMN_ALAMAT TEXT
            )
        """.trimIndent()
        db?.execSQL(queryTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert data siswa baru
    fun insertSiswa(siswa: Siswa): Boolean {
        val db = writableDatabase

        // Cek duplikat NISN
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_NISN = ?", arrayOf(siswa.nisn))
        if (cursor.count > 0) {
            cursor.close()
            db.close()
            return false
        }

        val values = ContentValues().apply {
            put(COLUMN_NISN, siswa.nisn)
            put(COLUMN_NAMA, siswa.nama)
            put(COLUMN_TTL, siswa.ttl)
            put(COLUMN_JK, siswa.jenisKelamin)
            put(COLUMN_ALAMAT, siswa.alamat)
        }

        val success = db.insert(TABLE_NAME, null, values)
        cursor.close()
        db.close()
        return success != -1L
    }

    // Ambil semua data siswa
    fun getSiswa(): List<Siswa> {
        val siswaList = mutableListOf<Siswa>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val siswa = Siswa(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NISN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TTL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JK)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALAMAT))
                )
                siswaList.add(siswa)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return siswaList
    }

    // Cari data berdasarkan NISN
    fun findDataByNISN(nisnSearch: String): Siswa? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_NISN = ?", arrayOf(nisnSearch))
        var siswa: Siswa? = null

        if (cursor.moveToFirst()) {
            siswa = Siswa(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NISN)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TTL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JK)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALAMAT))
            )
        }
        cursor.close()
        db.close()
        return siswa
    }

    // Update data siswa
    fun editDataSiswa(oldNisn: String, newSiswa: Siswa): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NISN, newSiswa.nisn)
            put(COLUMN_NAMA, newSiswa.nama)
            put(COLUMN_TTL, newSiswa.ttl)
            put(COLUMN_JK, newSiswa.jenisKelamin)
            put(COLUMN_ALAMAT, newSiswa.alamat)
        }

        val result = db.update(TABLE_NAME, values, "$COLUMN_NISN=?", arrayOf(oldNisn))
        db.close()
        return result > 0
    }

    // Hapus data siswa
    fun deleteSiswa(nisn: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_NISN=?", arrayOf(nisn))
        db.close()
        return result > 0
    }
}
