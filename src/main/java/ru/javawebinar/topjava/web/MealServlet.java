package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MapMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    final int CALORIES_PER_DAY = 2000;

    private MapMealRepository mapMealRepository;

    @Override
    public void init() throws ServletException {
        mapMealRepository = new MapMealRepository();
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get request");
        String action = request.getParameter("action");

        if (action == null) {
            log.info("getAll");
            request.setAttribute("meals",
                    MealsUtil.filteredByStreams((new ArrayList<>(mapMealRepository.getAll())), LocalTime.MIDNIGHT,
                            LocalTime.MAX, CALORIES_PER_DAY));
            request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int id = getId(request);
            log.info("Delete {}", id);
            mapMealRepository.delete(id);
            response.sendRedirect("meals");
        } else {
            final Meal meal = action.equals("create") ?
                    new Meal(LocalDateTime.now(), "", 1000) :
                    mapMealRepository.getById(getId(request));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("mealForm.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        log.debug("post requests");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("datetime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        mapMealRepository.save(meal);
        response.sendRedirect("meals");
    }
}