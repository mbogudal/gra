package mikolaj.bogudal.pacman.business.service;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import mikolaj.bogudal.pacman.business.dto.LevelDto;
import mikolaj.bogudal.pacman.business.dto.LevelJsonDto;
import mikolaj.bogudal.pacman.business.dto.PlayerDto;
import mikolaj.bogudal.pacman.business.listener.PlayerListener;
import mikolaj.bogudal.pacman.util.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
    private LevelDto levelDto;
    @Getter
    private PlayerDto playerDto;
    private final ImageService imageService;
    private final SystemService systemService;
    private List<LevelJsonDto> levelJsonDtos;
    private List<LevelJsonDto> levelJsonDtosCopy;
    private final JFrame frame;

    private JPanel panel;
    private LevelJsonDto selected;

    @SneakyThrows
    public GamePlayService(ImageService imageService, SystemService systemService) {
        this.imageService = imageService;
        this.systemService = systemService;
        this.bricks = new ArrayList<>();
        windowPoint = new Point();

        levelJsonDtos = JsonUtil.deserialize(
                IOUtils.toString(new ClassPathResource("levels.json").getInputStream()),
                new TypeReference<List<LevelJsonDto>>() {
                }
        );

        levelJsonDtosCopy = new ArrayList<>(levelJsonDtos);

        JFrame frame = null;

        try {
            frame = new JFrame("Parnavaz-legend about his dream");
        } catch (HeadlessException e) {

        }
        this.frame = frame;

        if (frame != null) {
            this.frame.setLayout(null);
            this.frame.setVisible(true);
            frame.setFocusable(true);
            frame.setSize(systemService.getScreenW(), systemService.getScreenH());
        }
    }

    @PostConstruct
    void init() {
        initLevel();
        initMap();
        reloadDisplay();
        for (int i = 0; i < levelDto.getRows(); i++) {
            for (int j = 0; j < levelDto.getCols(); j++) {
                levelDto.getBricks()[i][j] = null;
            }
        }
        levelJsonDtosCopy = new ArrayList<>(levelJsonDtos);
    }

    void initLevel() {
        log.info("game started");
        var rows = 5;
        var cols = 5;
        selected = levelJsonDtosCopy.get(new Random().nextInt(levelJsonDtosCopy.size()));

        levelDto = LevelDto
                .builder()
                .cols(cols)
                .rows(rows)
                .map(new String[rows][cols])
                .images(new HashMap<>())
                .endScreen(imageService.createImage(selected.getAssetsLocation() + "/endScreen", 0, 0, systemService.getScreenW(), systemService.getScreenH()))
                .bricks(new JLabel[rows][cols])
                .background(imageService.createImage(selected.getAssetsLocation() + "/background", 0, 0, systemService.getScreenW(), systemService.getScreenH()))
                .assetsLocation(selected.getAssetsLocation())
                .build();

        var playerPoint = new Point();
        playerDto = PlayerDto
                .builder()
                .playerListener(new PlayerListener(playerPoint, levelDto.getMap()))
                .player(imageService.createImage(selected.getAssetsLocation() + "/player", 0, 0))
                .playerPoint(playerPoint)
                .build();
    }


    @Async
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
        if (bricks.isEmpty()) {
            onGameOver();
        }
        if (playerDto.getPlayerListener().isClickSpace()) {
            onNewGame();
            playerDto.getPlayerListener().setClickSpace(false);
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
        playerDto.getPlayerListener().setGameOver(true);
        levelJsonDtosCopy.remove(selected);
        if (levelJsonDtosCopy.isEmpty()) {
            levelJsonDtosCopy = new ArrayList<>(levelJsonDtos);
        }
    }

    void onNewGame() {
        levelDto.getEndScreen().setVisible(false);
        initLevel();
        initMap();
        reloadDisplay();
    }

    void onHideBricks() {
        if (bricks.size() == 0)
            return;
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

    private JPanel createJPanel() {
        var out = new JPanel();
        out.setLayout(null);
        out.setVisible(true);
        out.setBounds(0, 0, systemService.getScreenW(), systemService.getScreenH());
        out.setBackground(new Color(0, 0, 0));
        return out;
    }

    void reloadDisplay() {
        if (frame != null) {
            if (panel != null)
                frame.remove(panel);
            frame.addKeyListener(playerDto.getPlayerListener());
        }
        panel = createJPanel();
        panel.add(levelDto.getEndScreen());
        levelDto.getEndScreen().setVisible(false);
        panel.add(playerDto.getPlayer());
        for (int i = 0; i < levelDto.getRows(); i++) {
            for (int j = 0; j < levelDto.getCols(); j++) {
                if ("0".equals(levelDto.getMap()[i][j])) {
                    levelDto.getBricks()[i][j] = imageService.createImage(levelDto.getAssetsLocation() + "/bricks", j * 100, i * 100);
                    panel.add(levelDto.getBricks()[i][j]);
                }
            }
        }
        panel.add(levelDto.getBackground());
        panel.repaint();
        panel.revalidate();
        if (frame != null) {
            frame.add(panel);
            frame.repaint();
            frame.revalidate();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

    }

}
