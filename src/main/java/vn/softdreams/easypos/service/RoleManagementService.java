package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.Authority;
import vn.softdreams.easypos.domain.Role;
import vn.softdreams.easypos.dto.authorities.TreeView;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.service.dto.RolePermissionRequestDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface RoleManagementService {
    Authority saveAuthority(Authority authority);

    List<Authority> getAllAuthorityParent(Boolean isParent);

    ArrayList<TreeView> getAllRole(Boolean isAuthorityUser);

    ResultDTO saveRolePermission(RolePermissionRequestDTO rolePermissionRequestDTO);

    Optional<Authority> findOneAuthority(Integer id);

    Page<Role> findAllRole(Pageable pageable);

    ResultDTO deleteRole(Integer id);
    ResultDTO deleteAuthority(Integer id);
    ResultDTO getAllPermission();
}
