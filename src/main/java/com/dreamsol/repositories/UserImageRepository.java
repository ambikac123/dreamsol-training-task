package com.dreamsol.repositories;

import com.dreamsol.entities.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage,Long>
{
    UserImage findByDuplicateImageName(String imageName);
}
