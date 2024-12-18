package com.sevar.ecommerce.service;

import com.sevar.ecommerce.model.Address;
import com.sevar.ecommerce.model.Users;

public interface AddressService {
    boolean isAddressAlreadyPresent(Address newAddress, Users user);
}
