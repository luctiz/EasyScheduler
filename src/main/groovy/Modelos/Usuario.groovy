package Modelos

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("usuarios")
class Usuario extends Entity {
    @Id
    public ObjectId _id
    @Indexed
    public String nombreUsuario
    public String contrasenia
    public Equipo[] equipos = []

    Usuario(String nombreUsuario, String contrasenia) {
        this.nombreUsuario = nombreUsuario
        this.contrasenia = contrasenia
        super
    }
    Usuario() {

    }

}
