package io.github.maximgorbatyuk.taskmanager.help;

import java.util.Date;

/**
 * Created by Maxim on 09.04.2016.
 */
public class Task {
    private int id;
    private String title;
    private String body;
    private boolean isDone;
    private Date deadline;
    private Date createdAt;
    private int priority;

    public Task(){
        id = -1;
        title = "No title";
        body = "Empty task";
        isDone = false;
        deadline = null;
        createdAt = new Date();
        priority = 0;
    }

    public Task(String body){
        id = -1;
        title = "No title";
        this.body = body;
        isDone = false;
        deadline = null;
        createdAt = new Date();
        priority = 0;
    }

    public int getId() { return id;}
    public void setId(int id) { this.id = id; }

    public String   getTitle() { return title; }
    public void     setTitle(String title) { this.title = title; }

    public String   getBody() { return body; }
    public void     setBody(String body) { this.body = body; }

    public boolean  getIsDone() { return isDone; }
    public void     setIsDone(boolean isDone) {  this.isDone = isDone; }

    public Date     getDeadline() { return deadline; }
    public void     setDeadline(Date deadline) { this.deadline = deadline; }

    public Date     getCreatedAt() { return createdAt; }
    public void     setCreatedAt(Date createdAt) { this.createdAt = createdAt;  }

    public int      getPriority() { return priority; }
    public void     setPriority(int priority) { this.priority = priority; }
}
