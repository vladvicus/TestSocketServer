package com.epam.util;

import com.epam.bean.Book;


public class XmlBuilder {

    public static String xmlBuilderForPostCreate(Book book) {

        String body = "<book>" +
                "<id>" + book.getId() + "</id>" +
                "<language>" + book.getLanguage() + "</language>" +
                "<edition>" + book.getEdition() + "</edition>" +
                "<author>" + book.getAuthor() + "</author>" +
                "<create_date>" + book.getDate() + "</create_date>" +
                "</book>";

        return body;
    }

    public static String xmlBuilderForPutCreate(Book book) {

        String body = "<book>" +
                "<id>" + book.getId() + "</id>" +
                "<language>" + book.getLanguage() + "</language>" +
                "<edition>" + book.getEdition() + "</edition>" +
                "<author>" + book.getAuthor() + "</author>" +
                "<create_date>" + book.getDate() + "</create_date>" +
                "</book>";

        return body;
    }

    public static String xmlBuilderAuthorForChange(String author) {
        String body="<book>" +

                "<author>" + author + "</author>" +

                "</book>";


        return body;
    }

    public static String xmlBuilderLangForChange(String language,int id) {
        String body = "<book>" +
                "<id>" + id + "</id>" +
                "<language>" + language + "</language>" +
                "</book>";

        return body;
    }
}
