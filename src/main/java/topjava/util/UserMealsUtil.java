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

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(15, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            int calories = meal.getCalories();
            if (caloriesSumByDate.containsKey(mealDate)) {
                int currentCalories = caloriesSumByDate.get(mealDate);
                caloriesSumByDate.put(mealDate, currentCalories + calories);
            } else {
                caloriesSumByDate.put(mealDate, calories);
            }
        }
        final List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();
        for (UserMeal meal : meals) {

        }
        return null;
    }
    public static List<UserMealWithExcess> filteredByStreams (List < UserMeal > meals, LocalTime
            startTime, LocalTime endTime,int caloriesPerDay){
        return null;
    }
}

