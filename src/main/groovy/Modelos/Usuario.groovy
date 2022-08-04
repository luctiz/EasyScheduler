package Modelos

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("usuarios")
class Usuario extends Entity {
    @Indexed
    public String nombreUsuario
    public String contrasenia
    public Equipo[] equipos = []

    Usuario(String nombreUsuario, String contrasenia, ObjectId _id) {
        this.nombreUsuario = nombreUsuario
        this.contrasenia = contrasenia
        super._id = _id
    }
    Usuario() {

    }

}
