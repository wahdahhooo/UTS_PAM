package com.example.datasiswa

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class SiswaAdapter(
    private var siswaList: MutableList<Siswa>,
    private val context: Context
) : RecyclerView.Adapter<SiswaAdapter.SiswaViewHolder>() {

    private val db = DatabaseHelper(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiswaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item_siswa, parent, false)
        return SiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SiswaViewHolder, position: Int) {
        val siswa = siswaList[position]

        holder.namaTextView.text = siswa.nama
        holder.nisnTextView.text = "NISN: ${siswa.nisn}"
        holder.ttlTextView.text = "TTL: ${siswa.ttl}"
        holder.jkTextView.text = "JK: ${siswa.jenisKelamin}"
        holder.alamatTextView.text = "Alamat: ${siswa.alamat}"

        // tombol edit
        holder.editButton.setOnClickListener {
            val intent = Intent(context, EditSiswaActivity::class.java)
            intent.putExtra("nisn_siswa", siswa.nisn)
            context.startActivity(intent)
        }

        // tombol delete
        holder.deleteButton.setOnClickListener {
            val success = db.deleteSiswa(siswa.nisn)
            if (success) {
                siswaList.removeAt(position)
                notifyItemRemoved(position)
                Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = siswaList.size

    class SiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTextView: TextView = itemView.findViewById(R.id.namaTextView)
        val nisnTextView: TextView = itemView.findViewById(R.id.nisnTextView)
        val ttlTextView: TextView = itemView.findViewById(R.id.ttlTextView)
        val jkTextView: TextView = itemView.findViewById(R.id.jkTextView)
        val alamatTextView: TextView = itemView.findViewById(R.id.alamatTextView)
        val editButton: ImageView = itemView.findViewById(R.id.editButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }
}
