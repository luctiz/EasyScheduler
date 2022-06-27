package Modelos

import org.springframework.data.annotation.Id


abstract class Entity {
    @Id
    String id
}
