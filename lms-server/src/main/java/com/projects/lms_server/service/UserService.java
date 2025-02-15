package com.projects.lms_server.service;

import com.projects.lms_server.dto.*;
import com.projects.lms_server.entites.MemberRegistrationEntity;
import com.projects.lms_server.entites.UserEntity;
import com.projects.lms_server.entites.enums.Roles;
import com.projects.lms_server.exceptions.ResourceAlreadyExistsException;
import com.projects.lms_server.exceptions.ResourceNotFoundException;
import com.projects.lms_server.repository.MemberRegistrationRepository;
import com.projects.lms_server.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.projects.lms_server.utils.GeneratePassword.generatePassword;

@Service
@Slf4j

public class UserService implements UserDetailsService {

    /* Injecting dependencies: UserRepository, MemberRegistrationRepository, ModelMapper, PasswordEncoder, and EmailService */
    private final MemberRegistrationRepository memberRegistrationRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JWTService jwtService;

    public UserService(MemberRegistrationRepository memberRegistrationRepository, UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, EmailService emailService, JWTService jwtService) {
        this.memberRegistrationRepository = memberRegistrationRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    /* Converting the UserDTO to MemberRegistrationEntity */
    private MemberRegistrationEntity convertToMemberRegistrationEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, MemberRegistrationEntity.class);
    }

    /* Converting the MemberRegistrationEntity into UserDTO */
    private UserDTO convertToUserDTO(MemberRegistrationEntity user) {
        return modelMapper.map(user, UserDTO.class);
    }

    /* Converting memberRegistrationEntity into the User Entity */
    private UserEntity convertToUserEntity(MemberRegistrationEntity memberRegistration) {
        return modelMapper.map(memberRegistration,UserEntity.class);
    }

    /* Converting the UserEntity into the UserDTO */
    private UserDTO converToUserDtO(UserEntity user) {
        return modelMapper.map(user,UserDTO.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("user not Found with given email:"+username));

    }

    /* User Sign-Up Method */
    @Transactional
    public UserDTO signUp(@Valid UserDTO user) {
        String email = user.getEmail();

        /* Checking if the user already exists in the system */
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Email already exists! Please login.");
        }

        /* Checking if the user has already requested to join the library */
        Optional<MemberRegistrationEntity> memOptional = memberRegistrationRepository.findByEmail(email);
        if (memOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("A request is already present!,Please wait for admin confirmation.");
        }

        /* Mapping UserDTO to MemberRegistrationEntity and saving the registration */
        MemberRegistrationEntity toBeCreatedUser = convertToMemberRegistrationEntity(user);
        MemberRegistrationEntity savedUserRequest = memberRegistrationRepository.save(toBeCreatedUser);

        log.info("Member Registration Id is {}", savedUserRequest.getRegistrationId());

        /* Returning the Registration ID to the requested user */
        return convertToUserDTO(savedUserRequest);
    }

    /* Approving User Request to become Member */
    @Transactional
    public UserDTO approveMemberRegistrationRequest(String regId) {
        MemberRegistrationEntity memberRegistration = memberRegistrationRepository.findById(regId).orElseThrow(()->
                new ResourceNotFoundException("Invalid Registration Number :"+ regId));
        UserEntity toBeApprovedUser = convertToUserEntity(memberRegistration);

        /* Setting isActive true and role set as a Member */
        toBeApprovedUser.setRole(Roles.MEMBER);
        toBeApprovedUser.setIsActive(true);
        UserEntity member  = getCurrentUser();
        toBeApprovedUser.setApprovedBy(member.getUserId());
        /* Generating Password and Hashing*/
        String password = generatePassword();
        String hashPassword = passwordEncoder.encode(password);

        /* setting their Credentials of new  User */
        toBeApprovedUser.setPassword(hashPassword);

        /* Saving new Member To DataBase */
        UserEntity newMember = userRepository.save(toBeApprovedUser);
        /* Deleting the Request */
        memberRegistrationRepository.delete(memberRegistration);

        /* Creating Mail to inform the user */
        String to = newMember.getEmail();
        String subject = "Your Library Membership is Approved! 🎉";
        String body = "Dear "+ newMember.getName()+"\n\n" +
                "We are pleased to inform you that your request to become a member of our library has been successfully approved! 🎉\n\n" +
                "Here are your credentials for accessing the library’s resources:\n\n" +
                "- User ID:" + newMember.getUserId() +"\n\n"+
                "- Password:"+ password+" \n\n" +
                "Please ensure that you keep your credentials secure for future access. You can now log in to explore our extensive collection, enjoy a variety of books, and take advantage of our member-exclusive benefits.\n\n" +
                "If you have any questions or need assistance, don’t hesitate to reach out to us.\n\n" +
                "Thank you for choosing Book Hive — where knowledge comes to life!\n\n" +
                "Warm regards,\n" +
                "The Book Hive Team\n" +
                "Library Management";

        /* Sending Request Acceptance Mail to the user*/
        emailService.sendMail(to,subject,body);

        return converToUserDtO(newMember);

    }

    /* Rejecting the member registration requests */
    @Transactional
    public UserDTO rejectMemberRegistrationRequest(String id) {

        /* Fetching User Registration Details */
        MemberRegistrationEntity reqUser = memberRegistrationRepository.findById(id).orElseThrow(()->
                new UsernameNotFoundException("Invalid Registration ID"));
        String to = reqUser.getEmail();
        String subject = "Your Library Membership Application Status";
        String message = "Dear+"+ reqUser.getName()+ "\n\n" +
                "Thank you for your interest in becoming a member of Book Hive. We regret to inform you that your application for membership has not been approved at this time.\n\n" +
                "We understand that this news may be disappointing, and we encourage you to apply again in the future should circumstances change. We appreciate your understanding.\n\n" +
                "If you have any questions or would like feedback regarding your application, please feel free to reach out to us.\n\n" +
                "Thank you once again for your interest in Book Hive . We wish you all the best.\n\n" +
                "Warm regards,\n" +
                "The Book Hive Team\n" +
                "Library Management";

        /* Deleting the User Details */
        memberRegistrationRepository.delete(reqUser);
        /* Sending Mail to the User */
        emailService.sendMail(to,subject,message);

        return  modelMapper.map(reqUser,UserDTO.class);

    }

    /* Get User by their id */
    @Transactional
    public UserEntity getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /* Verifying the credentials and Generate the token  */
    public LoginResDTO login(LoginReqDTO cred) {
        String email = cred.getEmail();

        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        /* if user not found throw the error */
        if (optionalUser.isEmpty())
            throw  new ResourceNotFoundException("User Not Found !,Please signup first");
       UserEntity user = optionalUser.get();
        /*make sure user is active*/
        if ( !user.getIsActive())
                throw new BadCredentialsException("User not Active !,Contact to ADMIN ");

        String inputPassword = cred.getPassword();
        /* fetching the saved Password */
        String prevPassword = user.getPassword();
        /* verifying the Password */
        if ( !passwordEncoder.matches(inputPassword,prevPassword))
            throw  new BadCredentialsException("Incorrect Password ");

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        /* Building the response and return to the user*/
        return  LoginResDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    /* Deactivating the user */
    public UserDTO deactivatingMember(String id) {
        UserEntity user = userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Invalid member id:"+id));
        user.setIsActive(false);
        return  converToUserDtO(userRepository.save(user));
    }

    /* activating the user */
    public UserDTO activatingMember(String id) {
        UserEntity user = userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Invalid member ID:"+id));
        user.setIsActive(true);
        return  converToUserDtO(userRepository.save(user));
    }

    /* get the member details */
    public UserDTO getMemberDetails(String id) {
        UserEntity user = userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Invalid Member ID:"+id));
        return converToUserDtO(user);
    }

    /* fetch the all Members */
    public Page<UserDTO> getAllMembers(int page) {

        // Fetching all users with the role "MEMBER" and applying pagination
        Pageable pageable = PageRequest.of(page, 6);
        Page<UserEntity> members = userRepository.findAllUserByRole(Roles.MEMBER, pageable);

        // Mapping the Page of UserEntity to Page of RecordDTO
        return members.map(user -> {
            return modelMapper.map(user,UserDTO.class);
        });

    }

    public UserEntity getCurrentUser() {
        // Get the authentication object from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Check if the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Get the principal, which should be of type UserEntity
            Object principal = authentication.getPrincipal();
            // If the principal is an instance of UserEntity, return it directly
            if (principal instanceof UserEntity currentUser) {
                log.info("Current user: {}", currentUser);
                return currentUser;
            }
        }
        return null;
    }

    /* get all member registration details */
    public Page<UserDTO> getAllMemberRegistrationDetails(int page) {

        Pageable pageable = PageRequest.of(page,6);

        Page<MemberRegistrationEntity> allRequests = memberRegistrationRepository.findAll(pageable);
            return allRequests.map(user->{
                return modelMapper.map(user,UserDTO.class);
            });
    }

    /* get Single  member registration details */
    public UserDTO getMemberRegistrationDetails(String id) {
        MemberRegistrationEntity member =memberRegistrationRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Invalid registration id:"+ id));
        return modelMapper.map(member,UserDTO.class);
    }

    /* get all members which are approved by this member Id */
    public Page<UserDTO> getAllApprovedMembers(String id ,int page) {
        log.info("page,{} id:{}",page,id);
            Pageable pageable = PageRequest.of(page,6);
            Page<UserEntity> members = userRepository.findByApprovedByAndRole(id,Roles.MEMBER,pageable);
            for ( UserEntity  user : members) {
                log.info("all membrs{}",user);
            }
            return members.map(mem -> modelMapper.map(mem, UserDTO.class));
    }

    /* get the access Token before it expire*/
    public LoginResDTO getAccessToken() {
        UserEntity user = getCurrentUser();
        String accessToken = jwtService.generateAccessToken(user);
        return LoginResDTO.builder().accessToken(accessToken).build();
    }



}
