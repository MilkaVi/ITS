package se.controllers;

import com.github.sypexgeo.SxRestClient;
import com.github.sypexgeo.model.SxGeoResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import se.Countres;
import se.entity.Room;
import se.servise.RoomRepository;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
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
    public String update(@PathVariable("id") int id,HttpServletRequest request,
                         Model model) {
        if (countryCheck(request, id)) {
            model.addAttribute("room", roomRepository.findById(id));
            return "room";
        }
        return "403";
    }

    @PostMapping("/switch")
    public String switchLight(Locale locale,Integer id, Model model
                              ) {
        Room room = roomRepository.findById(id).get();
        System.out.println(room.getName() + " " + room.isState() );
        room.turn();
        System.out.println(room.getName() + " " + room.isState() );
        roomRepository.setFixedFirstnameFor(room.isState(), id);
        model.addAttribute("room", room);
        return "turn";

    }


    @PostMapping("/rate")
    public String rateHandler() {
        return "redirect:/rooms";
    }


    @GetMapping("/h")
    public String sayHello(HttpServletRequest request, Model model) {
//        System.out.println(request.getRemoteAddr());
//        SxGeoResult result = SxRestClient.create("YZ882").get("37.45.115.113");
//        System.out.println(result.city != null ? result.country.name.ru() : null);
//        model.addAttribute("ligth", "on");
//        return "hello_world";
        return "lang";
    }


    public boolean countryCheck(HttpServletRequest request, Integer id){
        System.out.println("1 "+request.getRemoteAddr());
        System.out.println("2 " +fetchClientIpAddr());
        SxGeoResult result = SxRestClient.create("YZ882").get(fetchClientIpAddr());
        System.out.println(result.city != null ? result.country.name.ru() : null);
        if(result.city == null)
            return false;
        return result.country.name.ru().equals(roomRepository.findById(id).get().getCountry());
    }


    protected String fetchClientIpAddr() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        Assert.isTrue(ip.chars().filter($ -> $ == '.').count() == 3, "Illegal IP: " + ip);
        return ip;
    }



}
