package com.creativehazio.launchpad.model;

public class Text {

    private String model;
    private String prompt;
    private Integer max_tokens;
    private String instruction;
    private Choices[] choices;

    public Text(String model, String prompt,Integer max_tokens) {
        this.model = model;
        this.prompt = prompt;
        this.max_tokens = max_tokens;
    }

    public String getModel() {
        return model;
    }

    public String getPrompt() {
        return prompt;
    }

    public Choices[] getChoices() {
        return choices;
    }

    public class Choices{
        private String text;

        public String getText() {
            return text;
        }
    }
}
