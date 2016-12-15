package com.example.butterfly.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class StudentsProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.butterfly.contentprovider.StudentsProvider";
    public static final String PATH_GROUPS = "GroupsList";
    public static final String PATH_GROUPS_G = PATH_GROUPS + "/g";
    public static final String PATH_STUDENTS = "StudentsList";
    public static final String PATH_STUDENTS_G = PATH_STUDENTS + "/g";
    public static final String PATH_STUDENTS_G_S = PATH_STUDENTS_G + "/s";
    private static final int GROUPS_ID = 1;
    private static final int GROUPS_G_ID = 2;
    private static final int STUDENTS_G_ID = 3;
    private static final int STUDENTS_G_S_ID = 4;
    private static final int GROUPS = 5;
    private static final int STUDENTS_ID = 6;

    private static final UriMatcher URI_MATCHER;
    static{
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, PATH_GROUPS, GROUPS_ID);
        URI_MATCHER.addURI(AUTHORITY, PATH_GROUPS_G + "/#", GROUPS_G_ID);
        URI_MATCHER.addURI(AUTHORITY, PATH_STUDENTS_G + "/#", STUDENTS_G_ID);
        URI_MATCHER.addURI(AUTHORITY, PATH_STUDENTS_G_S + "/#", STUDENTS_G_S_ID);
        URI_MATCHER.addURI(AUTHORITY, PATH_STUDENTS, GROUPS);
        URI_MATCHER.addURI(AUTHORITY, PATH_STUDENTS + "/#", STUDENTS_ID);
    }

    MyDBClass dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        dbHelper = new MyDBClass(getContext(), "STUDENTSDB.db", null, 1);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor;
        switch (URI_MATCHER.match(uri)) {
            case GROUPS_ID:
                cursor = db.rawQuery("SELECT g.IDgroup, g.Head, COALESCE(s.col, 0) " +
                        "FROM STUDGROUPS as g " +
                        "LEFT JOIN " +
                        "(SELECT STUDENTS.IDgroup, Count(STUDENTS.Name) col " +
                        "FROM STUDENTS GROUP BY IDgroup) AS s ON s.IDgroup = g.IDgroup", null);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        Uri.parse("content://" + AUTHORITY + "/" + PATH_GROUPS));
                return cursor;
            case GROUPS_G_ID:
                String selectGroupsG = uri.getLastPathSegment();
                cursor = db.rawQuery("SELECT g.IDgroup, g.Head, COALESCE(s.col, 0) " +
                        "FROM STUDGROUPS as g " +
                        "LEFT JOIN " +
                        "(SELECT STUDENTS.IDgroup, Count(STUDENTS.Name) col " +
                        "FROM STUDENTS GROUP BY IDgroup) AS s ON s.IDgroup = g.IDgroup " +
                        "WHERE g.IDgroup = " + selectGroupsG, null);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        Uri.parse("content://" + AUTHORITY + "/" + PATH_GROUPS_G));
                return cursor;
            case STUDENTS_G_ID:
                String selectStudentsG = uri.getLastPathSegment();
                cursor = db.rawQuery("SELECT IDgroup, IDstudent, Name " +
                        "FROM STUDENTS " +
                        "WHERE IDgroup = " + selectStudentsG, null);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        Uri.parse("content://" + AUTHORITY + "/" + PATH_STUDENTS_G));
                return cursor;
            case STUDENTS_G_S_ID:
                String selectStudentsGS = uri.getLastPathSegment();
                cursor = db.rawQuery("SELECT g.IDgroup, g.Name, s.IDstudent, s.Name " +
                        "FROM STUDENTS AS s " +
                        "JOIN STUDGROUPS g ON g.IDgroup = s.IDgroup " +
                        "WHERE s.IDstudent = " + selectStudentsGS, null);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        Uri.parse("content://" + AUTHORITY + "/" + PATH_STUDENTS_G_S));
                return cursor;
            case GROUPS:
                cursor = db.rawQuery("SELECT IDgroup FROM STUDGROUPS", null);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        Uri.parse("content://" + AUTHORITY + "/" + PATH_STUDENTS));
                return cursor;
            case STUDENTS_ID:
                String selectStudents = uri.getLastPathSegment();
                cursor = db.rawQuery("SELECT g.IDgroup, g.Faculty, g.Course, g.Name, g.Head, " +
                        "s.IDstudent, s.Name FROM STUDGROUPS AS g " +
                        "LEFT JOIN STUDENTS AS s ON g.IDgroup = s.IDgroup " +
                        "WHERE g.IDgroup = " + selectStudents, null);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        Uri.parse("content://" + AUTHORITY +"/" + PATH_STUDENTS));
                return cursor;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        db = dbHelper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case GROUPS_ID:
                long GroupRowID = db.insert("STUDGROUPS", null, contentValues);
                Uri uriGroups = ContentUris.withAppendedId(Uri.parse("content://" + AUTHORITY + "/" + PATH_GROUPS_G), GroupRowID);
                getContext().getContentResolver().notifyChange(uriGroups, null);
                return uriGroups;
            case STUDENTS_G_ID:
                long StudentRowID = db.insert("STUDENTS", null, contentValues);
                Uri uriStudents = ContentUris.withAppendedId(Uri.parse("content://" + AUTHORITY + "/" + PATH_STUDENTS_G), StudentRowID);
                getContext().getContentResolver().notifyChange(uriStudents, null);
                return uriStudents;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        switch (URI_MATCHER.match(uri)) {
            case GROUPS_ID:
                int deleteGroups = db.delete("STUDGROUPS", null, null);
                getContext().getContentResolver().notifyChange(uri, null);
                return deleteGroups;
            case GROUPS_G_ID:
                String selectGroupsG = uri.getLastPathSegment();
                int deleteGroupsG = db.delete("STUDGROUPS", "IDgroup = ?", new String[]{selectGroupsG});
                getContext().getContentResolver().notifyChange(uri, null);
                return deleteGroupsG;
            case STUDENTS_G_ID:
                String selectStudentsG = uri.getLastPathSegment();
                int deleteStudentsG = db.delete("STUDENTS", "IDgroup = ?", new String[]{selectStudentsG});
                getContext().getContentResolver().notifyChange(uri, null);
                return deleteStudentsG;
            case STUDENTS_G_S_ID:
                String selectStudentsGS = uri.getLastPathSegment();
                int deleteStudentsGS = db.delete("STUDENTS", "IDstudent = ?", new String[]{selectStudentsGS});
                getContext().getContentResolver().notifyChange(uri, null);
                return deleteStudentsGS;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        switch (URI_MATCHER.match(uri))
        {
            case GROUPS_G_ID:
                String selectGroupsG = uri.getLastPathSegment();
                int updateGroupsG = db.update("STUDGROUPS",contentValues, "IDgroup = ?",
                        new String[]{selectGroupsG});
                getContext().getContentResolver().notifyChange(uri, null);
                return updateGroupsG;
            case STUDENTS_G_S_ID:
                String selectStudentGS = uri.getLastPathSegment();
                int updateStudentsGS = db.update("STUDENTS", contentValues, "IDstudent = ?",
                        new String[]{selectStudentGS});
                getContext().getContentResolver().notifyChange(uri, null);
                return updateStudentsGS;
            default: throw new IllegalArgumentException("Wrong uri: " + uri);
        }
    }
}
