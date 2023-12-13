package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.UserRole;
import vn.softdreams.easypos.repository.RoleRepository;
import vn.softdreams.easypos.repository.UserRoleRepository;
import vn.softdreams.easypos.service.UserRoleService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserRole}.
 */
@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private final Logger log = LoggerFactory.getLogger(UserRoleServiceImpl.class);

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserRole save(UserRole userRole) {
        log.debug("Request to save UserRole : {}", userRole);
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole update(UserRole userRole) {
        log.debug("Request to update UserRole : {}", userRole);
        return userRoleRepository.save(userRole);
    }

    @Override
    public Optional<UserRole> partialUpdate(UserRole userRole) {
        log.debug("Request to partially update UserRole : {}", userRole);

        return userRoleRepository
            .findById(userRole.getId())
            .map(existingUserRole -> {
                if (userRole.getUserId() != null) {
                    existingUserRole.setUserId(userRole.getUserId());
                }
                if (userRole.getRoleId() != null) {
                    existingUserRole.setRoleId(userRole.getRoleId());
                }
                if (userRole.getComId() != null) {
                    existingUserRole.setComId(userRole.getComId());
                }
                return existingUserRole;
            })
            .map(userRoleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserRole> findAll(Pageable pageable) {
        log.debug("Request to get all UserRoles");
        return userRoleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRole> findOne(Integer id) {
        log.debug("Request to get UserRole : {}", id);
        return userRoleRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete UserRole : {}", id);
        userRoleRepository.deleteById(id);
    }
}
