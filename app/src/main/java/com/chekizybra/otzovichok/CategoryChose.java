package com.chekizybra.otzovichok;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chekizybra.otzovichok.database.BdConnect;
import com.chekizybra.otzovichok.database.Category;
import com.chekizybra.otzovichok.database.Comment;
import com.chekizybra.otzovichok.database.Zaprosi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryChose extends AppCompatActivity {
    int stoper = 0;
    AutoCompleteTextView mainCategoryField;
    android.widget.EditText productNameField;
    Button addMainCategoryBtn, backBtn, applyBtn;
    Button addSubCategoryRowBtn;
    LinearLayout subCategoriesContainer;

    String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd4eGhodnB5bG5pdWl3Z2Vyc2x0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM1NTgzNzQsImV4cCI6MjA3OTEzNDM3NH0.ywOjtXlQZP-llJUCYnm8RSl2AiN0Dh6zE6Dg6vzFm1Y";

    ArrayAdapter<String> mainCategoryAdapter;
    List<String> mainCategories = new ArrayList<>();

    // Список для хранения всех категорий из базы
    private List<Category> allCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chose);

        mainCategoryField = findViewById(R.id.mainCategoryField);
        productNameField = findViewById(R.id.productNameField);
        addMainCategoryBtn = findViewById(R.id.addMainCategoryBtn);
        addSubCategoryRowBtn = findViewById(R.id.addSubCategoryRow);
        backBtn = findViewById(R.id.backBtn);
        applyBtn = findViewById(R.id.applyBtn);
        subCategoriesContainer = findViewById(R.id.subCategoriesContainer);

        mainCategoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, mainCategories);
        mainCategoryField.setAdapter(mainCategoryAdapter);

        loadAllCategories();

        mainCategoryField.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) mainCategoryField.showDropDown();
        });

        addMainCategoryBtn.setOnClickListener(v -> {
            String text = mainCategoryField.getText().toString().trim();
            if (!text.isEmpty()) addCategoryToDB(text, null);
            else Toast.makeText(this, "Введите название категории", Toast.LENGTH_SHORT).show();
        });

        addSubCategoryRowBtn.setOnClickListener(v -> {
            if (stoper < 5) addSubCategoryRow();
            else Toast.makeText(this, "Максимальное количество подкатегорий", Toast.LENGTH_SHORT).show();
        });

        backBtn.setOnClickListener(v -> finish());

        applyBtn.setOnClickListener(v -> {
            String productName = productNameField.getText().toString().trim();
            if (productName.isEmpty()) {
                Toast.makeText(this, "Введите наименование товара", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer lastCategoryId = getLastCategoryId();

            if (lastCategoryId == null) {
                Toast.makeText(this, "Не выбрана конечная категория", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(CategoryChose.this, WriteComment.class);
            intent.putExtra("product_name", productName);
            intent.putExtra("category_id", lastCategoryId);
            startActivity(intent);
        });


    }

    private Integer getLastCategoryId() {
        int rowCount = subCategoriesContainer.getChildCount();

        if (rowCount == 0) {
            // Если нет подкатегорий, берем ID главной категории
            String mainCatName = mainCategoryField.getText().toString().trim();
            if (mainCatName.isEmpty()) return null;

            for (Category cat : allCategories) {
                if (cat.category.equals(mainCatName)) {
                    return cat.id;
                }
            }
            return null;
        } else {
            // Берем ID из последней строки
            View lastRow = subCategoriesContainer.getChildAt(rowCount - 1);
            AutoCompleteTextView lastField = lastRow.findViewById(R.id.subCategory);
            String lastCategoryName = lastField.getText().toString().trim();

            if (lastCategoryName.isEmpty()) return null;

            for (Category cat : allCategories) {
                if (cat.category.equals(lastCategoryName)) {
                    return cat.id;
                }
            }
            return null;
        }
    }

    private void loadAllCategories() {
        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);
        api.getCategories("Bearer " + apiKey, apiKey, null).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allCategories = response.body();
                    updateMainCategories();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(CategoryChose.this, "Ошибка загрузки категорий", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMainCategories() {
        mainCategories.clear();
        for (Category c : allCategories) {
            if (c.parrent_id == null) mainCategories.add(c.category);
        }
        mainCategoryAdapter.notifyDataSetChanged();
    }

    private void addSubCategoryRow() {
        View row = LayoutInflater.from(this).inflate(R.layout.sub_category_row, null);
        AutoCompleteTextView subField = row.findViewById(R.id.subCategory);
        Button addBtn = row.findViewById(R.id.addSubBtn);

        // Определяем parentId для этой строки
        Integer parentId = findParentIdForNewRow();

        // Загружаем категории для этого parentId
        loadCategoriesForParentId(subField, parentId);

        addBtn.setOnClickListener(v -> {
            String subCategoryName = subField.getText().toString().trim();
            if (subCategoryName.isEmpty()) {
                Toast.makeText(this, "Введите название подкатегории", Toast.LENGTH_SHORT).show();
                return;
            }

            if (parentId == null) {
                Toast.makeText(this, "Сначала выберите главную категорию", Toast.LENGTH_SHORT).show();
                return;
            }

            // Добавляем категорию с найденным parentId
            addCategoryToDB(subCategoryName, parentId, newCategoryId -> {
                if (newCategoryId != null) {
                    Toast.makeText(this, "Категория добавлена", Toast.LENGTH_SHORT).show();
                    loadAllCategories(); // Обновляем все категории
                }
            });
        });

        subCategoriesContainer.addView(row);
        stoper++;
    }

    private Integer findParentIdForNewRow() {
        int rowCount = subCategoriesContainer.getChildCount();

        if (rowCount == 0) {
            // Первая строка - берем ID из главной категории
            String mainCatName = mainCategoryField.getText().toString().trim();
            if (mainCatName.isEmpty()) return null;

            // Ищем ID главной категории
            for (Category cat : allCategories) {
                if (cat.category.equals(mainCatName)) {
                    return cat.id;
                }
            }
            return null;
        } else {
            // Берем категорию из предыдущей строки
            View prevRow = subCategoriesContainer.getChildAt(rowCount - 1);
            AutoCompleteTextView prevField = prevRow.findViewById(R.id.subCategory);
            String prevCategoryName = prevField.getText().toString().trim();

            if (prevCategoryName.isEmpty()) return null;

            // Ищем ID категории из предыдущей строки
            for (Category cat : allCategories) {
                if (cat.category.equals(prevCategoryName)) {
                    return cat.id;
                }
            }
            return null;
        }
    }

    private void loadCategoriesForParentId(AutoCompleteTextView field, Integer parentId) {
        List<String> categories = new ArrayList<>();

        if (parentId == null) {
            // Для главной категории показываем все категории без parent_id
            for (Category cat : allCategories) {
                if (cat.parrent_id == null) {
                    categories.add(cat.category);
                }
            }
        } else {
            // Для подкатегорий показываем категории с нужным parent_id
            for (Category cat : allCategories) {
                if (cat.parrent_id != null && cat.parrent_id.equals(parentId)) {
                    categories.add(cat.category);
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                categories
        );
        field.setAdapter(adapter);

        field.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) field.showDropDown();
        });
    }

    private void addCategoryToDB(String categoryName, Integer parentId, CategoryIdCallback callback) {
        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);
        Map<String, String> body = new HashMap<>();
        body.put("category", categoryName);
        if (parentId != null) body.put("parrent_id", parentId.toString());

        api.addCategory("Bearer " + apiKey, apiKey, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // После добавления обновляем список категорий
                    loadAllCategories();
                    if (callback != null) {
                        // Ищем ID новой категории
                        for (Category cat : allCategories) {
                            if (cat.category.equals(categoryName) &&
                                    ((parentId == null && cat.parrent_id == null) ||
                                            (parentId != null && parentId.equals(cat.parrent_id)))) {
                                callback.onResult(cat.id);
                                break;
                            }
                        }
                    }
                    Toast.makeText(CategoryChose.this, "Категория добавлена", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CategoryChose.this, "Ошибка добавления категории", Toast.LENGTH_SHORT).show();
                    if (callback != null) callback.onResult(null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CategoryChose.this, "Сетевая ошибка", Toast.LENGTH_SHORT).show();
                if (callback != null) callback.onResult(null);
            }
        });
    }

    private void addCategoryToDB(String categoryName, Integer parentId) {
        addCategoryToDB(categoryName, parentId, null);
    }

    private interface CategoryIdCallback {
        void onResult(Integer id);
    }
}