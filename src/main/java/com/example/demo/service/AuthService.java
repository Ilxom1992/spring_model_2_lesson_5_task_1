package com.example.demo.service;
import com.example.demo.config.GetTheUser;
import com.example.demo.entity.Role;
import com.example.demo.entity.Task;
import com.example.demo.entity.enams.RoleEnum;
import com.example.demo.entity.User;
import com.example.demo.entity.enams.TaskStatus;
import com.example.demo.payload.LoginDto;
import com.example.demo.payload.RegisterDto;
import com.example.demo.payload.Response;
import com.example.demo.payload.TaskDto;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtFilter;
import com.example.demo.security.JwtProvider;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
@Service
public class AuthService implements UserDetailsService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final RoleRepository roleRepository;
    final JavaMailSender javaMailSender;
    final AuthenticationManager authenticationManager;
    final JwtProvider jwtProvider;
    final JwtFilter jwtFilter;
    final TaskRepository taskRepository;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JavaMailSender javaMailSender,
                       AuthenticationManager authenticationManager, JwtProvider jwtProvider, @Lazy JwtFilter jwtFilter, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.javaMailSender = javaMailSender;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.jwtFilter = jwtFilter;
        this.taskRepository = taskRepository;
    }
GetTheUser getTheUser =new GetTheUser();
    /**
     * BU METHOD BAZAGA USERNI REGISTIRATSIYADAN O'TKAZISH UCHUN ISHLATILADI
     * BAZAGA USERNI SAQLAYDI VA UNGA TASDIQLASH CODINI YUBORADI
     * @param registerDto
     * @return
     */
    //
    public Response userRegister(RegisterDto registerDto, HttpServletRequest httpServletRequest) {
        User user = new User();
        UserDetails userDetails = jwtFilter.getUser(httpServletRequest);
        //USER BAZADA BOR YOKI YO'QLIGI TEKSHIRILYABDI
        if (userDetails != null) {
            //USER BAZADAN OLINDI
            Optional<User> optionalUser = userRepository.findById(getTheUser.getCurrentAuditor().get());
            //USERNINIG ROLLARI OLINDI
            Set<Role> roleUser = optionalUser.get().getRoles();
            //ROLE TEKSHIRILADI AGAR 3 VA 4- ID LI ROLE BO'LSA REGISTRATSIYA QILISHGA RUHSAT BERILMAYDI QAYTARILADI
            for (Role roleId2 : roleUser) {
                if (roleId2.getId() == 3 || roleId2.getId() == 4) {
                    return new Response("Not add", false);
                }
            }
            //AGAR USER ROLE ID 2 BO'LSA U 1-2-3 ID LI ROLLARNI QO'SHA OLMAYDI QAYTARILADI
            for (Role role : roleUser) {
                if (role.getId() == 2 && (registerDto.getRoleListId().get(0) == 1 || registerDto.getRoleListId().get(0)==3 || registerDto.getRoleListId().get(0)==2) ) {
                    return new Response("Not add", false);
                }
            }
            //AKS HOLDA BAZAGA QO'SHISHGA RUHSAT BERILADI FAQAT KELGAN ROLLARDAN BIRINCHI ROLE BERILADI
            user.setRoles(Collections.singleton(roleRepository.findById(registerDto.getRoleListId().get(0)).get()));
            user.setFirstName(registerDto.getFirstName());
            user.setLastName(registerDto.getLastName());
            user.setEmail(registerDto.getEmail());
         // String newPassword=UUID.randomUUID().toString().substring(8);
            user.setPassword("");
            //tasodifiy sonni yaratib beradi va userga saqlanadi
            user.setEmailCode(UUID.randomUUID().toString());
            userRepository.save(user);
            //EMAILGA HABAR YUBORISH TASDIQLASH KODINI YUBORADI, METHODINI CHAQIRYABMIZ
            sendEmail(user.getEmail(), user.getEmailCode());
            return new Response("Muafaqiyatli ro'yhatdan o'tdingiz Aakkonutingiz " +
                    "aktivlashtirishingiz uchun emailni tasdiqlang " +
                    "va Yangi parol yuborildi", true);
        }

        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail){
            return new Response("Bunday email bazada mavjud",false);
        }
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleEnum.ROLE_DIRECTOR)));
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        //tasodifiy sonni yaratib beradi va userga saqlanadi
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);

        //EMAILGA HABAR YUBORISH TASDIQLASH KODINI YUBORADI, METHODINI CHAQIRYABMIZ
        sendEmail(user.getEmail(),user.getEmailCode());
        return new Response("Muafaqiyatli ro'yhatdan o'tdingiz Aakkonutingiz " +
                "aktivlashtirishingiz uchun emailni tasdiqlang",true);
    }

    /**
     * BU METHOD USER EMAILIGA ACTIVE LASHTIRISH CODINI  YUBORISH UCHUN ISHLATILADI
     * @param sendingEmail
     * @param emailCod
     * @return
     */
    public Boolean sendEmail(String sendingEmail,String emailCod) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Test@pdp.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Akkountni Tasdiqlash");
            mailMessage.setText("<a href='http://localhost:8080/auth/verifyEmail?emailCode="
                    + emailCod + "&email=" + sendingEmail +"&newPassword="+null+"'>Tasdiqlang</a>");
            javaMailSender.send(mailMessage);
            return true;
        }
