package org.example.tentrilliondollars.address.controller;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.address.dto.AddressRequestDto;
import org.example.tentrilliondollars.address.dto.AddressResponseDto;
import org.example.tentrilliondollars.address.service.AddressService;
import org.example.tentrilliondollars.global.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<String> createAddress(
            @RequestBody AddressRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        addressService.createAddress(requestDto, userDetails.getUser());

        return ResponseEntity.ok("주소 생성 완료");
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDto>> getUserAllAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AddressResponseDto> addressList = addressService.getUserAllAddress(userDetails.getUser());

        return ResponseEntity.ok(addressList);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<String> updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        addressService.updateAddress(addressId, requestDto, userDetails.getUser());

        return ResponseEntity.ok("주소 수정 완료");
    }
}
