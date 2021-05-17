package com.example.its_art.controllers;

import com.example.its_art.config.Countres;
import com.example.its_art.entity.Room;
import com.example.its_art.repository.RoomRepository;
import com.example.its_art.servise.RoomSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/rooms")
public class MainController {

    @Autowired
    RoomSevice roomSevice;

    @GetMapping()
    public String getAllRooms(Model model, @RequestParam(value = "messege", defaultValue = "") String messege) {
        model.addAttribute("messege", messege);
        model.addAttribute("rooms", roomSevice.findAll());
        return "rooms";
    }

    @GetMapping("new")
    public String addNewRoomPage(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("countres", Countres.countres);
        return "addNewRoom";
    }

    @PostMapping("")
    public String addNewRoom(
            @ModelAttribute Room room) {
        roomSevice.save(room);
        return "redirect:/rooms";
    }

    @GetMapping("{id}")
    public String update(@PathVariable("id") int id,
                         Model model) throws IOException {

        if (roomSevice.countryCheck(id)) {
            model.addAttribute("room", roomSevice.findById(id));
            return "room";
        }
        return "redirect:/rooms?messege=error";
    }


    @MessageMapping("/turning")
    @SendTo("/turning")
    public Room getMessages(Integer id) {
        Room room = roomSevice.findById(id).get();
        room.turn();
        roomSevice.setFixedFirstnameFor(room.isState(), id);
        return room;
    }


    @PostMapping("/rate")
    public String rateHandler() {
        return "redirect:/rooms";
    }


}
