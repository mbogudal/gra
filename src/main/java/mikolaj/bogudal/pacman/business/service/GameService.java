package mikolaj.bogudal.pacman.business.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Log
@Service
@RequiredArgsConstructor
public class GameService {

    private final DisplayService displayService;
    private Random random = new Random();
    private int millisCounter;
    private int secsCounter;

    @PostConstruct
    void init() {
        log.info("game started");
    }

    @Async
    @Scheduled(fixedRate = 16)
    void play() {
        displayService.getBricks()[displayService.getWindowPoint().y][displayService.getWindowPoint().x].setVisible(true);
        millisCounter++;
        if (millisCounter > 59) {
            millisCounter = 0;
            secsCounter++;
            if (secsCounter > 1) {
                secsCounter = 0;
                //after all this years i found first use case for do while
                do {
                    displayService.getWindowPoint().x=random.nextInt(10);
                    displayService.getWindowPoint().y=random.nextInt(10);
                } while (displayService.getBricks()[displayService.getWindowPoint().y][displayService.getWindowPoint().x] == null);
            }
        }
        displayService.getBricks()[displayService.getWindowPoint().y][displayService.getWindowPoint().x].setVisible(false);
        displayService.getPlayer().setLocation(displayService.getPlayerPoint().x*100, displayService.getPlayerPoint().y*100);
        displayService.getPlayerListener().setReleased(true);
    }

}
