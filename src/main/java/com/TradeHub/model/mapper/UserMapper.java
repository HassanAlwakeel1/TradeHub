package com.TradeHub.model.mapper;


import com.TradeHub.model.dto.CustomUserDTO;
import com.TradeHub.model.dto.ProfileDTO;
import com.TradeHub.model.dto.UserDTO;
import com.TradeHub.model.dto.UserProfileDTO;
import com.TradeHub.model.entity.User;

public interface UserMapper {

    public UserDTO userToUserDTO(User user);

    public ProfileDTO userToProfileDTO(User user);

    public CustomUserDTO userToCustomUserDTO(User user);

    public UserProfileDTO userToUserProfileDTO(User user);
}

