package com.example.datasiswa

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.datasiswa.databinding.ActivityAddSiswaBinding

class AddSiswaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSiswaBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        // tombol simpan data
        binding.BtnSimpan.setOnClickListener {
            val nisn = binding.InputNisn.text.toString().trim()
            val nama = binding.InputNama.text.toString().trim()
            val ttl = binding.InputTtl.text.toString().trim()
            val jk = binding.InputJk.text.toString().trim()
            val alamat = binding.InputAlamat.text.toString().trim()

            if (nisn.isEmpty() || nama.isEmpty() || ttl.isEmpty() || jk.isEmpty() || alamat.isEmpty()) {
                Toast.makeText(this, "Isi semua kolom dulu ya!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val siswa = Siswa(nisn, nama, ttl, jk, alamat)
            val success = db.insertSiswa(siswa)

            if (success) {
                Toast.makeText(this, "Data siswa berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish() // kembali ke MainActivity
            } else {
                Toast.makeText(this, "NISN sudah terdaftar!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
