package se;

import com.github.sypexgeo.SxRestClient;
import com.github.sypexgeo.model.SxGeoResult;

import java.util.Locale;

public class PO {
    public static void main(String[] args) {
        String[] countryCodes = Locale.getISOCountries();

        for (String countryCode : countryCodes) {

            Locale obj = new Locale("", countryCode);
            System.out.println("Country Code = " + obj.getCountry()
                    + ", Country Name = " + obj.getDisplayCountry());

        }
    }
}
