package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) throws Exception {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 420)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(21, 0), 2000);
        mealsTo.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo2 = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(21, 0), 2000);
        mealsTo2.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo3 = filteredByOneCycle(meals, LocalTime.of(7, 0), LocalTime.of(21, 0), 2000);
        mealsTo3.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime,
                                                            int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesSumByDate.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        final List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesSumByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return mealWithExcesses;
    }

    //optional
    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime,
                                                             int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesSumByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    //Optional 2 через цикл
    public static List<UserMealWithExcess> filteredByOneCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime,
                                                              int caloriesPerDay) throws Exception {
        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        List<Callable<UserMealWithExcess>> taskList = new ArrayList<>();

        meals.forEach(meal -> {
            caloriesSumByDate.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                taskList.add(() -> new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(), meal.getCalories(), caloriesSumByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        });
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        List<Future<UserMealWithExcess>> futures = executorService.invokeAll(taskList);

        final List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();
        for (Future<UserMealWithExcess> future : futures) {
            mealWithExcesses.add(future.get());
        }
        executorService.shutdown();

        return mealWithExcesses;
    }

    /*Optional 2 через стрим, не придумал
    public static List<UserMealWithExcess> filteredByOneStream(List<UserMeal> meals, LocalTime startTime, LocalTime endTime,
                                                              int caloriesPerDay) {

    }*/
}


