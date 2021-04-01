package chess.controller;

import chess.domain.Game;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class WebChessController {
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void run() {
        staticFiles.location("/static");
        Game game = new Game();
        game.init();

        get("/", (req, res) ->
        {
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });

        post("/start", (req, res) ->
        {
            Map<String, Object> model = new HashMap<>();
            return render(model, "game.html");
        });

        post("/initialize", (req, res) -> {
            init(game);
            return "";
        });

        post("/game", (req, res) ->
        {
            game.init();
            Map<String, Object> model = new HashMap<>();
            model.put("squares", game.squareDtos());
            model.put("turn", game.turnColor());
            return GSON.toJson(model);
        });

        post("/move", (req, res) ->
        {
            try {
                isStart(game);
                WebChessController.move(game, req.queryParams("source"), req.queryParams("target"));
                if (game.isEnd()) {
                    return req.queryParams("source") + " " + req.queryParams("target") + " " + game.winnerColor().getSymbol();
                }
            } catch (RuntimeException e) {
                res.status(400);
                return e.getMessage();
            }

            return req.queryParams("source") + " " + req.queryParams("target") + " " + game.turnColor().getName();
        });

        post("/status", (req, res) ->
        {
            String result;
            try {
                result = game.computeWhitePoint() + " " + game.computeBlackPoint();
            } catch (RuntimeException e) {
                res.status(400);
                return e.getMessage();
            }

            return result;
        });

        post("/end", (req, res) ->
        {
            game.end();
            return "";
        });
    }

    private static void init(Game game) {
        game.init();
    }

    private static void isStart(Game game) {
        if (!game.isStart()) {
            throw new IllegalArgumentException("게임이 시작되지 않았습니다.");
        }
    }

    public static void move(Game game, String source, String target) {
        game.move(source, target);
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}