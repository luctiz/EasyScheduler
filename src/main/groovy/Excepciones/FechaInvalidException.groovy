package Excepciones

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class FechaInvalidException extends ResponseStatusException{
    FechaInvalidException(HttpStatus status = HttpStatus.BAD_REQUEST, String reason) {
        super(status, reason)
    }
}
