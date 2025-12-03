package com.chekizybra.otzovichok.database;

public class Comment {
    public int id;
    public int user_id;
    public int category_id;
    public String title;
    public String comment;
    public Integer value;
    public Integer grade;
    public Integer plus_grade;
    public Integer minus_grade;
    public String post_date;   // Supabase отдаёт date как строку "2025-12-03"
}