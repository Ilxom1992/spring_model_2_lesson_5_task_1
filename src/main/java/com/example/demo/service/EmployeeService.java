package com.example.demo.service;
import com.example.demo.config.GetTheUser;
import com.example.demo.entity.*;
import com.example.demo.entity.enams.TaskStatus;
import com.example.demo.payload.Response;
import com.example.demo.payload.SalaryDto;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
@Service
public class EmployeeService {
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final TurniketRepository turniketRepository;
    final TaskRepository taskRepository;
    final SalaryHistoryRepository salaryHistoryRepository;
    final TurniketHistoryRepository turniketHistoryRepository;
    GetTheUser getTheUser = new GetTheUser();
    public EmployeeService(UserRepository userRepository, RoleRepository roleRepository, TurniketRepository turniketRepository, TaskRepository taskRepository, SalaryHistoryRepository salaryHistoryRepository, TurniketHistoryRepository turniketHistoryRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.turniketRepository = turniketRepository;
        this.taskRepository = taskRepository;
        this.salaryHistoryRepository = salaryHistoryRepository;
        this.turniketHistoryRepository = turniketHistoryRepository;
    }


    public Response findAllEmployees() {
        Optional<User> userSystem = getTheUser.getCurrentAuditorUser();
        Set<Role> roles = userSystem.get().getRoles();
        if (!userSystem.isPresent()) {
            return new Response("Not found user", false);
        }
        for (Role role : roles) {
            //BU YERDA AGAR EMPLOYEE YOKI MENAGER  BO'LSA QAYTARIB YUBORILADI FAQAT DIRECTOR VA HR MANAGERNI KIRISHI MUMKIN
            if (role.getId() == 3 || role.getId() == 4) {
                return new Response("You don't have access for this operation", false);
            }
            //BU YERDA DIREKTOR KIRGAN BO'LSA UNGA DIRECTORLARDAN BOSHQA BARCHA RO'YHAT BERILADI
            if (role.getId() == 1) {
                List<User> userByRolesId = userRepository.getUserByRolesIdDirector();
                return new Response("Success!", true, userByRolesId);
            }
            //BU FAQAT HR MANAGER UCHUN UNGA FAQAT EMPLOYEE LAR RO'YHATI BERILADI
            List<User> userByRolesId = userRepository.getUserByRolesId(4);
            return new Response("Success!", true, userByRolesId);
        }
        return new Response("Not Found Employee", false);
    }
    // BERILGAN VAQIT  bo’yicha ishga kelib-ketishi va bajargan tasklari haqida ma’lumotLAR OLINADI
    public Response findOneByData(UUID id, Timestamp start, Timestamp finish) {
        Optional<User> userSystem = getTheUser.getCurrentAuditorUser();
        Set<Role> roles = userSystem.get().getRoles();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startLocalDate;
        LocalDateTime finishLocalDate;
        try {
            startLocalDate = start.toLocalDateTime();
            finishLocalDate = finish.toLocalDateTime();
        } catch (DateTimeParseException e) {
            return new Response("LocalDateTime no Parse", false);
        }

        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new Response("This employee not found", false);
        }

        for (Role role : roles) {
            if (role.getId() == 3 || role.getId() == 4) {
                return new Response("It is forbidden to receive this information", false);
            }
        }
                /*
                bazadan berilgan vaqit oralig'idagi user idga tegishli turniketlar istoriyasini olib  keladi
                 */
             List<TurniketHistory> turniketHistoryList = turniketHistoryRepository.getTurniketHistoryByUserId(id, startLocalDate, finishLocalDate);
             if (turniketHistoryList.isEmpty()) {
                 return new Response("Data not found!", false);
             }
        List<Task> taskList = taskRepository.findAllByStatusAndResponsibleId(TaskStatus.DONE, id);
        return new Response("Done", true, turniketHistoryList+" " + taskList);
    }
    public Response payMonthly(SalaryDto salaryDto) {
        Optional<User> userSystem = getTheUser.getCurrentAuditorUser();
        Set<Role> roles = userSystem.get().getRoles();
        boolean checkRole = false;
        for (Role role : roles) {
            if (role.getId() == 1 || role.getId() == 2) {
                checkRole = true;
                break;
            }
        }
        if (!checkRole)
            return new Response("You don't have access for this operation", false);
        Optional<User> optionalEmployee = userRepository.findById(salaryDto.getEmployeeId());
        if (!optionalEmployee.isPresent())
            return new Response("Such Employee was not found!", false);
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.setEmployee(optionalEmployee.get());
        salaryHistory.setSalaryAmount(salaryDto.getSalaryAmount());
        salaryHistory.setWorkStartDate(salaryDto.getWorkStartDate());
        salaryHistory.setWorkEndDate(salaryDto.getWorkEndDate());
        salaryHistoryRepository.save(salaryHistory);
        return new Response("Salary Saved! To: " + optionalEmployee.get().getFirstName(), true);
    }
    public Response getSalariesByMonth(String year, Integer monthNumber) {
        Optional<User> userSystem = getTheUser.getCurrentAuditorUser();
        Set<Role> roles = userSystem.get().getRoles();
        boolean checkRole = false;
            for (Role role : roles) {
                if (role.getId()==1 || role.getId()==2) {
                    checkRole = true;
                    break;
                }
            }
            if (!checkRole)
                return new Response("You don't have access for this operation", false);
            String month = monthNumber + "";
            if (monthNumber < 10)
                month = "0" + monthNumber;
            String full = year + "-" + month + "-01 05:00";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(full, formatter);
            Timestamp start = Timestamp.valueOf(dateTime);
            List<SalaryHistory> salaryHistoryList = salaryHistoryRepository.findAllByWorkStartDate(start);
            if (salaryHistoryList.size() == 0)
                return new Response("Salary list empty!", false);
            return new Response("Success!", true, salaryHistoryList);
        }
    public Response getSalariesByUserId(UUID id) {
        Optional<User> userSystem = getTheUser.getCurrentAuditorUser();
        Set<Role> roles = userSystem.get().getRoles();
        boolean checkRole = false;
            for (Role role : roles) {
                if (role.getId()==1 || role.getId()==2) {
                    checkRole = true;
                    break;
                }
            }
            if (!checkRole)
                return new Response("You don't have access for this operation", false);
            List<SalaryHistory> salaryHistoryList = salaryHistoryRepository.findAllByEmployeeId(id);
            if (salaryHistoryList.size() == 0)
                return new Response("Such employee did not get salary!", false);
            return new Response("Success!", true, salaryHistoryList);
        }
         }


