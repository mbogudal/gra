package mikolaj.bogudal.pacman.business.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Async
@Log
@Service
public class GamePlayService {

    private final DisplayService displayService;
    private Random random = new Random();
    private long counterBricks = 1;
    private long counterEndGame = 1;
    private final List<JLabel> bricks;
    private JLabel brick;
    private final Point windowPoint;

    public GamePlayService(DisplayService displayService) {
        this.displayService = displayService;
        this.bricks = new ArrayList<>();
        windowPoint = new Point();
    }

    @PostConstruct
    void init() {
        log.info("game started");
    }


    @Scheduled(fixedRate = 16)
    void perFrame() {
        movePlayer();
        detectCollisions();
        environmentBehavior();
        validateRules();
        incrementCounters();
    }

    void environmentBehavior() {
        bricks.clear();
        for (int i = 0; i < displayService.getLevelDto().getRows(); i++) {
            for (int j = 0; j < displayService.getLevelDto().getCols(); j++) {
                var brick = displayService.getBricks()[i][j];
                if (brick != null) {
                    bricks.add(brick);
                }
            }
        }

        if (counterBricks % (60 * 2) == 0) {
            onHideBricks();
            counterBricks = 1;
        }
    }

    void validateRules() {
        if (bricks.isEmpty() || counterEndGame % (60 * 60 * 3) == 0) {
            onGameOver();
        }
    }

    void detectCollisions() {
        if (brick != null) {
            if (windowPoint.x == displayService.getPlayerPoint().x && windowPoint.y == displayService.getPlayerPoint().y) {
                onPickup();
            }
        }
    }

    void incrementCounters() {
        counterBricks++;
        counterEndGame++;
    }

    void movePlayer() {
        displayService.getPlayer().setLocation(displayService.getPlayerPoint().x * 100, displayService.getPlayerPoint().y * 100);
        displayService.getPlayerListener().setReleased(true);
    }

    void onPickup() {
        displayService.getBricks()[windowPoint.y][windowPoint.x] = null;
        counterBricks = 0;
        brick = null;
    }

    void onGameOver() {
        displayService.getEndScreen().setVisible(true);
        displayService.getBtnExcellent().setVisible(true);
        displayService.getBtnMeh().setVisible(true);
        while (true) ;
    }

    void onHideBricks() {
        if (brick != null)
            brick.setVisible(true);
        brick = bricks.get(random.nextInt(bricks.size()));
        windowPoint.x = brick.getX() / 100;
        windowPoint.y = brick.getY() / 100;
        if (brick != null)
            brick.setVisible(false);
    }

}
