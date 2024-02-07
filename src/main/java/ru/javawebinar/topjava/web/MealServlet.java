package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealMapRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.repository.MealMapRepository.nextId;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    public MealMapRepository mealMapRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        mealMapRepository = new MealMapRepository();
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get request");

        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            int mealId = Integer.parseInt(request.getParameter("id"));
            edit(request, response, mealId);
        } else if ("delete".equals(action)) {
            int mealId = Integer.parseInt(request.getParameter("id"));
            delete(request, response, mealId);
        } else {

            final int CALORIES_PER_DAY = 2000;
            List<Meal> meals = mealMapRepository.getAllMeals();

            List<MealTo> mealTos = MealsUtil.filteredByStreams(meals, LocalTime.MIDNIGHT,
                    LocalTime.MAX, CALORIES_PER_DAY);

            request.setAttribute("mealTos", mealTos);
            request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);

            request.getRequestDispatcher("meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("post requests");

        String action = request.getParameter("action");

        switch (action) {
            case "create":
                create(request, response);
                break;
            case "edit":
                int mealId = Integer.parseInt(request.getParameter("id"));
                Meal mealToUpdate = mealMapRepository.getById(mealId);

                LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("datetime"));
                String description = request.getParameter("description");
                int calories = Integer.parseInt(request.getParameter("calories"));

                mealToUpdate.setDateTime(dateTime);
                mealToUpdate.setDescription(description);
                mealToUpdate.setCalories(calories);

                update(request, response, mealToUpdate);
                break;
            case "delete":
                int mealIdToDelete = Integer.parseInt(request.getParameter("id"));
                delete(request, response, mealIdToDelete);
                break;
            default:
                log.debug("bug in action: {}    ", action);
                break;
        }
    }

    private void create(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        log.debug("create meal");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("datetime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal newMeal = new Meal(nextId++, dateTime, description, calories);
        mealMapRepository.save(newMeal);
        log.debug("created meal: {}", newMeal.toString());
        response.sendRedirect(request.getContextPath() + "/meals");
    }

    private void edit(HttpServletRequest request, HttpServletResponse response, int id)
            throws ServletException, IOException {
        log.debug("edit meal");
        Meal meal = mealMapRepository.getById(id);
        request.setAttribute("meal", meal);
        request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
        request.getRequestDispatcher("mealform.jsp").forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response, Meal updatedMeal)
            throws IOException {
        log.debug("update meal");
        mealMapRepository.save(updatedMeal);
        log.debug("updated meal: {}", updatedMeal.toString());
        response.sendRedirect(request.getContextPath() + "/meals");
    }

    private void delete(HttpServletRequest request, HttpServletResponse response, int id)
            throws IOException {
        log.debug("delete meal");
        mealMapRepository.delete(id);
        log.debug("deleted id meal: {}", id);
        response.sendRedirect(request.getContextPath() + "/meals");
    }

}
