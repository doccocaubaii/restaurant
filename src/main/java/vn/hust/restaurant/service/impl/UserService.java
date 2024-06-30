package vn.hust.restaurant.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hust.restaurant.config.Constants;
import vn.hust.restaurant.constants.ResultConstants;
import vn.hust.restaurant.constants.UserConstants;
import vn.hust.restaurant.domain.Otp;
import vn.hust.restaurant.domain.User;
import vn.hust.restaurant.repository.CompanyRepository;
import vn.hust.restaurant.repository.OtpRepository;
import vn.hust.restaurant.repository.UserRepository;
import vn.hust.restaurant.security.SecurityUtils;
import vn.hust.restaurant.security.UserNameNotFoundExceptionCustom;
import vn.hust.restaurant.service.dto.ChangePasswordDTO;
import vn.hust.restaurant.service.dto.ResultDTO;
import vn.hust.restaurant.service.dto.StaffDTO;
import vn.hust.restaurant.service.dto.StaffResponse;
import vn.hust.restaurant.service.dto.authorities.AuthenticationDTO;
import vn.hust.restaurant.service.dto.company.CompanyResult;
import vn.hust.restaurant.web.rest.errors.CustomException;
import vn.hust.restaurant.web.rest.errors.ExceptionConstants;
import vn.hust.restaurant.web.rest.errors.InternalServerException;
import vn.hust.restaurant.web.rest.vm.LoginVM;

import java.util.*;
import java.util.stream.Collectors;

import static vn.hust.restaurant.constants.ResultConstants.SUCCESS;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final OtpRepository otpRepository;

    public UserService(UserRepository userRepository, CompanyRepository companyRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, OtpRepository otpRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.otpRepository = otpRepository;
    }

    public AuthenticationDTO getAuthoritiesAndInfo(LoginVM loginVM) {
        Optional<User> userOptional = userRepository.findOneByUsername(loginVM.getUsername());
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        Integer comId = loginVM.getCompanyId();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // fake company
            CompanyResult companyResult = new CompanyResult(user.getCompanyId(), user.getFullName());

            List<CompanyResult> companyUsers = new ArrayList<>();
            companyUsers.add(companyResult);
            authenticationDTO.setActivate(false);
            for (CompanyResult com : companyUsers) {
                if (companyUsers.size() == 1 || (com.getId() != null && Objects.equals(com.getId(), comId))) {
                    authenticationDTO.setActivate(true);
                    authenticationDTO.setCompanyName(com.getName());
                    authenticationDTO.setCompanyId(com.getId());
                    break;
                }
            }
            if (loginVM.getCompanyId() != null && authenticationDTO.getCompanyId() == null) {
                throw new UserNameNotFoundExceptionCustom("COMPANY_NOT_FOUND");
            }
            authenticationDTO.setCompanies(companyUsers);
            //            }
            Set<String> authorities = new HashSet<>();
            authenticationDTO.setActivate(true);
            authenticationDTO.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                    loginVM.getUsername(),
                    null,
                    authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                )
            );
            authenticationDTO.setId(user.getId());
            authenticationDTO.setFullName(user.getFullName());
            authenticationDTO.setUsername(user.getUsername());
            return authenticationDTO;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        Optional<User> userOptional = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByUsername);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
//            JwtDTO jwtDTO = getInfoJwt();
            user.setAuthorities(new HashSet<>());
            return user;
        }
        throw new InternalServerException(ExceptionConstants.USERNAME_NOT_NULL_VI, ExceptionConstants.USERNAME_NOT_NULL_CODE);
    }

//    public JwtDTO getInfoJwt() {
//        if (!Strings.isNullOrEmpty(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString())) {
//            String[] chunks = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString().split("\\.");
//            Base64.Decoder decoder = Base64.getUrlDecoder();
//            String payload = new String(decoder.decode(chunks[1]));
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                JwtDTO jwtDTO = mapper.readValue(payload, JwtDTO.class);
//                return jwtDTO;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    public Integer getCompanyId() {
        User user = getUserWithAuthorities();
        return user.getCompanyId();
    }

    public String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    public String genCode(Integer comId, String code) {
        String value = generateRandomString();
        String seqName = getSeqName(comId, code) + value;
        return seqName;
    }

    public String getSeqName(Integer comId, String code) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(code);
        stringBuilder.append("_");
        stringBuilder.append(comId);
        stringBuilder.append("_");
        stringBuilder.append(Constants.SEQ);
        return stringBuilder.toString();
    }

    public ResultDTO createStaff(StaffDTO staffDTO) {
        User admin = getUserWithAuthorities();
        if (!admin.getId().equals(admin.getCompanyId())) throw new CustomException("Bạn không có quyền này");
        User user;
        if (staffDTO.getId() != null) {
            user = userRepository.findById(staffDTO.getId()).orElseThrow(() -> new CustomException("Không tìm thấy ID nhân viên"));
            user.setEmail(staffDTO.getEmail());
            user.setFullName(staffDTO.getFullName());
            user.setUsername(staffDTO.getUsername());
            user.setPhoneNumber(staffDTO.getPhoneNumber());
        } else {
            user = this.modelMapper.map(staffDTO, User.class);
        }
        user.setStatus(2);
        user.setNormalizedName(user.getFullName().concat(user.getUsername()).toLowerCase());
        if (staffDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(staffDTO.getPassword()));
        user.setCompanyId(admin.getCompanyId());
        User savedUser = userRepository.save(user);
        return new ResultDTO(savedUser);
    }

    public ResultDTO searchStaffs(Pageable pageable, String keyword) {
        ResultDTO resultDTO = new ResultDTO();
        User user = getUserWithAuthorities();
        Page<StaffResponse> page = userRepository.searchStaffs(pageable, keyword, user.getCompanyId());
        resultDTO.setMessage(ResultConstants.SUCCESS);
        resultDTO.setReason(ResultConstants.SUCCESS_GET_LIST);
        resultDTO.setStatus(true);
        resultDTO.setData(page.getContent());
        resultDTO.setCount((int) page.getTotalElements());
        return resultDTO;
    }

    public ResultDTO delStaff(Integer id) {
        userRepository.findById(id).ifPresent(userRepository::delete);
        return new ResultDTO(
            SUCCESS,
            "Xóa nhân viên thành công",
            true
        );    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(new User());
    }

    public ResultDTO activeAccount(Integer userId, String otp) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException("Không tìm thây user"));
        if (user.getStatus() != UserConstants.Status.ACTIVATE_EMAIL) throw new CustomException("Trạng thái tài khoản không hợp lệ");
        Otp otp1 = otpRepository.findFirstByUserIdAndAndTypeAndAndStatus(userId, 1, 1).orElseThrow(() -> new CustomException("Không tìm mã hợp lệ"));
        if (otp1.getCode().equals(otp)) {
            user.setStatus(1);
            userRepository.save(user);
            otp1.setStatus(0);
            otpRepository.save(otp1);
            return new ResultDTO("Kích hoạt tài khoản thành công",null,true);
        } else
            return new ResultDTO("Kích hoạt tài khoản thất bại",null,false);

    }

    public ResultDTO changePassword(ChangePasswordDTO request) {
        if (!Objects.equals(request.getNewPassword(), request.getConfirmPassword())) throw new CustomException("Mật khẩu xác nhận không khớp");
        User user = getUserWithAuthorities();
        if (passwordEncoder.matches(request.getOldPassword(),user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        } else return new ResultDTO("Password is incorrect","",false);
        return new ResultDTO("Success","",true);
    }
}
