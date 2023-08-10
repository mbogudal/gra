package mikolaj.bogudal.pacman.business.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.java.Log;
import mikolaj.bogudal.pacman.business.dto.LevelDto;
import mikolaj.bogudal.pacman.business.dto.PlayerDto;
import mikolaj.bogudal.pacman.business.listener.PlayerListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Async
@Log
@Service
public class GamePlayService {
    private Random random = new Random();
    private long counterBricks = 1;
    private long counterEndGame = 1;
    private final List<JLabel> bricks;
    private JLabel brick;
    private final Point windowPoint;
    @Getter
    private final LevelDto levelDto;
    @Getter
    private final PlayerDto playerDto;
    private final ImageService imageService;
    private final SystemService systemService;

    public GamePlayService(ImageService imageService, SystemService systemService) {
        this.imageService = imageService;
        this.systemService = systemService;
        this.bricks = new ArrayList<>();
        windowPoint = new Point();
        var rows = 5;
        var cols = 5;
        levelDto = LevelDto
                .builder()
                .cols(cols)
                .rows(rows)
                .map(new String[rows][cols])
                .images(new HashMap<>())
                .endScreen(imageService.createImage("endScreen", 0, 0, systemService.getScreenW(), systemService.getScreenH()))
                .bricks(new JLabel[rows][cols])
                .background(imageService.createImage("background", 0, 0, systemService.getScreenW(), systemService.getScreenH()))
                .build();

        var playerPoint = new Point();
        playerDto = PlayerDto
                .builder()
                .playerListener(new PlayerListener(playerPoint, levelDto.getMap()))
                .player(imageService.createImage("player", 0, 0))
                .playerPoint(playerPoint)
                .build();
    }

    @PostConstruct
    void init() {
        log.info("game started");
        initMap();
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
        for (int i = 0; i < levelDto.getRows(); i++) {
            for (int j = 0; j < levelDto.getCols(); j++) {
                var brick = levelDto.getBricks()[i][j];
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
            if (windowPoint.x == playerDto.getPlayerPoint().x && windowPoint.y == playerDto.getPlayerPoint().y) {
                onPickup();
            }
        }
    }

    void incrementCounters() {
        counterBricks++;
        counterEndGame++;
    }

    void movePlayer() {
        playerDto.getPlayer().setLocation(playerDto.getPlayerPoint().x * 100, playerDto.getPlayerPoint().y * 100);
        playerDto.getPlayerListener().setReleased(true);
    }

    void onPickup() {
        levelDto.getBricks()[windowPoint.y][windowPoint.x] = null;
        counterBricks = 0;
        brick = null;
    }

    void onGameOver() {
        levelDto.getEndScreen().setVisible(true);
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

    private void initMap() {
        for (int i = 0; i < levelDto.getRows(); i++) {
            for (int j = 0; j < levelDto.getCols(); j++) {
                levelDto.getMap()[i][j] = "0";
            }
        }
    }

}
