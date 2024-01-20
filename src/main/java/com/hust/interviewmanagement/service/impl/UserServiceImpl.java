package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.Account;
import com.hust.interviewmanagement.entities.Department;
import com.hust.interviewmanagement.entities.Users;
import com.hust.interviewmanagement.enums.EMessageUser;
import com.hust.interviewmanagement.enums.ERole;
import com.hust.interviewmanagement.repository.AccountRepository;
import com.hust.interviewmanagement.repository.DepartmentRepository;
import com.hust.interviewmanagement.repository.UserRepository;
import com.hust.interviewmanagement.service.EmailService;
import com.hust.interviewmanagement.service.UserService;
import com.hust.interviewmanagement.web.request.PasswordRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import com.hust.interviewmanagement.web.request.UserRequest;
import com.hust.interviewmanagement.web.response.Interviewer;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String ALPHA_UPPERCASE = ALPHA.toUpperCase(); // A-Z
    private static final String DIGITS = "0123456789"; // 0-9
    private static final String ALPHA_NUMERIC = ALPHA + ALPHA_UPPERCASE + DIGITS;
    private static final Random generator = new Random();
    @Override
    public Users findUserByAccount_Email(String email) {
        return userRepository.findByAccount_Email(email)
                .orElseThrow(()-> new UsernameNotFoundException(EMessageUser.DONT_USER.getValue()));
    }

    @Override
    public List<Interviewer> findInterviewerByDate(LocalDate date) {
        return userRepository.findInterviewers(date);
    }

    @Override
    public List<Users> findUserByRoleInterviewAndRecruiter() {
        return  userRepository.findUsersByAccount_RoleIn(
                List.of(ERole.ROLE_INTERVIEW,ERole.ROLE_RECRUITER));
    }

    @Override
    public List<Users> findUserByRoleManager() {
        return userRepository.findUsersByAccount_Role(ERole.ROLE_MANAGER);
    }

    @Override
    public List<Users> findUserByRoleInterview() {
        return userRepository.findUsersByAccount_Role(ERole.ROLE_INTERVIEW);
    }

    @Override
    public List<Users> findUserByRoleRecruiter() {
        return userRepository.findUsersByAccount_Role(ERole.ROLE_RECRUITER);
    }

    @Override
    public List<Users> findUserByRoleRecruiterAndManager() {
        return userRepository.findUsersByAccount_RoleIn(
                List.of(ERole.ROLE_MANAGER,ERole.ROLE_RECRUITER));
    }

    @Override
    public Page<Users> findAllUser(SearchRequest searchRequest) {
        Pageable pageable = PageRequest
                .of(searchRequest.getPageNumber() - 1, SearchRequest.PAGE_SIZE);
        if (searchRequest.getRole() == null || searchRequest.getRole().name().isEmpty()) {
            return userRepository.findAll(searchRequest.getParam(), pageable);
        }
        return userRepository.findAll(searchRequest.getParam(), searchRequest.getRole(), pageable);
    }

    @Override
    public Users findUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteUserById(Long id) {

    }

    @Override
    public Users findUserByAccountId(String email) {
        return null;
    }

    @Override
    public Users saveUser(UserRequest userRequest) throws MessagingException {
        Users users = modelMapper.map(userRequest, Users.class);

        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("dont"));
        users.setDepartment(department);
        final String password = randomPassword(10);
        Account account = getAccount(userRequest, "123456");
        account.setUser(users);

        users.setAccount(account);
        Users users1 =  userRepository.save(users);
        //        emailService.sendMailToUser(account, password);
        return users1;
    }

    @Override
    public Users updateUser(UserRequest userRequest) {
        Users users = userRepository.findById(userRequest.getId())
                .orElseThrow();
        modelMapper.map(userRequest, users);
        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow();
        users.setDepartment(department);
        users.getAccount().setEmail(userRequest.getEmail());
        users.getAccount().setRole(userRequest.getRole());
        users.getAccount().setStatus(userRequest.isStatus());
        return userRepository.save(users);
    }

    @Override
    @Transactional
    public Users changePassword(PasswordRequest passwordRequest, String email) {
        Users users = userRepository.findByAccount_Email(email).orElseThrow();
        users.getAccount().setCheckPassword(true);
        users.getAccount().setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        return userRepository.save(users);
    }

    private String randomPassword(int size) {
        List<String> result = new ArrayList<>();
        Consumer<String> appendChar = s -> {
            int number = randomNumber(s.length());
            result.add("" + s.charAt(number));
        };
        appendChar.accept(DIGITS);
        while (result.size() < size) {
            appendChar.accept(ALPHA_NUMERIC);
        }
        Collections.shuffle(result, generator);
        return String.join("", result);
    }

    private Account getAccount(UserRequest userRequest, String password) {
        Account account = new Account();
        account.setPassword(passwordEncoder.encode(password));
        account.setStatus(userRequest.isStatus());
        account.setCheckPassword(false);
        account.setEmail(userRequest.getEmail());
        account.setRole(userRequest.getRole());
        return account;
    }
    private int randomNumber(int max) {
        return generator.nextInt(max);
    }
}
