package com.sevar.ecommerce.serviceImpl;

import com.sevar.ecommerce.exception.UserException;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.repository.UserRepository;
import com.sevar.ecommerce.security.jwt.JwtProvider;
import com.sevar.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public Users findUserByUserId(Long userId) throws UserException {
        Optional<Users> users = userRepository.findById(userId);
        if (users.isPresent()){
            return users.get();
        }
        throw new UserException("User Not Found");
    }

    @Override
    public Users findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        Users user = userRepository.findByEmail(email);
        if (user == null){
            throw new UserException("User Not Found With : "+email);
        }
        return user;
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
