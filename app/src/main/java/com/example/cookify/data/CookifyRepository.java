package com.example.cookify.data;

import com.example.cookify.model.Ingredient;
import com.example.cookify.model.Recipe;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.example.cookify.model.ShoppingItem;
import com.google.firebase.firestore.DocumentSnapshot;
public class CookifyRepository {

    private final FirebaseFirestore db;

    public CookifyRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public ArrayList<Ingredient> getAllIngredients() {
        ArrayList<Ingredient> list = new ArrayList<>();

        String[] sebzeler = {
                "Domates", "Salatalık", "Marul", "Roka", "Maydanoz", "Dereotu",
                "Taze soğan", "Patlıcan", "Kabak", "Brokoli", "Karnabahar",
                "Havuç", "Patates", "Soğan", "Sarımsak", "Biber", "Mantar",
                "Ispanak", "Pırasa", "Kereviz", "Limon", "Bezelye"
        };

        String[] meyveler = {
                "Elma", "Muz", "Portakal", "Mandalina", "Çilek", "Üzüm",
                "Karpuz", "Kavun", "Şeftali", "Armut", "Kiraz", "Vişne",
                "Ananas", "Kivi", "Mango", "Nar", "Erik", "Greyfurt"
        };

        String[] bakliyatlar = {
                "Nohut", "Kuru fasulye", "Mercimek", "Yeşil mercimek",
                "Kırmızı mercimek", "Barbunya", "Börülce", "Bulgur",
                "Pirinç", "Kinoa", "Chia tohumu", "Yulaf", "Makarna",
                "Şehriye", "Kuskus", "İrmik", "Un", "Nişasta"
        };

        String[] sutUrunleri = {
                "Süt", "Kaşar peyniri", "Beyaz peynir", "Tulum peyniri",
                "Labne", "Krema", "Yoğurt", "Süzme yoğurt", "Ayran",
                "Tereyağı", "Margarin", "Kefir", "Mozzarella", "Parmesan",
                "Cheddar", "Krem peynir", "Lor peyniri", "Beşamel sos"
        };

        String[] etUrunleri = {
                "Tavuk", "Tavuk göğsü", "Tavuk but", "Dana kıyma",
                "Dana kuşbaşı", "Kıyma", "Köfte", "Sucuk", "Salam",
                "Sosis", "Pastırma", "Hindi füme", "Balık", "Somon",
                "Ton balığı", "Karides", "Yumurta"
        };

        String[] sosBaharatlar = {
                "Ketçap", "Mayonez", "Hardal", "Barbekü sos", "Acı sos",
                "Soya sosu", "Nar ekşisi", "Sirke", "Zeytinyağı",
                "Ayçiçek yağı", "Sıvı yağ", "Tuz", "Karabiber",
                "Pul biber", "Kekik", "Kimyon", "Köri", "Nane",
                "Tarçın", "Salça", "Domates sosu", "Kakao", "Şeker",
                "Limon suyu", "Sezar sos", "Yoğurt sosu"
        };

        String[] kuruyemis = {
                "Kuruyemiş", "Fındık", "Badem", "Kaju", "Ceviz",
                "Antep fıstığı", "Kuru üzüm", "Hurma"
        };

        String[] konserve = {
                "Domates konservesi", "Ton balığı konservesi", "Garnitür",
                "Mısır konservesi", "Bezelye konservesi", "Turşu",
                "Konserve fasulye", "Konserve nohut", "Haşlanmış nohut",
                "Zeytin ezmesi", "Kahvaltılık sos", "Bulyon"
        };

        String[] firinAtistirmalik = {
                "Ekmek", "Lavaş", "Burger ekmeği", "Kruton",
                "Petibör bisküvi", "Bisküvi", "Lazanya yaprağı"
        };

        addItems(list, sebzeler, "Sebzeler");
        addItems(list, meyveler, "Meyveler");
        addItems(list, bakliyatlar, "Bakliyatlar");
        addItems(list, sutUrunleri, "Süt Ürünleri");
        addItems(list, etUrunleri, "Et Ürünleri");
        addItems(list, sosBaharatlar, "Sos ve Baharatlar");
        addItems(list, kuruyemis, "Kuruyemiş");
        addItems(list, konserve, "Konserve");
        addItems(list, firinAtistirmalik, "Fırın ve Atıştırmalık");

        return list;
    }

    private void addItems(ArrayList<Ingredient> list, String[] items, String category) {
        for (String item : items) {
            list.add(new Ingredient(item, category));
        }
    }

