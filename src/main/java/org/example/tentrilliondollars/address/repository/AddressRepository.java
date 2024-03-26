package org.example.tentrilliondollars.address.repository;

import org.example.tentrilliondollars.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
