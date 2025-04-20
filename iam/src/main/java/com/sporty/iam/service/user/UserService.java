package com.sporty.iam.service.user;

import com.sporty.iam.domain.entity.user.User;
import com.sporty.iam.domain.model.common.exception.ErrorCode;
import com.sporty.iam.domain.model.common.exception.RecordConflictException;
import com.sporty.iam.domain.model.common.exception.RecordNotFoundException;
import com.sporty.iam.user.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 2/18/25
 * Time: 7:58 PM
 */
@Service
@Log4j2
public class UserService {

    private final UserRepository repository;

    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public User getById(final UUID id) {
        log.info("Retrieving user with id - {} ", id);
        User result = repository.findById(id).orElseThrow(() -> new RecordNotFoundException(
                String.format("User with id - %s not exists", id)));
        log.info("Successfully retrieved user with id - {}, result - {}", id, result);
        return result;
    }

    @Transactional(readOnly = true)
    public int getLoyaltyPoints(final UUID id) {
        log.info("Retrieving user loyalty points with id - {} ", id);
        User result = getById(id);
        int loyaltyPoints = result.getLoyaltyPoints();
        log.info("Successfully retrieved user loyalty points with id - {}, result - {}", id, loyaltyPoints);
        return loyaltyPoints;
    }

    @Transactional(readOnly = true)
    public User getByUserName(final String userName) {
        log.info("Retrieving user with user name - {} ", userName);
        User result = repository.findByUsername(userName)
                .orElseThrow(() -> new RecordConflictException(
                        String.format("User with name - %s not exists", userName),
                        ErrorCode.NOT_EXISTS_EXCEPTION));
        log.info("Successfully retrieved user with user name - {}, result - {}", userName, result);
        return result;
    }

    @Transactional
    public User updateLoyaltyPoints(final UUID id, final int loyaltyPoints) {
        log.info("Updating user with id - {} ", id);
        User user = getById(id);
        user.setLoyaltyPoints(loyaltyPoints);
        User result = repository.save(user);
        log.info("Successfully updated user with id - {}, result - {}", id, result);
        return result;
    }
}