catch (Exception e){
    return false;
}
    }
        /**
         * Bu method tizim hodimlariga vazfa jo'natish uchun bu methodda hodim emailiga habar yuboriladi uqa bul qilgach
         * tizimga statusi qaytadi.
          */
        public Boolean sendEmailToTask(String sendingEmail,TaskDto taskDto) {
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom("Test@pdp.com");
                mailMessage.setTo(sendingEmail);
                mailMessage.setSubject("Vazifa");
                mailMessage.setText("<a href='http://localhost:8080/auth/taskProgress?taskCode="+taskDto.getTaskCode()+"'>Taskni  Tasdiqlash</a>"+taskDto+"");
                javaMailSender.send(mailMessage);
                return true;
            }
            catch (Exception e){
                return false;
            }

    }
    public Boolean sendEmailToTaskDone(String sendingEmail) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Test@pdp.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Vazifa Bajarildi");
            mailMessage.setText("<a href='http://localhost:8080/auth/taskComplete'>Taskni  Qabul qilish</a>");
            javaMailSender.send(mailMessage);
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

    /**
     * BU METHOD EMAILGA HAT BORGANDAN SO'NG TASDIQLASH HABARINI YUBORGANDA QABUL
     * // QILIB OLIB UNI TEKSHIRIB ACCOUNTINI BAZADA
     * //ACTIVE LASHTIRADI
     *
     * @param email
     * @param emailCode
     * @return
     */
    public Response verifyEmail(String email, String emailCode,String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()){
            User user= optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            if (!newPassword.equals("null")){
            user.setPassword(passwordEncoder.encode(newPassword));}
            userRepository.save(user);
            return new Response("Account tasdiqlandi",true);
        }
        return new Response("Akkount alloqachon tasdiqlangan",false);
    }
    /**
     * bu method task yuboilganda taskni qabul qilgach u qabul qilganini tasdiqlaydi va taskk bajarilish jarayonida bo'ladi
     * qabul qilinganini va bajarilish jarayonida ekanini bazga yozadi yoki kelgan statusni olib statusinibelgilaydi
     */
    public Response taskProgress(String taskCode,Integer taskStatus) {
        Optional<Task> byTaskCode = taskRepository.findByTaskCode(taskCode);
        if (byTaskCode.isPresent()) {
            Task task = byTaskCode.get();
            if (task.getStatus().equals(TaskStatus.NEW)) {
                task.setStatus(TaskStatus.PROGRESS);
                taskRepository.save(task);
                return new Response("Vazifa  tasdiqlandi", true);
            }
            if (task.getStatus().equals(TaskStatus.PROGRESS) && taskStatus==2) {
                //VAZIFANI BERGAN MANAGERNI BAZADAN OLDIK VA UNGA VAZIFA BAJARILGANI HAQIDA HABAR YUBORAMIZ
                Optional<User> userByTaskCode = userRepository.getUserByTaskCode(taskCode);

                sendEmailToTaskDone(userByTaskCode.get().getEmail());
                task.setStatus(TaskStatus.DONE);
                taskRepository.save(task);
                return new Response("Vazifa  bajarildi", true);
            }
        }
        return new Response("Vazifa alloqachon Bajarilgan",false);
    }
    /**
     * BU METHOD USERGA LOGIN VA PAROLI BILAN KIRGANDA TOKIN YASAB QAYTARIB JO'NATADI
     * @param loginDto
     * @return
     */
    public Response login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()));
            User user=(User)authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles()
            );
            return new Response("Token",true,token);

        }catch (BadCredentialsException badCredentialsException){
            return new Response( "Parol yoki lagin hato",false);
        }
    }

    /**
     * BU METHOD BAZADAN YUZERNI TOPIB QAYTARADI
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser= userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new UsernameNotFoundException(" User topilmadi");
    }
    public Boolean emailForTask(String sendingEmail, String text, String subject) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("email@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }


    }
}
