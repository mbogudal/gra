package mikolaj.bogudal.pacman.business.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Async
@Log
@Service
@RequiredArgsConstructor
public class GamePlayService {

    private final DisplayService displayService;
    private Random random = new Random();
    private long counterBricks=1;
    private long counterEndGame=1;

    @PostConstruct
    void init() {
        log.info("game started");
    }


    @Scheduled(fixedRate = 16)
    void perFrame() {
        displayService.getPlayer().setLocation(displayService.getPlayerPoint().x * 100, displayService.getPlayerPoint().y * 100);
        if (displayService.getWindowPoint().x == displayService.getPlayerPoint().x && displayService.getWindowPoint().y == displayService.getPlayerPoint().y) {
            displayService.getBricks()[displayService.getWindowPoint().y][displayService.getWindowPoint().x] = null;
            hideBricks();
            counterBricks =0;
        }
        displayService.getPlayerListener().setReleased(true);
        if(counterBricks %(60*1)==0){
            hideBricks();
            counterBricks =0;
        }
        if(counterEndGame%(60*60*3)==0){
            displayService.getEndScreen().setVisible(true);
        }
        counterBricks++;
        counterEndGame++;
    }

    void hideBricks() {
        if (displayService.getBricks()[displayService.getWindowPoint().y][displayService.getWindowPoint().x] != null)
            displayService.getBricks()[displayService.getWindowPoint().y][displayService.getWindowPoint().x].setVisible(true);
        //after all this years i found first use case for do while
        do {
            displayService.getWindowPoint().x = random.nextInt(10);
            displayService.getWindowPoint().y = random.nextInt(10);
        } while (displayService.getBricks()[displayService.getWindowPoint().y][displayService.getWindowPoint().x] == null);
        if (displayService.getBricks()[displayService.getWindowPoint().y][displayService.getWindowPoint().x] != null)
            displayService.getBricks()[displayService.getWindowPoint().y][displayService.getWindowPoint().x].setVisible(false);
    }

}
