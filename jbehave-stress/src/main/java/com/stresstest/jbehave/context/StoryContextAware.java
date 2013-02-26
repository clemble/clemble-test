package com.stresstest.jbehave.context;

public interface StoryContextAware {

    public String getStoryContextObject();
    
    public void setStoryContextObject(String name);
}
