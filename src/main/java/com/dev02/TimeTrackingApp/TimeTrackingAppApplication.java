package com.dev02.TimeTrackingApp;

import com.dev02.TimeTrackingApp.entity.UserRole;
import com.dev02.TimeTrackingApp.entity.enums.RoleType;
import com.dev02.TimeTrackingApp.payload.request.UserRequest;
import com.dev02.TimeTrackingApp.payload.request.UserRequestForRegister;
import com.dev02.TimeTrackingApp.repository.UserRoleRepository;
import com.dev02.TimeTrackingApp.service.helper.MethodHelper;
import com.dev02.TimeTrackingApp.service.user.UserRoleService;
import com.dev02.TimeTrackingApp.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class TimeTrackingAppApplication implements CommandLineRunner {

	private final UserRoleService userRoleService;
	private final UserRoleRepository userRoleRepository;
	private final UserService userService;
	private final MethodHelper methodHelper;

	public TimeTrackingAppApplication(UserRoleService userRoleService, UserRoleRepository userRoleRepository, UserService userService, MethodHelper methodHelper) {
		this.userRoleService = userRoleService;
		this.userRoleRepository = userRoleRepository;
		this.userService = userService;
		this.methodHelper = methodHelper;
	}

	public static void main(String[] args) {
		SpringApplication.run(TimeTrackingAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Role tablomu dolduracagim ama once bos mu diye kontrol edecegim
		if(userRoleService.getAllUserRole().isEmpty()){

			UserRole admin = new UserRole();
			admin.setRoleType(RoleType.ADMIN);
			admin.setRoleName("Admin");
			userRoleRepository.save(admin);

			UserRole user = new UserRole();
			user.setRoleType(RoleType.USER);
			user.setRoleName("User");
			userRoleRepository.save(user);

		}

		// Built_in Admin olusturuluyor eger sistemde Admin yoksa
		{
			UserRequestForRegister adminRequestForRegister = new UserRequestForRegister();
			adminRequestForRegister.setUsername("Admin");
			adminRequestForRegister.setEmail("admin@admin.com");
			adminRequestForRegister.setSsn("111-11-1111");
			adminRequestForRegister.setPassword("123456");
			adminRequestForRegister.setFirstName("Ahmet");
			adminRequestForRegister.setLastName("Mehmet");
			adminRequestForRegister.setPhoneNumber("111-111-1111");
			userService.saveUser(adminRequestForRegister, "Admin");
		}
}
}
