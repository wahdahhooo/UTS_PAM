package com.example.datasiswa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tombolTambah: FloatingActionButton
    private lateinit var db: DatabaseHelper
    private lateinit var adapter: SiswaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // inisialisasi view
        recyclerView = findViewById(R.id.SiswaRecyclerView)
        tombolTambah = findViewById(R.id.TombolTambah)

        // setup database & recyclerView
        db = DatabaseHelper(this)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // tombol tambah data siswa
        tombolTambah.setOnClickListener {
            val intent = Intent(this, AddSiswaActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // ambil data dari database dan tampilkan ulang
        val siswaList = db.getSiswa()
        adapter = SiswaAdapter(siswaList.toMutableList(), this)
        recyclerView.adapter = adapter
    }
}
