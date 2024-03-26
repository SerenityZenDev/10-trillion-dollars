package org.example.tentrilliondollars.address.service;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.address.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

}
