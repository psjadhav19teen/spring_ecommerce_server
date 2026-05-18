package com.example.ecommerce.dto;

import com.example.ecommerce.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {

    private Role role;
}
