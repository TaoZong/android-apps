package com.moreapp.doctorapp;

import java.io.Serializable;
import java.util.ArrayList;

public class CaseDetailObj implements Serializable {

	private static final long serialVersionUID = 9102660102288864221L;

	public String case_id, case_name, name, dob, gender, nationality,
			diagnosis, situation;
	public ArrayList<CommentObj> comments;
	public ArrayList<NotificationObj> notifications;

	public void setCaseDetail(ArrayList<CommentObj> comments,
			ArrayList<NotificationObj> notifications, String case_id,
			String case_name, String name, String dob, String gender,
			String nationality, String diagnosis, String situation) {
		this.comments = comments;
		this.notifications = notifications;
		this.case_id = case_id;
		this.case_name = case_name;
		this.name = name;
		this.dob = dob;
		this.gender = gender;
		this.nationality = nationality;
		this.diagnosis = diagnosis;
		this.situation = situation;

	}

}
