package se.servise;

import org.springframework.data.repository.CrudRepository;
import se.entity.Room;

public interface RoomRepository extends CrudRepository<Room, Integer> {
}
