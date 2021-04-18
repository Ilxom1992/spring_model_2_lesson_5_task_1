package com.example.demo.service;

import com.example.demo.config.GetTheUser;
import com.example.demo.entity.Role;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.entity.enams.TaskStatus;
import com.example.demo.payload.Response;
import com.example.demo.payload.TaskDto;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {
    final UserRepository userRepository;
    final TaskRepository taskRepository;
    final AuthService authService;

    public TaskService(UserRepository userRepository, TaskRepository taskRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.authService = authService;
    }

    GetTheUser getTheUser=new GetTheUser();
    public Response addTask(TaskDto taskDto){


        //VAZIFANI YUBORGAN USERNI OLDIK
        Optional<User> userSystem = getTheUser.getCurrentAuditorUser();

        //VAZIFANI BAJARUVCHI HODIMNI BAZADAN OLDIK
        Optional<User> optionalResponsibleUser = userRepository.findById(taskDto.getResponsibleId());
        if (!optionalResponsibleUser.isPresent()){
            new Response("Not found optionalResponsible  id ",false);
        }
        //SISTEMADAGI USERNINIG ROLLARINI OLDIK
        Set<Role> roleSet = userSystem.get().getRoles();

        //ROLLARNI TEKSHIRDIK
        for (Role role: roleSet) {
            switch (role.getId()){
               //DIRECTOR
                case 1:
                    for (Role roleResponsible:optionalResponsibleUser.get().getRoles()) {
                        if (roleResponsible.getId()==1){
                            //VAZIFA RAD ETILDA
                            return new Response("You can not assign the task to this user!", false);
                        }else {
                            //VAZIFA BAZAGA SAQLANDI
                            Response response = saveTask(taskDto, userSystem.get(), optionalResponsibleUser.get());
                            //VAZIFA   BAJARUVCHI EMAILIGA YUBORILDI
                            authService.sendEmailToTask(optionalResponsibleUser.get().getEmail(),taskDto);
                            return response;
                        }
                    }
                    break;
                //MANAGER LAR
                case 2:
                case 3:
                    for (Role roleResponsible:optionalResponsibleUser.get().getRoles()) {
                        if (roleResponsible.getId() == 1 || roleResponsible.getId() == 2 || roleResponsible.getId() == 3  ) {
                            //VAZIFA RAD ETILDA
                            return new Response("You can not assign the task to this user!", false);
                        } else {
                            //VAZIFA BAZAGA SAQLANADI
                            Response response = saveTask(taskDto, userSystem.get(), optionalResponsibleUser.get());
                            //VAZIFA  QABUL BAJARUVCHI, EMAILIGA YUBORILDI
                           authService.sendEmailToTask(optionalResponsibleUser.get().getEmail(),taskDto);
                            return response;
                        }
                    }

                //EMPLOYEE
                case 4:
                    return new Response("You can not assign the task to this user!", false);
                //ROLE ID TOPILMASA QAYTARILADI
                default:new Response("Such role id Not found",false);
            }
        }
        return new Response("Ok",true,userSystem.get());
    }

    public Response saveTask(TaskDto taskDto,User userSystem,User userResponsible){
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setBody(taskDto.getBody());
        task.setResponsible(userResponsible);
        task.setStatus(TaskStatus.NEW);
        task.setDeadLine(taskDto.getDeadLine());
        task.setTaskCode(taskDto.getTaskCode());
        taskRepository.save(task);
        return new Response("Task assigned! From: "
                + userSystem.getFirstName()     + " " + userSystem.getLastName() + " To: "
                + userResponsible.getFirstName()+ " " + userResponsible.getLastName(), true,taskDto);
    }
public Response taskProgress(String taskCode,Integer taskProgress){
        return authService.taskProgress(taskCode,taskProgress);
}
    public Response checkEmployeeTask(UUID employeeId,Integer  taskStatus) {
        //VAZIFANI YUBORGAN USERNI OLDIK
        Optional<User> userSystem = getTheUser.getCurrentAuditorUser();
        if (userSystem.isPresent()){
            Set<Role> roles = userSystem.get().getRoles();
            for (Role role: roles) {
              if (role.getId()==3 ||role.getId()==4){
                  return new Response("You don't have access for this operation!", false);
              }
                List<Task> tasks=taskRepository.findAllByStatusAndResponsibleId(taskStatus,employeeId);
              if (tasks.size()==0)
                return new Response("There is not any task for this data!", false);

                return new Response("Success!", true, tasks);
            }


        }
        return new Response("Authorization empty!", false);
    }
}
