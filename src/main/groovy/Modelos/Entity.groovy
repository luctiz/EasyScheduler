package Modelos

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id


abstract class Entity {
    @Id
    ObjectId id
}
