package org.example.tentrilliondollars.address.controller;


import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.address.service.AddressService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

}
