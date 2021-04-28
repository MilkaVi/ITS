package se.controllers;

import com.github.sypexgeo.SxRestClient;
import com.github.sypexgeo.model.SxGeoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.entity.Room;
import se.servise.RoomRepository;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

@Controller
public class MainController {
//    @Autowired
//    RoomRepository roomRepository;
//
//    @GetMapping("/rooms")
//    public String getAllRooms(Model model) {
//        model.addAttribute("rooms", roomRepository.findAll());
//        return "rooms";
//    }
//
//
//    @GetMapping("/rooms/new")
//    public String addNewRoom(Model model) {
//        model.addAttribute("room", new Room());
//        return "addNewRoom";
//    }






    @GetMapping("")
    public String sayHello(HttpServletRequest request, Model model) {
        System.out.println(getLocalIpAddress());
        SxGeoResult result = SxRestClient.create("YZ882").get(getLocalIpAddress());
        System.out.println(result.city != null ? result.city.name.ru() : null);
        model.addAttribute("ligth", "on");
        return "hello_world";
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }




    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String home(Locale locale, Model model, String username, String password) {


        if(username.equalsIgnoreCase("david"))
        {
            model.addAttribute("validUser", "Welcome " + username );

            return "home";
        }
        else
        {
            model.addAttribute("validUser", "Incorrect username and password");
            return "home";
        }

    }



}
