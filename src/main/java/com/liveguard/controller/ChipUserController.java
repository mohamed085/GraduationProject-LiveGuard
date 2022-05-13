package com.liveguard.controller;

import com.liveguard.domain.ChipUser;
import com.liveguard.dto.ChipUserDTO;
import com.liveguard.dto.UserInChipDTO;
import com.liveguard.mapper.ChipUserMapper;
import com.liveguard.payload.AddNewChipRequest;
import com.liveguard.payload.ApiResponse;
import com.liveguard.service.ChipUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chip-user")
public class ChipUserController {

    private final ChipUserService chipUserService;

    public ChipUserController(ChipUserService chipUserService) {
        this.chipUserService = chipUserService;
    }

    @PostMapping("")
    public ResponseEntity<?> addNewChipToUser(@RequestBody AddNewChipRequest addNewChipRequest) {
        log.debug("ChipUserController | addNewChipToUser");

        chipUserService.add(addNewChipRequest);
        return ResponseEntity
                .ok()
                .body(new ApiResponse(true, "Chip add successfully"));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllMyChips() {
        log.debug("ChipUserController | getAllMyChips");
        List<ChipUserDTO> chipUserDTOs = new ArrayList<>();

        chipUserService.findAllByUser().forEach(chip -> {
            chipUserDTOs.add(ChipUserMapper.chipUserTOChipUserDTO(chip));
        });

        return ResponseEntity
                .ok()
                .body(chipUserDTOs);
    }

    @RequestMapping(value = "/info",  method = RequestMethod.PUT, consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateMyChipInfo(@ModelAttribute ChipUserDTO chipUserDTO) {
        log.debug("ChipUserController | updateMyChipInfo | chipUserDTO: " + chipUserDTO.getName());

        chipUserService.updateMyChipInfo(chipUserDTO);
        return ResponseEntity
                .ok()
                .body(new ApiResponse(true, "Update chip information successfully"));
    }

    @PutMapping("/details")
    public ResponseEntity<?> updateMyChipDetails(@RequestBody ChipUserDTO chipUserDTO) {
        log.debug("ChipUserController | updateMyChipInfo | chipUserDTO: " + chipUserDTO.getDetails());

        chipUserService.updateMyChipDetails(chipUserDTO);
        return ResponseEntity
                .ok()
                .body(new ApiResponse(true, "Update chip details successfully"));
    }

    @GetMapping("/chip/{id}")
    public ResponseEntity<?> findAllUsersInChip(@PathVariable("id") Long id) {
        log.debug("ChipUserController | findAllUsersInChip | id: " + id);

        List<UserInChipDTO> users = new ArrayList<>();

        chipUserService.findAllUsersInChip(id).forEach(chipUser -> {
            users.add(new UserInChipDTO(chipUser.getUser().getId(), chipUser.getUser().getEmail(),
                    chipUser.getUser().getName(), chipUser.getUser().getAbout(), chipUser.getUser().getAvatar(),
                    chipUser.getChipUserType()));
        });

        return ResponseEntity
                .ok()
                .body(users);
    }

}
