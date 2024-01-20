package com.hust.interviewmanagement;

import com.hust.interviewmanagement.entities.*;
import com.hust.interviewmanagement.enums.ERole;
import com.hust.interviewmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
@EnableAsync
public class InterviewManagementApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(InterviewManagementApplication.class, args);
    }


    private final SkillRepository skillRepository;
    private final LevelRepository levelRepository;
    private final DepartmentRepository departmentRepository;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        List<Skill> skills = List.of(Skill.builder().name("Java").build(),
                Skill.builder().name("Nodejs").build(),
                Skill.builder().name("Javascript").build(),
                Skill.builder().name("ReactJs").build(),
                Skill.builder().name("Laravel").build(),
                Skill.builder().name("PHP").build(),
                Skill.builder().name(".Net").build(),
                Skill.builder().name("C++").build(),
                Skill.builder().name("C#").build(),
                Skill.builder().name("BA").build(),
                Skill.builder().name("Communication").build()
        );


        List<Department> departments = List.of(
                Department.builder().name("IT").build(),
                Department.builder().name("HR").build(),
                Department.builder().name("Finance").build(),
                Department.builder().name("Communication").build(),
                Department.builder().name("Marketing").build(),
                Department.builder().name("Accounting").build()
        );
        List<Level> levels = List.of(
                Level.builder().name("Fresher 1").build(),
                Level.builder().name("Junior 2.1").build(),
                Level.builder().name("Junior 2.2").build(),
                Level.builder().name("Junior 3.1").build(),
                Level.builder().name("Junior 3.2").build(),
                Level.builder().name("Delivery").build(),
                Level.builder().name("Leader").build(),
                Level.builder().name("Manager").build(),
                Level.builder().name("Vice Head").build()
        );


        List<Users> users = List.of(
                Users.builder()
                        .fullName("Admin")
                        .department(Department.builder().id(1L).build())
                        .account(Account.builder()
                                .email("Dung@gmail.com")
                                .password(passwordEncoder
                                        .encode("123456"))
                                .role(ERole.ROLE_ADMIN)
                                .checkPassword(true)
                                .status(true)
                                .build())
                        .build(),
                Users.builder()
                        .fullName("Dung")
                        .department(Department.builder().id(1L).build())
                        .account(Account.builder()
                                .email("Dungg@gmail.com")
                                .password(passwordEncoder
                                        .encode("123456"))
                                .role(ERole.ROLE_MANAGER)
                                .checkPassword(true)
                                .status(true)
                                .build())
                        .build(),
                Users.builder()
                        .fullName("Dung_1")
                        .department(Department.builder().id(1L).build())
                        .account(Account.builder()
                                .email("Dungg1@gmail.com")
                                .password(passwordEncoder
                                        .encode("123456"))
                                .checkPassword(true)
                                .status(true)
                                .role(ERole.ROLE_RECRUITER)
                                .build())
                        .build(),
                Users.builder()
                        .fullName("Dung_2")
                        .department(Department.builder().id(1L).build())
                        .account(Account.builder()
                                .email("Dungg2@gmail.com")
                                .password(passwordEncoder
                                        .encode("123456"))
                                .role(ERole.ROLE_INTERVIEW)
                                .checkPassword(true)
                                .status(true)
                                .build())
                        .build(),
                Users.builder()
                        .fullName("Dung_3")
                        .department(Department.builder().id(1L).build())
                        .account(Account.builder()
                                .email("Dungg3@gmail.com")
                                .password(passwordEncoder.encode("123456"))
                                .role(ERole.ROLE_INTERVIEW)
                                .checkPassword(true)
                                .status(true)
                                .build())
                        .build(),
                Users.builder()
                        .fullName("Dung_4")
                        .department(Department.builder().id(1L).build())
                        .account(Account.builder()
                                .email("Dungg4@gmail.com")
                                .password(passwordEncoder
                                        .encode("123456"))
                                .role(ERole.ROLE_INTERVIEW)
                                .checkPassword(true)
                                .status(true)
                                .build())
                        .build()
        );
//
//        skillRepository.saveAll(skills);
//        levelRepository.saveAll(levels);
//        departmentRepository.saveAll(departments);
//        userRepository.saveAll(users);
    }
}