    public void saveSelectedIngredients(ArrayList<Ingredient> selectedIngredients) {
        db.collection("selectedIngredients")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete();
                    }

                    for (Ingredient ingredient : selectedIngredients) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", ingredient.getName());
                        data.put("category", ingredient.getCategory());

                        db.collection("selectedIngredients").add(data);
                    }
                });
    }

    public ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();

        addRecipe(recipes, "Tereyağlı Yumurta",
                "Basit ve hızlı kahvaltılık.",
                "Yumurta", "Tereyağı", "Tuz");

        addRecipe(recipes, "Kaşarlı Tost",
                "Pratik ve doyurucu tost.",
                "Ekmek", "Kaşar peyniri", "Tereyağı");

        addRecipe(recipes, "Domatesli Omlet",
                "Domatesli hafif omlet.",
                "Yumurta", "Domates", "Tuz");

        addRecipe(recipes, "Patates Kızartması",
                "Klasik çıtır patates kızartması.",
                "Patates", "Sıvı yağ", "Tuz");

        addRecipe(recipes, "Cacık",
                "Yoğurt ve salatalıkla ferah tarif.",
                "Yoğurt", "Salatalık", "Nane", "Tuz");

        addRecipe(recipes, "Salçalı Makarna",
                "Az malzemeyle pratik makarna.",
                "Makarna", "Salça", "Sıvı yağ", "Tuz");

        addRecipe(recipes, "Sarımsaklı Yoğurtlu Makarna",
                "Yoğurtlu ve sarımsaklı pratik tarif.",
                "Makarna", "Yoğurt", "Sarımsak", "Tereyağı");

        addRecipe(recipes, "Çoban Salata",
                "Taze sebzelerle hafif salata.",
                "Domates", "Salatalık", "Soğan", "Limon", "Zeytinyağı");

        addRecipe(recipes, "Lavaş Pizza",
                "Lavaşla kolay pizza.",
                "Lavaş", "Kaşar peyniri", "Sucuk", "Ketçap");

        addRecipe(recipes, "Mercimek Çorbası",
                "Kırmızı mercimekle klasik çorba.",
                "Kırmızı mercimek", "Soğan", "Salça", "Tereyağı");

        addRecipe(recipes, "Patatesli Yumurta",
                "Patates ve yumurtayla doyurucu tarif.",
                "Patates", "Yumurta", "Tuz", "Karabiber");

        addRecipe(recipes, "Tavuk Sote",
                "Sebzeli pratik tavuk yemeği.",
                "Tavuk göğsü", "Biber", "Soğan", "Sıvı yağ");

        addRecipe(recipes, "Ton Balıklı Sandviç",
                "Hızlı ve pratik sandviç.",
                "Ton balığı", "Mayonez", "Ekmek", "Marul");

        addRecipe(recipes, "Tavuklu Sezar Salata",
                "Tavuklu doyurucu salata.",
                "Marul", "Tavuk göğsü", "Parmesan", "Kruton", "Sezar sos");

        addRecipe(recipes, "Sebzeli Pilav",
                "Sebzeli kolay pilav.",
                "Pirinç", "Havuç", "Bezelye", "Tereyağı");

        addRecipe(recipes, "Fırında Kaşarlı Patates",
                "Fırında kremalı kaşarlı patates.",
                "Patates", "Kaşar peyniri", "Krema", "Tuz", "Karabiber");

        addRecipe(recipes, "Nohutlu Pirinç Pilavı",
                "Nohutlu klasik pilav.",
                "Pirinç", "Haşlanmış nohut", "Tereyağı", "Tuz");

        addRecipe(recipes, "Ev Burgeri",
                "Ev yapımı burger.",
                "Burger ekmeği", "Köfte", "Marul", "Domates", "Kaşar peyniri");

        addRecipe(recipes, "Tavuklu Wrap",
                "Lavaşla pratik tavuklu wrap.",
                "Lavaş", "Tavuk göğsü", "Marul", "Domates", "Yoğurt sosu");

        addRecipe(recipes, "Kremalı Mantarlı Makarna",
                "Kremalı mantarlı doyurucu makarna.",
                "Makarna", "Mantar", "Krema", "Sarımsak", "Parmesan");

        addRecipe(recipes, "Fırında Tavuk ve Sebze",
                "Tavuk ve sebzelerle fırın yemeği.",
                "Tavuk", "Patates", "Havuç", "Soğan", "Tuz", "Karabiber");

        addRecipe(recipes, "Menemen",
                "Domates, biber ve yumurtayla kahvaltılık.",
                "Domates", "Biber", "Yumurta", "Sucuk", "Kaşar peyniri");

        addRecipe(recipes, "Köri Soslu Tavuk",
                "Kremalı köri soslu tavuk.",
                "Tavuk göğsü", "Krema", "Köri", "Sarımsak", "Tereyağı");

        addRecipe(recipes, "Karnıyarık",
                "Patlıcan ve kıymayla klasik yemek.",
                "Patlıcan", "Kıyma", "Domates", "Biber", "Soğan");

        addRecipe(recipes, "Lazanya",
                "Fırında kaşarlı lazanya.",
                "Lazanya yaprağı", "Kıyma", "Domates sosu", "Beşamel sos", "Kaşar peyniri");

        addRecipe(recipes, "Sütlaç",
                "Sütlü hafif tatlı.",
                "Süt", "Pirinç", "Şeker", "Nişasta");

        addRecipe(recipes, "Revani",
                "Yoğurtlu irmikli şerbetli tatlı.",
                "Yumurta", "Şeker", "Yoğurt", "İrmik", "Un");

        addRecipe(recipes, "Mozaik Pasta",
                "Bisküvili kolay pasta.",
                "Petibör bisküvi", "Süt", "Kakao", "Tereyağı");

        addRecipe(recipes, "İrmik Helvası",
                "Klasik irmik helvası.",
                "İrmik", "Tereyağı", "Şeker", "Süt");

        addRecipe(recipes, "Magnolia",
                "Meyveli bisküvili kuplu tatlı.",
                "Süt", "Un", "Krema", "Muz", "Çilek", "Bisküvi");

        return recipes;
    }

    private void addRecipe(ArrayList<Recipe> recipes, String title, String description, String... ingredients) {
        ArrayList<String> ingredientList = new ArrayList<>();

        for (String ingredient : ingredients) {
            ingredientList.add(ingredient);
        }

        recipes.add(new Recipe(title, description, ingredientList));
    }
    public void getSelectedIngredientNames(OnIngredientsLoadedListener listener) {
        db.collection("selectedIngredients")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<String> selectedNames = new ArrayList<>();

                    queryDocumentSnapshots.forEach(document -> {
                        String name = document.getString("name");

                        if (name != null) {
                            selectedNames.add(name);
                        }
                    });

                    listener.onLoaded(selectedNames);
                })
                .addOnFailureListener(e -> {
                    listener.onLoaded(new ArrayList<>());
                });
}
    public void clearSelectedIngredients() {
        db.collection("selectedIngredients")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete();
                    }
                });
    }
    public interface OnIngredientsLoadedListener {
        void onLoaded(ArrayList<String> selectedNames);
    }
    public void saveShoppingList(String recipeName, ArrayList<String> missingIngredients) {

        db.collection("shoppingList")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    ArrayList<String> existingItems = new ArrayList<>();

                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        String name = document.getString("name");

                        if (name != null) {
                            existingItems.add(name);
                        }
                    }

                    for (String item : missingIngredients) {

                        if (!existingItems.contains(item)) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("recipeName", recipeName);
                            data.put("name", item);

                            db.collection("shoppingList").add(data);
                        }
                    }
                });
    }
    public void getShoppingList(OnShoppingListLoadedListener listener) {
        db.collection("shoppingList")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<ShoppingItem> list = new ArrayList<>();

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId();
                        String name = document.getString("name");
                        String recipeName = document.getString("recipeName");

                        if (name != null && recipeName != null) {
                            list.add(new ShoppingItem(id, name, recipeName));
                        }
                    }

                    listener.onLoaded(list);
                })
                .addOnFailureListener(e -> listener.onLoaded(new ArrayList<>()));
    }

    public void deleteShoppingItems(ArrayList<String> ids, OnSimpleCompleteListener listener) {
        for (String id : ids) {
            db.collection("shoppingList").document(id).delete();
        }

        listener.onComplete();
    }

    public void clearShoppingList(OnSimpleCompleteListener listener) {
        db.collection("shoppingList")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete();
                    }

                    listener.onComplete();
                });
    }

    public interface OnShoppingListLoadedListener {
        void onLoaded(ArrayList<ShoppingItem> list);
    }

    public interface OnSimpleCompleteListener {
        void onComplete();
    }
}

