package Controladores

import Modelos.Entity
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class ApiControllerBase<T extends Entity> {


    Logger logger = LoggerFactory.getLogger(ApiControllerBase.class)

    T created(T entity) {
        logger.info("Se creo la entidad ${entity.getClass()} con Id ${entity.id}")
        return entity
    }

    T updated(T entity) {
        logger.info("Se actualizo la entidad ${entity.getClass()} con Id ${entity.id}")
        return entity
    }
}
