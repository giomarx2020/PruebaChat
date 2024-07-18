package com.giosoto.chatpruebaradar.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.giosoto.chatpruebaradar.R
import de.hdodenhof.circleimageview.CircleImageView

class HolderMensaje(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var nombre: TextView = itemView.findViewById(R.id.nombreMensaje)
    var mensaje: TextView = itemView.findViewById(R.id.mensajeMensaje)
    var hora: TextView = itemView.findViewById(R.id.horaMensaje)
    var fotoMensajePerfil: CircleImageView = itemView.findViewById(R.id.fotoPerfilMensaje)
    var fotoMensaje: ImageView = itemView.findViewById(R.id.mensajeFoto)
}
