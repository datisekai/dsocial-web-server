/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Models;

import org.springframework.data.domain.Page;

/**
 *
 * @author haidu
 */
public class Pagination {

    public int total_page;
    public int current_page;
    public int per_page;
    public String next_page;
    public String prev_page;

    public Pagination(int total_page, int current_page, int per_page, String next_page, String prev_page) {
        this.total_page = total_page;
        this.current_page = current_page;
        this.per_page = per_page;
        this.next_page = next_page;
        this.prev_page = prev_page;
    }

    public static Pagination getPagination(String page, String limit, Page gr) {
        Pagination p = new Pagination();
        p.setTotal_page(gr.getTotalPages());
        p.setCurrent_page(Integer.parseInt(page));
        p.setNext_page(p.getCurrent_page() < p.getTotal_page() ? (p.getCurrent_page() + 1) + "" : null);
        p.setPer_page(gr.getNumberOfElements());
        p.setPrev_page(p.getCurrent_page() > 1 ? (p.getCurrent_page() - 1) + "" : null);
        return p;
    }

    public Pagination() {
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public String getNext_page() {
        return next_page;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public String getPrev_page() {
        return prev_page;
    }

    public void setPrev_page(String prev_page) {
        this.prev_page = prev_page;
    }

}
