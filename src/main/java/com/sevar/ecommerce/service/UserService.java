package com.sevar.ecommerce.service;

import com.sevar.ecommerce.exception.UserException;
import com.sevar.ecommerce.model.Users;

import java.util.List;

public interface UserService {

    public Users findUserByUserId(Long userId) throws UserException;

    public Users findUserProfileByJwt(String jwt) throws UserException;

    List<Users> getAllUsers();
}
