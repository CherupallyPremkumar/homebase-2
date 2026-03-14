package com.homebase.ecom.user.client;

import com.homebase.ecom.user.dto.UserDto;
import com.homebase.ecom.user.dto.AddressDto;
import com.homebase.ecom.user.dto.PreferencesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "user-service", url = "${homebase.client.user.url:http://user.user.svc.cluster.local:8086}")
public interface UserClient {

    @GetMapping("/api/users/me")
    UserDto getMyProfile(@RequestHeader("Authorization") String token);

    @PatchMapping("/api/users/me")
    UserDto updateProfile(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> updates);

    @GetMapping("/api/users/me/addresses")
    List<AddressDto> getAddresses(@RequestHeader("Authorization") String token);

    @PostMapping("/api/users/me/addresses")
    AddressDto addAddress(@RequestHeader("Authorization") String token, @RequestBody AddressDto addressDto);

    @DeleteMapping("/api/users/me/addresses/{addressId}")
    void removeAddress(@RequestHeader("Authorization") String token, @PathVariable("addressId") String addressId);

    @PatchMapping("/api/users/me/addresses/{addressId}/default")
    AddressDto setDefaultAddress(@RequestHeader("Authorization") String token, @PathVariable("addressId") String addressId);

    @GetMapping("/api/users/me/preferences")
    PreferencesDto getPreferences(@RequestHeader("Authorization") String token);

    @PutMapping("/api/users/me/preferences")
    PreferencesDto updatePreferences(@RequestHeader("Authorization") String token, @RequestBody PreferencesDto preferencesDto);
}
