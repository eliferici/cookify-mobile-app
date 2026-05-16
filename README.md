# Cookify

Cookify is an Android mobile application developed using Java and Firebase technologies. The application is designed to help users discover suitable recipes based on the ingredients available at home and to simplify the meal planning process.

The system analyzes user-selected ingredients, recommends appropriate recipes, calculates missing ingredients automatically, and allows users to create shopping lists efficiently.

---

## Project Features

* Ingredient-based recipe recommendation system
* Suggested recipes with complete ingredient matching
* Minimum missing ingredient recipe suggestions
* Shopping list generation for missing ingredients
* User registration and login system
* Email verification with Firebase Authentication
* User-specific recipe adding feature
* Daily recipe recommendation system
* Light and Dark mode support
* Modern and user-friendly interface design

---

## Technologies Used

* Java
* Android Studio
* XML
* Firebase Firestore
* Firebase Authentication
* RecyclerView
* SharedPreferences
* MVVM Architecture
* Single Activity + Multiple Fragment Structure

---

## Application Modules

* Onboarding Module
* Home Module
* Ingredient Selection Module
* Recipe Recommendation Module
* Shopping List Module
* User Authentication Module
* Add Recipe Module
* Theme and Notification Module

---

## System Workflow

1. The user selects available ingredients from categorized lists.
2. Selected ingredients are stored in Firebase Firestore.
3. The application filters recipes according to selected ingredients.
4. Fully matched recipes are displayed in the Suggested Recipes section.
5. Recipes with 1–3 missing ingredients are displayed in the Minimum Ingredient Recipes section.
6. Missing ingredients can be added automatically to the shopping list.

---

## Firebase Integration

Firebase services are used for:

* User authentication and login operations
* Email verification
* Cloud-based data storage
* User-specific recipe management
* Shopping list management
* Ingredient data storage

---

## Project Purpose

The purpose of this project is to provide users with a practical and efficient meal planning experience by utilizing the ingredients they already have at home. The application also aims to reduce unnecessary shopping and food waste.

---

## Future Improvements

* Integration with external recipe APIs
* Dynamic recipe image support
* AI-supported recipe recommendation system
* Nutritional analysis features
* Multi-language support
