package com.scut.scutwizard;

public class Event {
    public String name;
    public double progress;
    public int finish;
    public int daysLeft;
    public double rating;
    public int step;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        this.calcuRating();
    }

    public int isfinish() {
        return finish;
    }

    public void setfinish(int finish) {
        this.finish = finish;
        this.calcuRating();
    }

    public int getDaysLeft() {
        return daysLeft;
    }


    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
        this.calcuRating();
    }

    public double getRating() {
        return rating;
    }

    public Event(String _name) {
        this.name = _name;
        this.daysLeft = 30;
        this.progress = 0;
        this.step = 1;
        this.finish = 0;
        this.calcuRating();
    }
    public Event(String _name,int _daysLeft,double _progress,int _step) {
        this.name = _name;
        this.progress = _progress;
        this.daysLeft = _daysLeft;
        this.step = _step;
        this.finish = 0;
        this.calcuRating();
    }
    //计算 当前任务紧急度
    public void calcuRating(){
        if(this.finish==0){
            if(this.daysLeft>30) this.rating=0.5;
            else{
                double temp = 30/this.daysLeft*(100-this.progress)/100;
                if(temp>5) this.rating=5;
                else this.rating = temp;
            }
        }else{
            this.rating=0;
        }

    }

}
