package com.ssuog.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Quiz {
    private String guid;

    private Boolean quizavailable;

    private Double score;

    private Integer costtime;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updatetime;

    

    public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Boolean getQuizavailable() {
        return quizavailable;
    }

    public void setQuizavailable(Boolean quizavailable) {
        this.quizavailable = quizavailable;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getCosttime() {
        return costtime;
    }

    public void setCosttime(Integer costtime) {
        this.costtime = costtime;
    }

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
}