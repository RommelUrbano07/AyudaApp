package com.example.ayudaapp;

import androidx.appcompat.app.AppCompatActivity;

public class CaseItem extends AppCompatActivity {
    private String mCaseText1;
    private String mCaseText2;

    public CaseItem(String caseText1, String caseText2){
        mCaseText1 = caseText1;
        mCaseText2 = caseText2;
    }

    public String getCaseText1(){
        return mCaseText1;
    }

    public String getCaseText2(){
        return mCaseText2;
    }

}
