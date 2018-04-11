package com.david.jetcab.Models;

import com.david.jetcab.BaseActivity;
import com.google.gson.JsonObject;

/**
 * Created by david on 18/03/2018.
 */

public class Faq {
    private String question;
    private String answer;

    public Faq() {
        this.question = "";
        this.answer = "";
    }

    public Faq(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setByJsonObject(JsonObject oneFaqJsonObject) {

        if (oneFaqJsonObject == null || oneFaqJsonObject.isJsonNull())
            return;

        if (oneFaqJsonObject.has("question") && !oneFaqJsonObject.get("question").isJsonNull())
            this.question = oneFaqJsonObject.get("question").getAsString();

        if (oneFaqJsonObject.has("answer") && !oneFaqJsonObject.get("answer").isJsonNull())
            this.answer = oneFaqJsonObject.get("answer").getAsString();
    }
}
