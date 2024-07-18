package com.giosoto.chatpruebaradar.notifications

import com.giosoto.chatpruebaradar.model.Mensaje


class MensajeEnviar : Mensaje {
    var hora: Map<*, *>? = null

    constructor() : super()

    constructor(hora: Map<*, *>?) : super() {
        this.hora = hora
    }

    constructor(
        mensaje: String?,
        nombreUsuario: String?,
        fotoPerfil: String?,
        type_mensaje: String?,
        hora: Map<*, *>?
    ) : super(mensaje, null, nombreUsuario, fotoPerfil, type_mensaje) {
        this.hora = hora
    }

    constructor(
        mensaje: String?,
    urlFoto: String?,
    nombre: String?,
    fotoPerfil: String?,
    type_mensaje: String?,
    hora: Map<*, *>?
    ) : super(mensaje, urlFoto, nombre, fotoPerfil, type_mensaje) {
        this.hora = hora
    }


}
