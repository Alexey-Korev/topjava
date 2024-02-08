package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {

    Collection<Meal> getAll();

    Meal getById(int id);

    Meal save(Meal meal);

    void delete(int id);

}
