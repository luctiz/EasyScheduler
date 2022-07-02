package Excepciones

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UsuarioNoExisteException extends ResponseStatusException{

    UsuarioNoExisteException(HttpStatus status = HttpStatus.NOT_FOUND, String reason) {
        super(status, reason)
    }
}
