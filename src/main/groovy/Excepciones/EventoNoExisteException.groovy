package Excepciones

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class EventoNoExisteException extends ResponseStatusException {

    EventoNoExisteException(HttpStatus status = HttpStatus.NOT_FOUND, String reason) {
        super(status, reason)
    }
}
