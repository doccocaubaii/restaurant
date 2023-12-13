package vn.softdreams.easypos.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.Authority;
import vn.softdreams.easypos.domain.Role;
import vn.softdreams.easypos.domain.RolePermission;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.authorities.TreeView;
import vn.softdreams.easypos.dto.permission.PermissionItemResponse;
import vn.softdreams.easypos.repository.AuthorityRepository;
import vn.softdreams.easypos.repository.RolePermissionRepository;
import vn.softdreams.easypos.repository.RoleRepository;
import vn.softdreams.easypos.repository.UserRepository;
import vn.softdreams.easypos.service.RoleManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.service.dto.RolePermissionRequestDTO;

import java.util.*;

@Service
@Transactional
public class RoleManagementServiceImpl implements RoleManagementService {

    private final UserService userService;

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;

    public RoleManagementServiceImpl(
        UserService userService,
        AuthorityRepository authorityRepository,
        UserRepository userRepository,
        RolePermissionRepository rolePermissionRepository,
        RoleRepository roleRepository
    ) {
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Authority saveAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public List<Authority> getAllAuthorityParent(Boolean isParent) {
        if (isParent != null && isParent) {
            return authorityRepository.getAllAuthorityParent();
        }
        return authorityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ArrayList<TreeView> getAllRole(Boolean isAuthorityUser) {
        User user = userService.getUserWithAuthorities();
        // Lấy ra danh sách tất cả các quyền của admin
        List<Authority> authorities = authorityRepository.findAll();
        // Lấy ra tất cả các quyền của user cần phân quyền
        //        Set<Authority> userAuth = user.getAuthorities();
        //        List<String> userAuth = authorityRepository.findAllAuthorityByUserIDAndCompanyID(user.getId(), "123");
        List<String> userAuth = new ArrayList<>();

        Map<String, Integer> map = new HashMap<>();
        for (String auth : userAuth) {
            map.put(auth, 1);
        }
        for (Authority auth : authorities) {
            auth.setCheck(map.keySet().contains(auth.getId()));
        }
        Map<String, TreeView> hashMap = new HashMap<>();
        ArrayList<TreeView> result = new ArrayList<>();
        // Mảng chứa code root
        ArrayList<String> parentsCode = new ArrayList<>();
        // Nếu không có parent code => authority root
        // Ngược lại là authority child
        // Lưu authority dưới dạng tree và put vào map
        for (Authority authority : authorities) {
            // Hiện quyền theo đúng phòng ban
            if (authority.getParentId() == null) {
                TreeView node = new TreeView(authority.getName(), authority.getCode(), false, authority.getCheck(), false);
                hashMap.put(node.getValue(), node);
                parentsCode.add(node.getValue());
            } else {
                TreeView parent = hashMap.get(authority.getParentCode());
                if (!Objects.isNull(parent)) {
                    TreeView node = new TreeView(authority.getName(), authority.getCode(), false, authority.getCheck(), true);
                    parent.addChild(node);
                    hashMap.put(node.getValue(), node);
                }
            }
        }
        // Thêm những node root vào mảng result
        for (String code : parentsCode) {
            result.add(hashMap.get(code));
        }
        return result;
    }

    @Override
    public ResultDTO saveRolePermission(RolePermissionRequestDTO rolePermissionRequestDTO) {
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.getUserWithAuthorities();
        List<Authority> authorities = authorityRepository.findAllByCode(rolePermissionRequestDTO.getRolePermissions());
        Role role = new Role();
        role.setCode(rolePermissionRequestDTO.getCode());
        role.setName(rolePermissionRequestDTO.getName());
        roleRepository.save(role);
        List<RolePermission> rolePermissionList = new ArrayList();
        for (Authority item : authorities) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionCode(item.getCode());
            rolePermission.setPermissionId(item.getId());
            rolePermission.setPermissionParentCode(item.getParentCode());
            rolePermission.setRoleId(role.getId());
            rolePermission.setRoleCode(role.getCode());
            rolePermissionList.add(rolePermission);
        }
        rolePermissionRepository.saveAll(rolePermissionList);

        resultDTO.setStatus(true);
        return resultDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Authority> findOneAuthority(Integer id) {
        return authorityRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Role> findAllRole(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public ResultDTO deleteAuthority(Integer id) {
        ResultDTO resultDTO = new ResultDTO();
        authorityRepository.deleteById(id);
        resultDTO.setStatus(true);
        return resultDTO;
    }

    @Override
    public ResultDTO getAllPermission() {
        List<PermissionItemResponse> responses = roleRepository.getAllPermission();
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, responses, responses.size());
    }

    @Override
    public ResultDTO deleteRole(Integer id) {
        ResultDTO resultDTO = new ResultDTO();
        roleRepository.deleteById(id);
        resultDTO.setStatus(true);
        return resultDTO;
    }
}
