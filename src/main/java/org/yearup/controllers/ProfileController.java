package org.yearup.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {

    public ProfileService profileService;
    private UserService userService;

    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    //Returns the logged-in user info
    public Profile getProfile(Principal principal) {

        //Used the Principle object to turn the userService into a userId so I could get the right profile.
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        return profileService.getById(userId);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    //The user can change their personal information.
    public Profile updateProfile(Principal principal, @RequestBody Profile profile) {

        //Takes the new data from the @RequestBody and passes it to the service to be saved.
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        return profileService.update(userId, profile);
    }
}
