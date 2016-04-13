package io.github.maximgorbatyuk.taskmanager.help;

import java.util.Date;

public class Project {
    private int id;
    private String title;
    private String body;
    private boolean isDone;
    private Date deadline;
    private Date createdAt;
    private double cost;
    private long milliseconds;

    public Project(){
        id = -1;
        title = "No title";
        body = "Empty description";
        isDone = false;
        deadline = null;
        createdAt = new Date();
        cost = 0.0;
        milliseconds = 0;
    }

    public Project(String body){
        id = -1;
        title = "No title";
        this.body = body;
        isDone = false;
        deadline = null;
        createdAt = new Date();
        cost = 0;
        milliseconds = 0;
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

    public double   getCost() { return cost; }
    public void     setCost(double cost) { this.cost = cost; }

    public long     getMilliseconds() { return milliseconds; }
    public void     setMilliseconds(long milliseconds) { this.milliseconds = milliseconds; }
}
