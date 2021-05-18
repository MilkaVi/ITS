package com.example.its_art.servise;

import com.example.its_art.entity.Room;
import com.example.its_art.repository.RoomRepository;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class RoomSevice {

    @Autowired
    RoomRepository roomRepository;

    public RoomSevice(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public <S extends Room> S save(S s) {
        return roomRepository.save(s);
    }


    public Optional<Room> findById(Integer integer) {
        if (!roomRepository.findById(integer).isPresent())
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        return roomRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return roomRepository.existsById(integer);
    }

    public Iterable<Room> findAll() {
        return roomRepository.findAll();
    }

    public Iterable<Room> findAllById(Iterable<Integer> iterable) {
        return roomRepository.findAllById(iterable);
    }

    public long count() {
        return roomRepository.count();
    }

    public void deleteById(Integer integer) {
        roomRepository.deleteById(integer);
    }

    public void delete(Room room) {
        roomRepository.delete(room);
    }

    public void deleteAll(Iterable<? extends Room> iterable) {
        roomRepository.deleteAll(iterable);
    }
    public void setFixedFirstnameFor(Boolean state, Integer id){
        roomRepository.setFixedFirstnameFor(state, id);
    }

    public void deleteAll() {
        roomRepository.deleteAll();
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
        return country.getName().equalsIgnoreCase(findById(id).get().getCountry());
    }


    protected String fetchClientIpAddr() {
        HttpServletRequest request = ((ServletRequestAttributes)
                (Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))).getRequest();
        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        Assert.isTrue(ip.chars().filter($ -> $ == '.').count() == 3, "Illegal IP: " + ip);
        return ip;
    }





}
