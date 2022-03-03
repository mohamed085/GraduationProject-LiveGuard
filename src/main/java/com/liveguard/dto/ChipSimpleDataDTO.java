package com.liveguard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChipSimpleDataDTO {
    private Long id;
    private String name;
    private String password;
    private Boolean used;
    private String photo;
}
