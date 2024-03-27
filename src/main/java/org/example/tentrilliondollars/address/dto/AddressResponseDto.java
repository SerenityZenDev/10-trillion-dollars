package org.example.tentrilliondollars.address.dto;

import lombok.Getter;
import org.example.tentrilliondollars.address.entity.Address;

@Getter
public class AddressResponseDto {
    private String city;
    private String village;
    private String province;

    public AddressResponseDto(Address address) {
    }
}
