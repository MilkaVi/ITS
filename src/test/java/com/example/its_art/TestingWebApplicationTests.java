package com.example.its_art;

import com.example.its_art.controllers.MainController;
import com.example.its_art.entity.Room;
import com.example.its_art.servise.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestingWebApplicationTests {

    @Autowired
    MainController mainController;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void resetDb() {
        roomRepository.deleteAll();
    }

    private Room createRoom(String name, String country){
        Room room = new Room(name, country);
        return roomRepository.save(room);
    }


    @Test
    public void contextLoads() {
        assertThat(mainController).isNotNull();
    }


    @Test
    public void givenRooms_whenGetRooms_thenStatus200() throws Exception {
        Room room1 = createRoom("test room", "BELARUS");
        Room room2 =createRoom("test room2", "RUSSIA");
        mockMvc.perform(
                get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test room")))
                .andExpect(content().string(containsString("BELARUS")))
                .andExpect(content().string(containsString("test room2")))
                .andExpect(content().string(containsString("RUSSIA")));

    }


    @Test
    public void givenId_whenGetExistingRoom_thenStatus200andRoomReturned() throws Exception {
        long id = createRoom("test room", "BELARUS").getId();
        mockMvc.perform(
                get("/rooms/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test room")))
                .andExpect(content().string(containsString("BELARUS")));
    }

    @Test
    public void givenId_whenGetNotExistingRoom_thenStatus404anExceptionThrown() throws Exception {

        mockMvc.perform(
                get("/rooms/1"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ResponseStatusException.class));

    }





//
//    @Test
//    public void givenId_whenGetExistingRoom_thenStatus200andRoomReturned_thenTurnLight() throws Exception {
//
//
//    }


}