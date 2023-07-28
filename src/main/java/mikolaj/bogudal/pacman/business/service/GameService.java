package mikolaj.bogudal.pacman.business.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Log
@Service
public class GameService {

    @PostConstruct
    void init(){
        log.info("game started");
    };

}
