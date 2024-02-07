package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealMapRepository implements MealRepository {

    private final Map<Integer, Meal> mealMap;
    public static int nextId = 1;
    public List<Meal> mealList = Arrays.asList(
            new Meal(nextId++, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(nextId++, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(nextId++, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(nextId++, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(nextId++, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(nextId++, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(nextId++, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

    public MealMapRepository() {
        mealMap = mealList.stream()
                .collect(Collectors.toMap(meal -> {
                    int id = meal.getId();
                    if (id == 0) {
                        id = nextId++;
                        meal.setId(id);
                    }
                    return id;
                }, meal -> meal));
    }

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal getById(int id) {
        return mealMap.get(id);
    }

    @Override
    public void save(Meal meal) {
        if (meal.getId() == 0) {
            meal.setId(nextId++);
        }
        mealMap.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        mealMap.remove(id);
    }
}

