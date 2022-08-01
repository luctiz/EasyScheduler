package Excepciones

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UsuarioNoAsignadoATareaException extends ResponseStatusException {
    UsuarioNoAsignadoATareaException(HttpStatus status = HttpStatus.BAD_REQUEST, String reason = "El Usuario no esta asignado a la tarea indicada") {
        super(status, reason)
    }
}
