package org.example.tentrilliondollars.address.service;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.address.dto.AddressRequestDto;
import org.example.tentrilliondollars.address.entity.Address;
import org.example.tentrilliondollars.address.repository.AddressRepository;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public void createAddress(AddressRequestDto requestDto, User user) {
        User finduser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Address address = new Address(requestDto, finduser);

        addressRepository.save(address);
    }

}
