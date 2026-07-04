package com.charlie.hirehub.userservice.user.dto.response;

import com.charlie.hirehub.userservice.user.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private Role role;
}
