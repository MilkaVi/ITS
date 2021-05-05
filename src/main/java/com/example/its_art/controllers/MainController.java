package com.example.its_art.controllers;

import com.example.its_art.config.Countres;
import com.example.its_art.entity.Room;
import com.example.its_art.servise.RoomRepository;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/rooms")
public class MainController {


    @Autowired
    RoomRepository roomRepository;

    @GetMapping()
    public String getAllRooms(Model model, @RequestParam(value = "messege", defaultValue = "") String messege) {
        model.addAttribute("messege", messege);
        model.addAttribute("rooms", roomRepository.findAll());
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
        roomRepository.save(room);
        return "redirect:/rooms";
    }


    @GetMapping("{id}")
    public String update(@PathVariable("id") int id,
                         Model model) throws IOException {
        if (!roomRepository.findById(id).isPresent())
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");


        if (countryCheck(id)) {
            model.addAttribute("room", roomRepository.findById(id));
            return "room";
        }
        return "redirect:/rooms?messege=error";
    }


    @MessageMapping("/turning")
    @SendTo("/turning")
    public Room getMessages(Integer id) {
        Room room = roomRepository.findById(id).get();
        room.turn();
        roomRepository.setFixedFirstnameFor(room.isState(), id);
        return room;
    }


    @PostMapping("/rate")
    public String rateHandler() {
        return "redirect:/rooms";
    }


    public boolean countryCheck(Integer id) throws IOException {

        File dbFile = new File("GeoLite2-Country.mmdb");
        DatabaseReader reader = new DatabaseReader.Builder(dbFile).build();
        InetAddress ipAddress = null;
        CountryResponse response = null;
        try {
            ipAddress = InetAddress.getByName(fetchClientIpAddr());
            response = reader.country(ipAddress);
        } catch (Exception e) {
            return true;
        }
        Country country = response.getCountry();
        return country.getName().equalsIgnoreCase(roomRepository.findById(id).get().getCountry());


    }


    protected String fetchClientIpAddr() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        Assert.isTrue(ip.chars().filter($ -> $ == '.').count() == 3, "Illegal IP: " + ip);
        return ip;
    }


}
