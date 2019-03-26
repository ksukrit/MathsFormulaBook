package com.alphabyte.maths.models;

import java.io.Serializable;
import java.util.List;

public class Maths implements Serializable {
    /**
     * topic_count : 2
     * data_version : 1
     * last_updated : 08/04
     * topic : [{"topic_name":"Polynomials","topic_subtopic_count":1,"subtopic":[{"name":"Solving A Quadratic Equation","images":"No","formula":"$$x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}$$","example":{"question":"Find the Zero of the Equation f(x)","solution":"Use The Quadratic Formula"},"information":["a is the co-efficient of X2","b is the co-efficient of x","c is the constant term"]}]},{"topic_name":"Number System","topic_subtopic_count":2,"subtopic":[{"name":"Natural Numbers","images":"No","formula":"All Numbers From 1 to Infinity are natural numbers","example":{"question":"Find the Zero of the Equation f(x)","solution":"Use The Quadratic Formula"},"information":["a is the co-efficient of X2","b is the co-efficient of x","c is the constant term"]},{"name":"Whole Numbers","images":"No","formula":"All Numbers From 0 to Infinity are natural numbers","example":{"question":"Find the Zero of the Equation f(x)","solution":"Use The Quadratic Formula"},"information":["a is the co-efficient of X2","b is the co-efficient of x","c is the constant term"]}]}]
     */

    private int topic_count;
    private int data_version;
    private String last_updated;
    private List<Topic> topic;

    public int getTopic_count() {
        return topic_count;
    }

    public void setTopic_count(int topic_count) {
        this.topic_count = topic_count;
    }

    public int getData_version() {
        return data_version;
    }

    public void setData_version(int data_version) {
        this.data_version = data_version;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public List<Topic> getTopic() {
        return topic;
    }

    public void setTopic(List<Topic> topic) {
        this.topic = topic;
    }

    public static class Topic implements Serializable{
        /**
         * topic_name : Polynomials
         * topic_subtopic_count : 1
         * subtopic : [{"name":"Solving A Quadratic Equation","images":"No","formula":"$$x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}$$","example":{"question":"Find the Zero of the Equation f(x)","solution":"Use The Quadratic Formula"},"information":["a is the co-efficient of X2","b is the co-efficient of x","c is the constant term"]}]
         */

        private String topic_name;
        private int topic_subtopic_count;
        private List<Subtopic> subtopic;
        private int logo_color;
        private String logo_image;

        public String getLogo_image() {
            return logo_image;
        }

        public void setLogo_image(String logo_image) {
            this.logo_image = logo_image;
        }

        public int getLogo_color() {
            return logo_color;
        }

        public void setLogo_color(int logo_color) {
            this.logo_color = logo_color;
        }

        public String getTopic_name() {
            return topic_name;
        }

        public void setTopic_name(String topic_name) {
            this.topic_name = topic_name;
        }

        public int getTopic_subtopic_count() {
            return topic_subtopic_count;
        }

        public void setTopic_subtopic_count(int topic_subtopic_count) {
            this.topic_subtopic_count = topic_subtopic_count;
        }

        public List<Subtopic> getSubtopic() {
            return subtopic;
        }

        public void setSubtopic(List<Subtopic> subtopic) {
            this.subtopic = subtopic;
        }

        public static class Subtopic implements Serializable{
            /**
             * name : Solving A Quadratic Equation
             * images : No
             * formula : $$x = \frac{-b \pm \sqrt{b^2 - 4ac}}{2a}$$
             * example : {"question":"Find the Zero of the Equation f(x)","solution":"Use The Quadratic Formula"}
             * information : ["a is the co-efficient of X2","b is the co-efficient of x","c is the constant term"]
             */

            private String name;
            private String images;
            private String formula;
            private boolean display_formula;
            private boolean display_information;
            private Examples example;
            private List<String> information;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public String getFormula() {
                return formula;
            }

            public void setFormula(String formula) {
                this.formula = formula;
            }

            public Examples getExample() {
                return example;
            }

            public void setExample(Examples example) {
                this.example = example;
            }

            public List<String> getInformation() {
                return information;
            }

            public void setInformation(List<String> information) {
                this.information = information;
            }

            public boolean isDisplay_formula() {
                return display_formula;
            }

            public void setDisplay_formula(boolean display_formula) {
                this.display_formula = display_formula;
            }

            public boolean isDisplay_information() {
                return display_information;
            }

            public void setDisplay_information(boolean display_information) {
                this.display_information = display_information;
            }

            public static class Examples implements Serializable {
                /**
                 * question : Find the Zero of the Equation f(x)
                 * solution : Use The Quadratic Formula
                 */

                private String question;
                private String solution;
                private boolean displayExample;

                public String getQuestion() {
                    return question;
                }

                public void setQuestion(String question) {
                    this.question = question;
                }

                public String getSolution() {
                    return solution;
                }

                public void setSolution(String solution) {
                    this.solution = solution;
                }

                public boolean isDisplayExample() {
                    return displayExample;
                }

                public void setDisplayExample(boolean displayExample) {
                    this.displayExample = displayExample;
                }


            }
        }
    }
}