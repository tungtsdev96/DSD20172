package com.hust.edu.dsd.model.staff;

/**
 * Created by tungts on 3/13/2018.
 */

public class Staff {


    /**
     * staff_id : 1
     * staff_code : QL_01_LinhPH
     * staff_name : Phạm Hải Linh
     * staff_dob : 1996-11-29T17:00:00.000Z
     * staff_phone : 01667645238
     * staff_address : Bạch Mai - Hà Nội
     * staff_role : MANAGER
     * staff_state : 1
     * username : linhph
     * password : linhph
     */

    private int staff_id;
    private String staff_code;
    private String staff_name;
    private String staff_dob;
    private String staff_phone;
    private String staff_address;
    private String staff_role;
    private int staff_state;
    private String username;
    private String password;

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_code() {
        return staff_code;
    }

    public void setStaff_code(String staff_code) {
        this.staff_code = staff_code;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStaff_dob() {
        return staff_dob;
    }

    public void setStaff_dob(String staff_dob) {
        this.staff_dob = staff_dob;
    }

    public String getStaff_phone() {
        return staff_phone;
    }

    public void setStaff_phone(String staff_phone) {
        this.staff_phone = staff_phone;
    }

    public String getStaff_address() {
        return staff_address;
    }

    public void setStaff_address(String staff_address) {
        this.staff_address = staff_address;
    }

    public String getStaff_role() {
        return staff_role;
    }

    public void setStaff_role(String staff_role) {
        this.staff_role = staff_role;
    }

    public int getStaff_state() {
        return staff_state;
    }

    public void setStaff_state(int staff_state) {
        this.staff_state = staff_state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
