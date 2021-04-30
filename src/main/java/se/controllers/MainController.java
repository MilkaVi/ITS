package se.controllers;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import se.Countres;
import se.entity.Room;
import se.servise.RoomRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/rooms")
public class MainController {


    @Autowired
    RoomRepository roomRepository;

    @GetMapping()
    public String getAllRooms(Model model) {
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
            @ModelAttribute Room room, Model model) {
        roomRepository.save(room);
        return "redirect:/rooms";
    }


    @GetMapping("{id}")
    public String update(@PathVariable("id") int id, HttpServletRequest request,
                         Model model) throws IOException, GeoIp2Exception {
        if (countryCheck( id)) {
            model.addAttribute("room", roomRepository.findById(id));
            return "room";
        }
        return "403";
    }


    @MessageMapping("/turning")
    @SendTo("/turning")
    public Room getMessages(Integer id) {
        Room room = roomRepository.findById(id).get();
        System.out.println(room.getName() + " " + room.isState());
        room.turn();
        System.out.println(room.getName() + " " + room.isState());
        roomRepository.setFixedFirstnameFor(room.isState(), id);
        return room;
    }








    @PostMapping("/rate")
    public String rateHandler() {
        return "redirect:/rooms";
    }




    public boolean countryCheck(Integer id) throws IOException, GeoIp2Exception {
        if (fetchClientIpAddr().equals("127.0.0.1"))
            return true;

        File dbFile = new File("GeoLite2-Country.mmdb");
        DatabaseReader reader = new DatabaseReader.Builder(dbFile).build();
        InetAddress ipAddress = InetAddress.getByName("37.45.115.113");
        CountryResponse response = reader.country(ipAddress);
        Country country = response.getCountry();
        System.out.println("Country Name: " + country.getName());

        return country.getName().equals(roomRepository.findById(id).get().getCountry());


    }


    protected String fetchClientIpAddr() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        Assert.isTrue(ip.chars().filter($ -> $ == '.').count() == 3, "Illegal IP: " + ip);
        return ip;
    }


}
