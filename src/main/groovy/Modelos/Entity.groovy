package Modelos

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.MongoId

abstract class Entity {
    @Id
    public ObjectId _id

    Entity(ObjectId _id) {
        this._id = _id
    }

    Entity() {

    }
}
