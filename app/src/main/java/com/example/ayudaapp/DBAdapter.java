package com.example.ayudaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class DBAdapter {

    myDbHelper myhelper;

    public DBAdapter(Context context) {
        myhelper = new myDbHelper(context);
    }


    static class myDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "AyudaProjectDB";    // Database Name
        private static final int DATABASE_Version = 13;    // Database Version
        private final Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(
                        "Create Table Region(" +
                                "RegionID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                "RegionName VARCHAR(255) NOT NULL" +
                                "); "
                );
                db.execSQL(
                        "Create Table City(" +
                                "CityID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                                "RegionID INTEGER NOT NULL," +
                                "CityName VARCHAR(50) NOT NULL," +
                                "Foreign key (RegionID) references Region(RegionID) " +
                                ");"
                );
                db.execSQL(
                        "Create Table Barangay(" +
                                "BarangayID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                "CityID INTEGER NOT NULL," +
                                "BarangayName VARCHAR(50) NOT NULL," +
                                "Foreign key (CityID) references City(CityID) " +
                                ");"
                );
                // FAMILY ID GENERATED SHOULD BE 8 NUMERICAL SERIES
                db.execSQL(
                        "Create Table Family(" +
                                "FamilyID VARCHAR(255) PRIMARY KEY UNIQUE NOT NULL," +
                                "BarangayID NOT NULL," +
                                "FamilyName VARCHAR(50) NOT NULL, " +
                                "HouseNo VARCHAR(255) NOT NULL," +
                                "Subdivision VARCHAR(255)," +
                                "StreetName VARCHAR(255) NOT NULL," +
                                "DonatedStatus INTEGER NOT NULL," +
                                "Foreign key (BarangayID) references Barangay(BarangayID) " +
                                ");"
                );
                db.execSQL(
                        "Create Table Person(" +
                                "accountID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                "FamilyID VARCHAR(255) NOT NULL," +
                                "FirstName VARCHAR(255) NOT NULL," +
                                "MiddleName VARCHAR(255) NOT NULL," +
                                "LastName VARCHAR(255) NOT NULL," +
                                "Suffix VARCHAR(255)," +
                                "username VARCHAR(255) UNIQUE NOT NULL," +
                                "password VARCHAR(255) NOT NULL," +
                                "phoneNo VARCHAR(13) NOT NULL," +
                                "Foreign key (FamilyID) references Family(FamilyID) " +
                                "); "
                );
                db.execSQL(
                        "Create Table adminAccount(" +
                                "AdminID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                "Username VARCHAR(100) unique NOT NULL," +
                                "Password VARCHAR(100) NOT NULL," +
                                "FirstName VARCHAR(50) NOT NULL," +
                                "MiddleName VARCHAR(50) NOT NULL," +
                                "LastName VARCHAR(50) NOT NULL," +
                                "Suffix VARCHAR(50)" +
                                ");"
                );
                db.execSQL(
                        "Create Table covidcases( " +
                                "case_month VARCHAR(255) NOT NULL," +
                                "case_day VARCHAR(255) NOT NULL," +
                                "case_year VARCHAR(255) NOT NULL," +
                                "case_count INTEGER NOT NULL " +
                                ");"
                );
                db.execSQL(
                        "Create Table newsannouncements(" +
                                "news VARCHAR(255) NOT NULL," +
                                "news_auth_Fname VARCHAR(255) NOT NULL," +
                                "news_auth_Mname VARCHAR(255) NOT NULL," +
                                "news_auth_Lname VARCHAR(255) NOT NULL," +
                                "news_auth_Suffix VARCHAR(255)," +
                                "news_published DATE NOT NULL" +
                                ");"
                );
                db.execSQL(
                        "Create Table historylog(" +
                                "historyrecord VARCHAR(255) NOT NULL," +
                                "familyID INTEGER NOT NULL," +
                                "donationDate DATE NOT NULL," +
                                "Foreign key (familyID) references Family(FamilyID) " +
                                ");"
                );
                Toast.makeText(context, "DB created", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Toast.makeText(context, "OnUpgrade", Toast.LENGTH_LONG).show();
                db.execSQL("DROP TABLE IF EXISTS Region;");
                db.execSQL("DROP TABLE IF EXISTS City;");
                db.execSQL("DROP TABLE IF EXISTS Barangay;");
                db.execSQL("DROP TABLE IF EXISTS Family;");
                db.execSQL("DROP TABLE IF EXISTS Person;");
                db.execSQL("DROP TABLE IF EXISTS adminAccount;");
                db.execSQL("DROP TABLE IF EXISTS covidcases;");
                db.execSQL("DROP TABLE IF EXISTS newsannouncements;");
                db.execSQL("DROP TABLE IF EXISTS historylog;");
                onCreate(db);
            } catch (Exception e) {
                Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show();
            }
        }

    }

    public boolean checkDonatedStatus(String familyID) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] temp = {familyID};
        Cursor cursor = db.rawQuery(
                "SELECT DonatedStatus from Family where FamilyID = ?;"
                , temp);
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("DonatedStatus")).equals("0")) {
                return true;
            }
        }
        return false;
    }

    public String getFamilySurname(String s) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] temp = {s};
        Cursor cursor = db.rawQuery(
                "SELECT FamilyName from Family where FamilyID = ?;"
                , temp);
        String value="";
        while (cursor.moveToNext()) {
            value=cursor.getString(cursor.getColumnIndex("FamilyName"));
        }
        return value;
    }

    public void loadData() {
        loadRegion();
        loadCity();
        loadBarangay();
        loadFamily();
        loadPerson();
    }

    public long loadRegion(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String [] regions = {"NCR","Region II","Region IV"} ;
        long id =0;
        for (String x: regions) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("RegionName",x);
            id = db.insert("Region", null, contentValues);
        }
        return id;
    }

    public long loadCity(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String [] RegionID = {"1","1","2","3"} ;
        String [] City = {"Pasig","Marikina","Tuguegarao","Davao"} ;
        long id =0;
        for (int i = 0; i<RegionID.length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("RegionID",RegionID[i]);
            contentValues.put("CityName",City[i]);
            id = db.insert("City", null, contentValues);
        }
        return id;

    }

    public long loadBarangay(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String [] CityID= {"1","2","3","4"} ;
        String [] Barangay = {"Kapitolyo","Calumpang","Buntun","Poblacion"} ;
        long id =0;
        for (int i = 0; i<CityID.length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("CityID",CityID[i]);
            contentValues.put("BarangayName",Barangay[i]);
            id = db.insert("Barangay", null, contentValues);
        }
        return id;
    }

    public long loadFamily(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String [] FamilyID = {"SAMPFAM1","SAMPFAM2","SAMPFAM3","SAMPFAM4"};
        String [] BarangayID = {"1","2","3","4"} ;
        String [] FamilyName = {"Hart","Ramos","Collins","Duterte"} ;
        String [] HouseNo = {"1234","3456","5678","9012"} ;
        String [] Subdivision = {"Greenwoods","Heights","Mercedes","Camella"} ;
        String [] StreetName = {"Mango","Swamp","Doging","Xi Jinping"} ;
        long id =0;
        for (int i = 0; i<FamilyID.length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("FamilyID",FamilyID[i]);
            contentValues.put("BarangayID",BarangayID[i]);
            contentValues.put("FamilyName",FamilyName[i]);
            contentValues.put("HouseNo",HouseNo[i]);
            contentValues.put("Subdivision",Subdivision[i]);
            contentValues.put("StreetName",StreetName[i]);
            contentValues.put("DonatedStatus","0");
            id = db.insert("Family", null, contentValues);
        }
        return id;

    }

    public long loadPerson(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String [] FamilyID = {"SAMPFAM1","SAMPFAM2","SAMPFAM3","SAMPFAM4"};
        long id =0;
        for (int i = 0; i<FamilyID.length; i++) {
            if(i==0){ //Hart
                for (int j =0; j<4; j++){
                    String FirstName [] = {"Duncan", "John", "Danny", "Bruno"};
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("FamilyID",FamilyID[i]);
                    contentValues.put("FirstName",FirstName[j]);
                    contentValues.put("MiddleName","Cooke");
                    contentValues.put("LastName","Hart");
                    contentValues.put("username","Cooke"+(j+1));
                    contentValues.put("password","Hart"+(j+1));
                    contentValues.put("phoneNo","09175741973");
                    id = db.insert("Person", null, contentValues);
                }
            }else if(i==1){ //Ramos
                for (int j =0; j<4; j++){
                    String FirstName [] = {"Fiona", "Shrek", "Donkey", "Dragon"};
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("FamilyID",FamilyID[i]);
                    contentValues.put("FirstName",FirstName[j]);
                    contentValues.put("MiddleName","Pope");
                    contentValues.put("LastName","Ramos");
                    contentValues.put("username","Pope"+(j+1));
                    contentValues.put("password","Ramos"+(j+1));
                    contentValues.put("phoneNo","09058367374");
                    id = db.insert("Person", null, contentValues);
                }
            }else if(i==2){ //Collins
                for (int j =0; j<4; j++){
                    String FirstName [] = {"Carl", "Mark", "Simon", "Ken"};
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("FamilyID",FamilyID[i]);
                    contentValues.put("FirstName",FirstName[j]);
                    contentValues.put("MiddleName","Wells");
                    contentValues.put("LastName","Collins");
                    contentValues.put("username","Wells"+(j+1));
                    contentValues.put("password","Collins"+(j+1));
                    contentValues.put("phoneNo","09993323894");
                    id = db.insert("Person", null, contentValues);
                }
            }else{ // Duterte
                for (int j =0; j<4; j++){
                    String FirstName [] = {"Rodrigo", "Sara", "Paolo", "Kitty"};
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("FamilyID",FamilyID[i]);
                    contentValues.put("FirstName",FirstName[j]);
                    contentValues.put("MiddleName","Roa");
                    contentValues.put("LastName","Duterte");
                    contentValues.put("username","Roa"+(j+1));
                    contentValues.put("password","Duterte"+(j+1));
                    contentValues.put("phoneNo","09293199016");
                    id = db.insert("Person", null, contentValues);
                }
            }
        }
        return id;
    }

    public ArrayList<String> getAdminName(String username, String password) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] temp = {username, password};
        ArrayList<String> returnable = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "SELECT * from adminAccount where Username = ? AND Password = ?;"
                , temp);
        String fname = "";
        String mname = "";
        String lname = "";
        String sname = "";
        while (cursor.moveToNext()) {
            fname = cursor.getString(cursor.getColumnIndex("FirstName"));
            mname = cursor.getString(cursor.getColumnIndex("MiddleName"));
            lname = cursor.getString(cursor.getColumnIndex("LastName"));
            sname = cursor.getString(cursor.getColumnIndex("Suffix"));
            returnable.add(fname);
            returnable.add(mname);
            returnable.add(lname);
            returnable.add(sname);
        }
        return returnable;
    }

    public long initialAdminAccount() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username", "admin_username");
        contentValues.put("Password", "sample123");
        contentValues.put("FirstName", "Rommel");
        contentValues.put("MiddleName", "Hernandez");
        contentValues.put("LastName", "Urbano");
        contentValues.put("Suffix", "Jr.");
        long id = db.insert("adminAccount", null, contentValues);
        return id;
    }

    /**
     * returns the Admin Credentials and is dual purpose for login verification,
     * if ArrayList returns no values, the login verification is terminated
     *
     * @param username
     * @param password
     * @return
     */
    public ArrayList<String[]> adminlogin(String username, String password) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ArrayList<String[]> records = new ArrayList<>();
        String data[] = {username, password};

        Cursor cursor = db.rawQuery(
                "Select * from adminAccount where Username =? AND Password = ?"
                , data);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("AdminID"));
            String FName = cursor.getString(cursor.getColumnIndex("FirstName"));
            String MName = cursor.getString(cursor.getColumnIndex("MiddleName"));
            String LName = cursor.getString(cursor.getColumnIndex("LastName"));
            String Suffix = cursor.getString(cursor.getColumnIndex("Suffix"));
            String array[] = {id, FName, MName, LName, Suffix};
            records.add(array);
        }
        return records;
    }

    /**
     * Invoked when donation function is triggered, sets the DonatedStatus of the Family to 1
     *
     * @param id
     * @return
     */
    public int updateFamilyDonationStatus(String id) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DonatedStatus", 1);
        String[] whereArgs = {id};
        int count = db.update("Family", contentValues, "FamilyID = ?", whereArgs);
        return count;
    }

    public int resetFamilyDonationStatus() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DonatedStatus", 0);
        int count = db.update("Family", contentValues, null, null);
        return count;
    }

    /**
     * Donation function for the Ayuda Giving
     *
     * @param message
     * @param familyID
     * @return
     */
    public long insertAyudaDonation(String message, String familyID) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT DATE('now','localtime') as date;"
                , null);
        String date = "";
        while (cursor.moveToNext()) {
            date = cursor.getString(cursor.getColumnIndex("date"));
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("historyrecord", message);
        contentValues.put("familyID", familyID);
        contentValues.put("donationDate", date);
        long id = db.insert("historylog", null, contentValues);
        return id;
    }

    /**
     * Inserting News / Announcement of current day along with the post author
     * returns long value for validation
     *
     * @param message
     * @param Fname
     * @param MName
     * @param LName
     * @param Suffix
     * @return
     */
    public long insertNewsAnnouncement(String message, String Fname, String MName, String LName, String Suffix) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT DATE('now','localtime') as date;"
                , null);
        String date = "";
        while (cursor.moveToNext()) {
            date = cursor.getString(cursor.getColumnIndex("date"));
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("news", message);
        contentValues.put("news_auth_Fname", Fname);
        contentValues.put("news_auth_Mname", MName);
        contentValues.put("news_auth_Lname", LName);
        contentValues.put("news_auth_Suffix", Suffix);
        contentValues.put("news_published", date);
        long id = db.insert("newsannouncements", null, contentValues);
        return id;
    }

    /**
     * Inserting Covid Case of current day
     * returns long value for validation
     *
     * @param number
     * @return
     */
    public long insertCovidCase(String number) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT DATE('now','localtime') as date;"
                , null);
        String date = "";
        while (cursor.moveToNext()) {
            date = cursor.getString(cursor.getColumnIndex("date"));
        }
        String temp[] = date.split("-");
        ContentValues contentValues = new ContentValues();
        contentValues.put("case_count", number);
        contentValues.put("case_year", temp[0]);
        contentValues.put("case_month", temp[1]);
        contentValues.put("case_day", temp[2]);
        long id = db.insert("covidcases", null, contentValues);
        return id;
    }

    /**
     * Returns Phone Number of Person for OTP Verification
     *
     * @param PersonID
     * @return
     */
    public String returnContactNo(String PersonID) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String temp[] = {PersonID};
        Cursor cursor = db.rawQuery(
                "Select phoneNo from Person where accountID = ?"
                , temp);
        String number = "";
        while (cursor.moveToNext()) {
            number = cursor.getString(cursor.getColumnIndex("phoneNo"));
        }
        return number;
    }

    /**
     * Checks if the generated Family ID is already taken
     * <p>
     * Returns true if the generated Family ID is already taken, else false
     *
     * @param generated
     * @return
     */
    public boolean checkFamilyIDExists(String generated) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "Select Family.FamilyID as id from Family INNER JOIN Person on Family.FamilyID=Person.FamilyID"
                , null);
        String id = "";
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex("id"));
            if (id.equals(generated)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates random OTP code
     *
     * @return
     */
    public String generateID() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)));
        for (int i = 0; i < 7; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);
        if (checkFamilyIDExists(sb.toString())) {
            generateID();
        }
        return sb.toString();
    }

    /**
     * returns family reference ID, and is also re-purposed as the login verifier
     * if no reference ID is returned, the login process is terminated.
     *
     * @param username
     * @param password
     * @return
     */
    public String getFamilyReferenceID(String username, String password) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String data[] = {username, password};
        Cursor cursor = db.rawQuery(
                "Select Family.FamilyID as id, Person.accountID as person_id from " +
                        "Family INNER JOIN Person on Family.FamilyID=Person.FamilyID " +
                        "where Person.username =? AND Person.password = ?"
                , data);
        String familyID = "";
        String PersonID = "";
        while (cursor.moveToNext()) {
            familyID = cursor.getString(cursor.getColumnIndex("id"));
            PersonID=cursor.getString(cursor.getColumnIndex("person_id"));
        }
        return familyID+"-"+PersonID;
    }

    public ArrayList<String[]> getNews() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from newsannouncements Order by news_published DESC",null);

        ArrayList<String[]> temp = new ArrayList<>();
        while (cursor.moveToNext()) {
            String values[] = new String[6];
            if(cursor.getString(cursor.getColumnIndex("news_auth_Fname"))==null){
                break;
            }
            values[0] = cursor.getString(cursor.getColumnIndex("news_auth_Fname")); //Headline
            values[1] = cursor.getString(cursor.getColumnIndex("news_auth_Mname")); //Source
            values[2] = cursor.getString(cursor.getColumnIndex("news_auth_Lname")); //Author Full Name
            values[3] = cursor.getString(cursor.getColumnIndex("news_auth_Suffix")); //Link
            values[4] = cursor.getString(cursor.getColumnIndex("news")); //Content/Preview
            values[5] = cursor.getString(cursor.getColumnIndex("news_published")); //Date Published
            temp.add(values);
        }
        return temp;
    }

    public ArrayList<String[]> covidDaily() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from covidcases",null);

        ArrayList<String[]> temp = new ArrayList<>();
        while (cursor.moveToNext()) {
            String values[] = new String[4];
            values[0] = cursor.getString(cursor.getColumnIndex("case_month"));
            values[1] = cursor.getString(cursor.getColumnIndex("case_day"));
            values[2] = cursor.getString(cursor.getColumnIndex("case_year"));
            values[3] = cursor.getString(cursor.getColumnIndex("case_count"));
            temp.add(values);
        }
        return temp;
    }

    public ArrayList<String[]> covidMonthly() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select case_month, case_year, SUM(case_count) as count from covidcases" +
                " Group By case_month, case_year",null);

        ArrayList<String[]> temp = new ArrayList<>();
        while (cursor.moveToNext()) {
            String values[] = new String[3];
            values[0] = cursor.getString(cursor.getColumnIndex("case_month"));
            values[1] = cursor.getString(cursor.getColumnIndex("case_year"));
            values[2] = cursor.getString(cursor.getColumnIndex("count"));
            temp.add(values);
        }
        return temp;
    }

    public ArrayList<String[]> covidYearly() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select case_year, SUM(case_count) as count from covidcases" +
                " Group By case_year",null);

        ArrayList<String[]> temp = new ArrayList<>();
        while (cursor.moveToNext()) {
            String values[] = new String[2];
            values[0] = cursor.getString(cursor.getColumnIndex("case_year"));
            values[1] = cursor.getString(cursor.getColumnIndex("count"));
            temp.add(values);
        }
        return temp;
    }

    public int getDailyCovid(){
        SQLiteDatabase db = myhelper.getWritableDatabase();

        Cursor temp_1 = db.rawQuery("SELECT DATETIME('now') as date;",null);
        String date_data="";
        while (temp_1.moveToNext()) {
            date_data = temp_1.getString(temp_1.getColumnIndex("date"));
        }

        String temp_split[] =date_data.split(" ");
        String x =temp_split[0];

        String [] actual_date = x.split("-");
        String year = actual_date[0];
        String month = actual_date[1];
        String day = actual_date[2];

        Cursor cursor = db.rawQuery("Select  case_count from covidcases" +
                " where case_year=? and case_month =? and case_day = ? ",actual_date);
        int temp=0;
        while (cursor.moveToNext()) {
            temp = Integer.parseInt(cursor.getString(cursor.getColumnIndex("case_count")));
        }
        return temp;
    }

    public int getMonthlyCovidCase(){
        SQLiteDatabase db = myhelper.getWritableDatabase();

        Cursor temp_1 = db.rawQuery("SELECT DATETIME('now')as date;",null);
        String date_data="";
        while (temp_1.moveToNext()) {
            date_data = temp_1.getString(temp_1.getColumnIndex("date"));
        }

        String temp_split[] =date_data.split(" ");
        String x =temp_split[0];

        String [] actual_date = x.split("-");
        String year = actual_date[0];
        String month = actual_date[1];

        String [] args ={year,month};

        Cursor cursor = db.rawQuery("Select  Sum(case_count) as total from covidcases" +
                " where case_year=? and case_month =?  ",args);
        int temp=0;
        while (cursor.moveToNext()) {
            temp = Integer.parseInt(cursor.getString(cursor.getColumnIndex("total")));
        }
        return temp;

    }

    public int getYearlyCovid(){
        SQLiteDatabase db = myhelper.getWritableDatabase();

        Cursor temp_1 = db.rawQuery("SELECT DATETIME('now') as date;",null);
        String date_data="";
        while (temp_1.moveToNext()) {
            date_data = temp_1.getString(temp_1.getColumnIndex("date"));
        }

        String temp_split[] =date_data.split(" ");
        String x =temp_split[0];

        String [] actual_date = x.split("-");
        String year = actual_date[0];
        String month = actual_date[1];

        String [] args ={year};

        Cursor cursor = db.rawQuery("Select  Sum(case_count) as total from covidcases" +
                " where case_year=? ",args);
        String temp="";
        while (cursor.moveToNext()) {
            temp = cursor.getString(cursor.getColumnIndex("total"));
        }
        if(temp == null){
            temp = "0";
        }
        return Integer.parseInt(temp);

    }



    /**
     * Returns true or false if generated OTP code == Admin Input
     *
     * @param generated
     * @param input
     * @return
     */
    public boolean OTPverifier(String generated, String input) {
        return generated.equals(input);
    }

    /**
     * Returns 2D-ArrayList record of History Logs
     *
     * @return
     */
    public ArrayList<String[]> returnHistoryLog() {
        ArrayList<String[]> records = new ArrayList<>();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "Select * from historylog"
                , null);
        while (cursor.moveToNext()) {
            String log = cursor.getString(cursor.getColumnIndex("historyrecord"));
            String id = cursor.getString(cursor.getColumnIndex("familyID"));
            String date = cursor.getString(cursor.getColumnIndex("donationDate"));
            String array[] = {log, id, date};
            records.add(array);
        }
        return records;
    }

    /**
     * returns Covid Case Report for a specific criteria
     * <p>
     * IMPORTANT: DATE FORMAT SHOULD BE YYYY-MM-DD or YYYY-MM or YYYY
     * Anything else will result in an error/no result
     *
     * @param date
     * @return
     */
    public String returncovidcasereport(String date) {
        //DATE INPUT SHOULD BE IN YEAR-MONTH-DAY FORMAT!!! IMPORTANT!!!
        String[] data = date.split("-");
        String filter = "";

        if (data.length == 3) {
            filter = "case_year =? AND case_month = ? AND case_day = ?";
        } else if (data.length == 2) {
            filter = "case_year =? AND case_month = ? ";
        } else {
            filter = "case_year = ?";
        }

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String command = "Select SUM(case_count) as total from covidcases where ";
        Cursor cursor = db.rawQuery(command + filter, data);
        String total = "";
        while (cursor.moveToNext()) {
            total = cursor.getString(cursor.getColumnIndex("total"));
        }
        return total;
    }

    /**
     * Checks if the database is empty, returns TRUE if database contains data
     * FALSE if database is empty.
     * <p>
     * THIS METHOD IS USED FOR PRE-LOADING DATA SUCH AS COVID RECORDS FROM
     * Department of Health and INITIAL FAMILY RECORDS,
     * as per decided assumptions during meeting.
     *
     * @return
     */
    public boolean checkDatabaseContent() {
        boolean condition = false;
        condition = checkcovidDatabase();
        condition = checkFamilyCount();
        return condition;
    }

    /**
     * Returns 2D-ArrayList of families and their addresses based on the search filter and
     * donation status. Donation Status = 1 means the Family has received donations
     * Donation Status = 0 means the Family is YET to receive donations
     * <p>
     * NO FILTERS / DEFAULT Display
     *
     * @return
     */
    public ArrayList<String[]> returnFamilyChecklistAllCriteria() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ArrayList<String[]> records = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "Select * from Region inner join City on Region.RegionID=City.RegionID " +
                        "inner join Barangay on City.CityID=Barangay.CityID " +
                        "inner join Family on Family.BarangayID=Barangay.BarangayID where Family.DonatedStatus = 0"
                , null);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("FamilyID"));
            String FName = cursor.getString(cursor.getColumnIndex("FamilyName"));
            String MName = cursor.getString(cursor.getColumnIndex("HouseNo"));
            String LName = cursor.getString(cursor.getColumnIndex("Subdivision"));
            String Suffix = cursor.getString(cursor.getColumnIndex("StreetName"));
            String array[] = {id, FName, MName, LName, Suffix};
            records.add(array);
        }
        return records;
    }

    /**
     * Returns 2D-ArrayList of families and their addresses based on the search filter and
     * donation status. Donation Status = 1 means the Family has received donations
     * Donation Status = 0 means the Family is YET to receive donations
     * <p>
     * FOR REGION FILTER
     *
     * @param filterName
     * @return
     */
    public ArrayList<String[]> returnFamilyChecklistRegion(String filterName) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] data = {filterName};
        ArrayList<String[]> records = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "Select * from Region inner join City on Region.RegionID=City.RegionID " +
                        "inner join Barangay on City.CityID=Barangay.CityID " +
                        "inner join Family on Family.BarangayID=Barangay.BarangayID where RegionName = ? AND Family.DonatedStatus = 0"
                , data);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("FamilyID"));
            String FName = cursor.getString(cursor.getColumnIndex("FamilyName"));
            String MName = cursor.getString(cursor.getColumnIndex("HouseNo"));
            String LName = cursor.getString(cursor.getColumnIndex("Subdivision"));
            String Suffix = cursor.getString(cursor.getColumnIndex("StreetName"));
            String array[] = {id, FName, MName, LName, Suffix};
            records.add(array);
        }
        return records;
    }

    /**
     * Returns 2D-ArrayList of families and their addresses based on the search filter and
     * donation status. Donation Status = 1 means the Family has received donations
     * Donation Status = 0 means the Family is YET to receive donations
     * <p>
     * FOR CITY FILTER
     *
     * @param filterName
     * @return
     */
    public ArrayList<String[]> returnFamilyChecklistCity(String filterName) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] data = {filterName};
        ArrayList<String[]> records = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "Select * from Region inner join City on Region.RegionID=City.RegionID " +
                        "inner join Barangay on City.CityID=Barangay.CityID " +
                        "inner join Family on Family.BarangayID=Barangay.BarangayID where CityName = ? AND Family.DonatedStatus = 0"
                , data);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("FamilyID"));
            String FName = cursor.getString(cursor.getColumnIndex("FamilyName"));
            String MName = cursor.getString(cursor.getColumnIndex("HouseNo"));
            String LName = cursor.getString(cursor.getColumnIndex("Subdivision"));
            String Suffix = cursor.getString(cursor.getColumnIndex("StreetName"));
            String array[] = {id, FName, MName, LName, Suffix};
            records.add(array);
        }
        return records;
    }

    /**
     * Returns 2D-ArrayList of families and their addresses based on the search filter and
     * donation status. Donation Status = 1 means the Family has received donations
     * Donation Status = 0 means the Family is YET to receive donations
     * <p>
     * FOR BARANGAY FILTER
     *
     * @param filterName
     * @return
     */
    public ArrayList<String[]> returnFamilyChecklistBarangay(String filterName) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] data = {filterName};
        ArrayList<String[]> records = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "Select * from Region inner join City on Region.RegionID=City.RegionID " +
                        "inner join Barangay on City.CityID=Barangay.CityID " +
                        "inner join Family on Family.BarangayID=Barangay.BarangayID where BarangayName = ? AND Family.DonatedStatus = 0"
                , data);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("FamilyID"));
            String FName = cursor.getString(cursor.getColumnIndex("FamilyName"));
            String MName = cursor.getString(cursor.getColumnIndex("HouseNo"));
            String LName = cursor.getString(cursor.getColumnIndex("Subdivision"));
            String Suffix = cursor.getString(cursor.getColumnIndex("StreetName"));
            String array[] = {id, FName, MName, LName, Suffix};
            records.add(array);
        }
        return records;
    }

    /**
     * returns 2D-ArrayList of News and Announcements posted by Admin
     *
     * @return
     */
    public ArrayList<String[]> returnNewsAnnouncements() {
        ArrayList<String[]> records = new ArrayList<>();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "Select * from news_announcements ", null);
        while (cursor.moveToNext()) {
            String FamilyName = cursor.getString(cursor.getColumnIndex("Family.FamilyName"));
            String region = cursor.getString(cursor.getColumnIndex("Region.RegionName"));
            String city = cursor.getString(cursor.getColumnIndex("City.CityName"));
            String barangay = cursor.getString(cursor.getColumnIndex("Barangay.BarangayName"));
            String houseno = cursor.getString(cursor.getColumnIndex("Family.HouseNo"));
            String streetname = cursor.getString(cursor.getColumnIndex("Family.StreetName"));
            String subdivision = cursor.getString(cursor.getColumnIndex("Family.Subdivision"));
            String array[] = {FamilyName, region, city, barangay, houseno, streetname, subdivision};
            records.add(array);
        }
        return records;
    }

    /**
     * Check is covid case table is empty, return true if contains data
     * else false.
     *
     * @return
     */
    public boolean checkcovidDatabase() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from covidcases", null);
        while (cursor.moveToNext()) {
            String temp = cursor.getString(cursor.getColumnIndex("case_year"));
            if (temp != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if Family table is empty, returns true if table contains data,
     * else false.
     *
     * @return
     */
    public boolean checkFamilyCount() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Family", null);
        while (cursor.moveToNext()) {
            String temp = cursor.getString(cursor.getColumnIndex("FamilyID"));
            if (temp != null) {
                return true;
            }
        }
        return false;
    }

}