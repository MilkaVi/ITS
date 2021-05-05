package com.example.its_art.servise;

import com.example.its_art.entity.Room;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public interface RoomRepository extends CrudRepository<Room, Integer> {
    @Modifying
    @Query("update Room u set u.state = ?1 where u.id = ?2")
    void setFixedFirstnameFor(boolean state, Integer id);

}