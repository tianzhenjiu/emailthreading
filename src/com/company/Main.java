package com.company;

import java.util.HashSet;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Message message=new Message();
        message.subject="first";
        message.message_id="01";
        message.references=new HashSet<>();

        Message message2=new Message();
        message2.subject="second";
        message2.message_id="02";
        message2.references=new HashSet<>();
        message2.references.add("01");

        Map<String,Container> map=Message.thread(message,message2);
        System.out.println(map);
    }
}
