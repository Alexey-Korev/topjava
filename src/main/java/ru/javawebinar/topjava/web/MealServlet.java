package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        final int CALORIES_PER_DAY = 2000;
        List<Meal> meals = MealsUtil.getList();

        List<MealTo> mealTos = MealsUtil.filteredByStreams(meals, LocalTime.MIDNIGHT,
                LocalTime.MAX, CALORIES_PER_DAY);

        request.setAttribute("mealTos", mealTos);
        request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);

        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
