package topjava.util;

import topjava.model.UserMeal;
import topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(10, 0), LocalTime.of(15, 0), 2000);
        mealsTo.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo2 = filteredByStreams(meals, LocalTime.of(10, 0), LocalTime.of(15, 0), 2000);
        mealsTo2.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo3 = filteredByOneCycle(meals, LocalTime.of(10, 0), LocalTime.of(15, 0), 2000);
        mealsTo3.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            int calories = meal.getCalories();
            caloriesSumByDate.put(mealDate, caloriesSumByDate.getOrDefault(mealDate, 0) + calories);
        }
        final List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalTime mealTime = meal.getDateTime().toLocalTime();

            if (TimeUtil.isBetweenHalfOpen(mealTime, startTime, endTime)) {
                LocalDate mealDate = meal.getDateTime().toLocalDate();
                boolean exceed = caloriesSumByDate.get(mealDate) > caloriesPerDay;
                mealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed));
            }
        }
        return mealWithExcesses;
    }

    //optional
    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime
            startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        meals.forEach(meal -> caloriesSumByDate.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));

        final List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();
        meals.forEach(meal -> {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesSumByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        });
        return mealWithExcesses;
    }

    //Optional 2, что смог придумать через 1 цикл
    // метод не выдает корректных данных если временной промежуток указывать до приема пищи,
    // после которого превышается лимит калорий
    public static List<UserMealWithExcess> filteredByOneCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        final List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();

        for (UserMeal meal : meals) {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            int calories = meal.getCalories();

            caloriesSumByDate.put(mealDate, caloriesSumByDate.getOrDefault(mealDate, 0) + calories);
            LocalTime mealTime = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(mealTime, startTime, endTime)) {
                boolean exceed = caloriesSumByDate.get(mealDate) > caloriesPerDay;
                mealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed));
            }
        }

        return mealWithExcesses;
    }
}


