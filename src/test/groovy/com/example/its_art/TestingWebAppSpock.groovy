package com.example.its_art

import com.example.its_art.entity.Room
import com.example.its_art.repository.RoomRepository
import com.example.its_art.servise.RoomSevice
import org.springframework.web.server.ResponseStatusException
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

class TestingWebAppSpock  extends Specification {

//    @Autowired
//    RoomSevice roomSevice


    def "when context is loaded then all expected beans are created"() {
        given:
        def room = new Room("test", "Belarus")
        def roomIn = Mock(Room.class)
        def roomRepo = Mock(RoomRepository.class)
        RoomSevice roomService = new RoomSevice(roomRepo)
        1 * roomRepo.save(roomIn) >> room //задаём поведение для заглушки
        when:
        def newRoom = roomService.save(roomIn)
        then:
        newRoom == room
    }

    def "when get exist room return room"(){
        given:
        def room = new Optional<Room>(new Room(2,"test", "Belarus"))
        def roomRepo = Mock(RoomRepository.class)
        RoomSevice roomService = new RoomSevice(roomRepo)
        roomRepo.findById(2) >> room
        when:
        def newRoom = roomService.findById(2)
        then:
        newRoom == room
    }

    def "when get not exist room return exeption"(){
        given:
        def roomRepo = Mock(RoomRepository.class)
        RoomSevice roomService = new RoomSevice(roomRepo)
        roomRepo.findById(1) >> Optional.empty()
        when:
        roomService.findById(1)
        then:
        thrown(ResponseStatusException)
    }


}
