package com.TradeHub.model.mapper.impl;

import com.TradeHub.model.dto.CustomUserDTO;
import com.TradeHub.model.dto.ProfileDTO;
import com.TradeHub.model.dto.UserDTO;
import com.TradeHub.model.dto.UserProfileDTO;
import com.TradeHub.model.entity.User;
import com.TradeHub.model.mapper.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {
    private ModelMapper mapper;

    public UserMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDTO userToUserDTO(User user){
        return mapper.map(user,UserDTO.class);
    }



    @Override
    public CustomUserDTO userToCustomUserDTO(User user){
        return mapper.map(user,CustomUserDTO.class);
    }

    @Override
    public ProfileDTO userToProfileDTO(User user) {
        return mapper.map(user,ProfileDTO.class);
    }

    @Override
    public UserProfileDTO userToUserProfileDTO(User user) {
        return mapper.map(user,UserProfileDTO.class);
    }


}
