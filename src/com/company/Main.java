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


        Message message3=new Message();
        message3.subject="third";
        message3.message_id="03";
        message3.references=new HashSet<>();
        message3.references.add("08");

        Message message4=new Message();
        message4.subject="four";
        message4.message_id="04";
        message4.references=new HashSet<>();
        message4.references.add("03");

        Message message5=new Message();
        message5.subject="five";
        message5.message_id="05";
        message5.references=new HashSet<>();
        message5.references.add("03");

        Map<String,Container> map=Message.thread(message,message2,message3,message4,message5);
        System.out.println(map);
    }
}
