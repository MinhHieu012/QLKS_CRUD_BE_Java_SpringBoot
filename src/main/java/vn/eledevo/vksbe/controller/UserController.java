package vn.eledevo.vksbe.controller;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import vn.eledevo.vksbe.dto.request.user.UserAddRequest;
import vn.eledevo.vksbe.dto.request.user.UserUpdateRequest;
import vn.eledevo.vksbe.dto.response.ApiResponse;
import vn.eledevo.vksbe.dto.response.UserResponse;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.service.user.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/quanlyuser")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUser() {
        return new ApiResponse<>(200, "Get all user success!", userService.getAllUser());
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse createUser(@RequestBody @Valid UserAddRequest userRequest) throws ValidationException {
        return new ApiResponse<>(201, "Create user success!", userService.createUser(userRequest));
    }

    @PutMapping("/update/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse updateUser(@PathVariable UUID uuid, @RequestBody @Valid UserUpdateRequest userRequest) throws ValidationException {
        return new ApiResponse<>(204, "Update user success!", userService.updateUser(uuid, userRequest));
    }

    @PatchMapping("/lock/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse lockUser(@PathVariable UUID uuid) throws ValidationException {
        return new ApiResponse<>(200, "Lock user success!", userService.lockUser(uuid));
    }

    @PatchMapping("/unlock/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse unLockUser(@PathVariable UUID uuid) throws ValidationException {
        return new ApiResponse<>(200, "Unlock user success!", userService.unLockUser(uuid));
    }
}
