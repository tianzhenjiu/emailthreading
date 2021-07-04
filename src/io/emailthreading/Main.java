package io.emailthreading;

import java.util.Map;

/**
 * @author tianzhenjiu
 */
public class Main {


    private static void printContainer(Container container,int deep){
        for (int i = 0; i <deep ; i++) {
            System.out.print("\t");
        }
        System.out.println(container);
        container.children.forEach(container1 -> {
            printContainer(container1,deep+1);
        });
    }

    public static void printThread(Map<String,Container> threadMap,int deep){

        threadMap.forEach((k,v)->{
            printContainer(v,0);
        });
    }

    public static void main(String[] args) {

        Message message=new Message("a","a subject");

        Message message2 =new Message("b","b subject","a");

        Message message3 =new Message("c","this is c");

        Message message4 =new Message("d","d subject","c");

        Message message5 =new Message("e","e subject","c","a");

        Map<String,Container> map=Message.thread(message,message2,message3,message4,message5);
        printThread(map,0);
    }
}
