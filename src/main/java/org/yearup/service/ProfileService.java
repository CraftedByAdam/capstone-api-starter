package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

@Service
public class ProfileService
{
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository)
    {
        this.profileRepository = profileRepository;
    }

    public Profile create(Profile profile)
    {
        return profileRepository.save(profile);
    }

    //Looks up a profile in the repo using the userId.
    public Profile getById(int userId)
    {                            //added orElse(null) so the controller can handle it if not found.
        return profileRepository.findById(userId).orElse(null);
    }

    public Profile update(int userId, Profile profile)
    {
        //Finds the original first
        Profile existing = profileRepository.findById(userId).orElseThrow();
        //Overwrites each field with the new information before saving back to the database.
        existing.setFirstName(profile.getFirstName());
        existing.setLastName(profile.getLastName());
        existing.setPhone(profile.getPhone());
        existing.setEmail(profile.getEmail());
        existing.setAddress(profile.getAddress());
        existing.setCity(profile.getCity());
        existing.setState(profile.getState());
        existing.setZip(profile.getZip());

        return profileRepository.save(existing);
    }
}
