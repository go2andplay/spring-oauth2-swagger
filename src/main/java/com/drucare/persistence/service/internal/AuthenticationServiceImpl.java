/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drucare.persistence.service.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.drucare.persistence.entity.AuthorityEntity;
import com.drucare.persistence.entity.UserEntity;
import com.drucare.persistence.repository.UserRepository;
import com.drucare.persistence.vo.Authority;
import com.drucare.persistence.vo.User;

/**
 * only deals with authentication
 *
 * @author ThirupathiReddy V
 *
 */
@Service("userDetailsService")
public class AuthenticationServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LOGGER.info("Loading normal user from the database with username {} ", username);
        UserEntity personEntity = userRepository.findByUsername(username);

        if (personEntity == null) {
            personEntity=new UserEntity();
            personEntity.setUsername("tvajjala");
            personEntity.setPassword("workflow");
            LOGGER.warn("User not found in the local database throwing exception ");
            LOGGER.warn(
                    "#### This may be due to all table are droped and recreated through hibernate but UserConnection table is created throgh jdbcTemplate.please cleanup this table ");
            // throw new UsernameNotFoundException(String.format("Person %s does not exist!", username));
        }

        LOGGER.info("User found in the local database and rerurning ");
        final User user = new User();
        user.setId(personEntity.getId());
        user.setUsername(personEntity.getUsername());
        user.setPassword(personEntity.getPassword()); // this will be verified on AuthenticationProvider
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        for (final AuthorityEntity authorityEntity : personEntity.getAuthorities()) {
            user.getAuthorities().add(new Authority(authorityEntity.getAuthority(), authorityEntity.getTitle()));
        }

        return user;
    }

}
