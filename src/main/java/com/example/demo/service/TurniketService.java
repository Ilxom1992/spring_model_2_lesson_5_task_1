package com.example.demo.service;

import com.example.demo.config.GetTheUser;
import com.example.demo.entity.Role;
import com.example.demo.entity.Turniket;
import com.example.demo.entity.TurniketHistory;
import com.example.demo.entity.User;
import com.example.demo.payload.Response;
import com.example.demo.repository.TurniketHistoryRepository;
import com.example.demo.repository.TurniketRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TurniketService {
    final TurniketRepository turniketRepository;
    final TurniketHistoryRepository turniketHistoryRepository;
    final UserRepository userRepository;
    GetTheUser getTheUser=new GetTheUser();

    public TurniketService(TurniketRepository turniketRepository, TurniketHistoryRepository tourniquetHistory, TurniketHistoryRepository turniketHistoryRepository, UserRepository userRepository) {
        this.turniketRepository = turniketRepository;
        this.turniketHistoryRepository = turniketHistoryRepository;
        this.userRepository = userRepository;
    }

    public Response addHistory(Integer tuniketId) {

        // bazadan ushbu aydili turniketni oldik
        Optional<Turniket> optionalTourniquet = turniketRepository.findById(tuniketId);
        //bazada borligini tekshirdik bazada mavjud bo'lmasa qaytariladi
        if (!optionalTourniquet.isPresent()) {return new Response("Invalid Tourniquet ID!", false);}

        //bu turniketninig statusini tekshirdik status true bo'lsa bu turniket kampaniyaga
        // kirgan agar status false bo'lsa kampanidan chiqib ketgan kirgan va chiqqan vaqtini turniket tarihiga yozib boramiz
        boolean status = optionalTourniquet.get().isStatus();
        Optional<Turniket> optionalTurniket  = turniketRepository.findById(tuniketId);
        if (status){
            //turni ket chiqish vaqti yozilmagan tarihini olib keladi
            Optional<TurniketHistory> optionalTurniketHistory = turniketHistoryRepository.getBy(tuniketId);
            //turniket bazada borligi va chiqish tarihiga chiqish vaqti yozilmagan istoriyani tekshiradi bo'sh emasligini
            if (optionalTurniket.isPresent() && optionalTurniketHistory.isPresent()){
                //turniketni oldik
               Turniket turniket=optionalTurniket.get();
               //statusini false qilib belgiladik kampaniyadan chiqdi
               turniket.setStatus(false);
               //tuniket tarihni oldik
               TurniketHistory turniketHistory= optionalTurniketHistory.get();
                // turniket egasini  kampaniyadan chiqish vaqtini yozadi
               turniketHistory.setExitDateTime(LocalDateTime.now());
               //BAZAGA SAQLANDI
               turniketRepository.save(turniket);
               turniketHistoryRepository.save(turniketHistory);
                return new Response("User Exit ",true);
            }
        }
        //har kirishda yangitdan istoriya ochib kirish vaqtini va hodimni  yozib qo'yamiz
        TurniketHistory turniketHistory=new TurniketHistory();

        turniketHistory.setTurniket(optionalTourniquet.get());

        Turniket turniket=optionalTurniket.get();
        //bu yerda hodim kampaniya kirayabdi istoriya kirish vaqti yozilyabdi
        turniket.setStatus(true);
        turniketRepository.save(turniket);
        turniketHistoryRepository.save(turniketHistory);
        return new Response("User Enter",true);
}

    public Response addturnket(String location, UUID userId) {
        Turniket turniket=new Turniket();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return new Response("User not found",false);
        }
        Set<Role> roleSet = getTheUser.getCurrentAuditorUser().get().getRoles();
        for (Role role: roleSet) {
            //FAQAT DIRECTOR VA HR MANAGERGA TUNIKET BERISHGA RUHSAT ETILADI
           if (role.getId()==1 || role.getId()==2){
                turniket.setLocation(location);
                turniket.setUser(optionalUser.get());
                turniketRepository.save(turniket);
               return new Response(" add turniket",true);
            }
        }
        return new Response("Not add turniket",false);

    }
}
