package com.example.datasiswa

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.datasiswa.databinding.ActivityEditSiswaBinding

class EditSiswaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditSiswaBinding
    private lateinit var db: DatabaseHelper
    private var oldNisn: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)
        oldNisn = intent.getStringExtra("nisn_siswa")

        // ambil data lama dan tampilkan
        oldNisn?.let { nisn ->
            val siswa = db.findDataByNISN(nisn)
            siswa?.let {
                binding.InputNisn.setText(it.nisn)
                binding.InputNama.setText(it.nama)
                binding.InputTtl.setText(it.ttl)
                binding.InputJk.setText(it.jenisKelamin)
                binding.InputAlamat.setText(it.alamat)
            }
        }

        binding.BtnUpdate.setOnClickListener {
            val nisnBaru = binding.InputNisn.text.toString().trim()
            val namaBaru = binding.InputNama.text.toString().trim()
            val ttlBaru = binding.InputTtl.text.toString().trim()
            val jkBaru = binding.InputJk.text.toString().trim()
            val alamatBaru = binding.InputAlamat.text.toString().trim()

            if (nisnBaru.isEmpty() || namaBaru.isEmpty() || ttlBaru.isEmpty() || jkBaru.isEmpty() || alamatBaru.isEmpty()) {
                Toast.makeText(this, "Isi semua kolom terlebih dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val siswaBaru = Siswa(nisnBaru, namaBaru, ttlBaru, jkBaru, alamatBaru)
            val success = db.editDataSiswa(oldNisn ?: "", siswaBaru)
            if (success) {
                Toast.makeText(this, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal memperbarui data!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
