package com.example.its_art.repository;

import com.example.its_art.entity.Room;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoomRepository extends CrudRepository<Room, Integer> {
    @Modifying
    @Query("update Room u set u.state = ?1 where u.id = ?2")
    void setFixedFirstnameFor(boolean state, Integer id);

}