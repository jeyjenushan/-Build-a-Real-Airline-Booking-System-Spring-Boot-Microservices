package com.jenu.mapper;

import com.jenu.model.City;
import com.jenu.payload.request.CityRequest;
import com.jenu.payload.response.CityResponse;

public class CityMapper {

    public static City ConvertToCity(CityRequest cityRequest) {
        if(cityRequest == null) return null;
        return City.builder()
                .name(cityRequest.getName())
                .cityCode(cityRequest.getCityCode())
                .countryCode(cityRequest.getCountryCode())
                .countryName(cityRequest.getCountryName())
                .regionCode(cityRequest.getRegionCode())
                .timeZoneId(cityRequest.getTimeZoneOffset())
                .build();
    }

    public static CityResponse ConvertToCityResponse(City city) {
        if(city == null) return null;
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .cityCode(city.getCityCode())
                .countryCode(city.getCountryCode())
                .countryName(city.getCountryName())
                .regionCode(city.getRegionCode())
                .timeZoneOffset(city.getTimeZoneId())
                .build();
    }

    public static City updateCity(City city,CityRequest cityRequest){
        if(cityRequest.getName()!=null){
            cityRequest.setName(cityRequest.getName().trim());
        }
        if(cityRequest.getCityCode()!=null){
            cityRequest.setCityCode(cityRequest.getCityCode().toUpperCase().trim());
        }
        if(cityRequest.getCountryCode()!=null){
            cityRequest.setCountryCode(cityRequest.getCountryCode().toUpperCase().trim());
        }
        if(cityRequest.getCountryName()!=null){
            cityRequest.setCountryName(cityRequest.getCountryName().trim());
        }
        if(cityRequest.getRegionCode()!=null){
            cityRequest.setRegionCode(cityRequest.getRegionCode().toUpperCase().trim());
        }
        return city;
    }


}

