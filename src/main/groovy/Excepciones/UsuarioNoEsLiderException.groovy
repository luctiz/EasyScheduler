package Excepciones

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UsuarioNoEsLiderException extends ResponseStatusException {
    UsuarioNoEsLiderException(HttpStatus status = HttpStatus.BAD_REQUEST, String reason) {
        super(status, reason)
    }
}
