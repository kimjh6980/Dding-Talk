package com.example.keeyoung.ddingtalk1;

/**
 * Created by keeyoung on 2017-01-29.
 */

public class Contact {
    public long photoid;
    public String phonenum;
    public String name;
    public static int number;

    public static long[] arrayphotoid;
    public static String[] arrayphonenum;
    public static String[] arrayname;

    public static int a = 0;
    public static int b = 0;
    public static int c = 0;

    Contact() {
    }

    Contact(int number) {
        arrayphotoid = new long[number];
        arrayphonenum = new String[number];
        arrayname = new String[number];
    }

    public void setcount() {
        a=0;
        b=0;
        c=0;
    }

    public  void setNumber(int number) {
        this.number = number;
    }

    public long getPhotoid() {
        return photoid;
    }

    public void setPhotoid(long photoid) {
        this.photoid = photoid;
        arrayphotoid[b] = this.photoid;
            b++;
            if((b-1)==number){
                a=0;
                b=0;
                c=0;
        }
    }

    public void setarrayPhotoid(long photoid) {
        this.photoid = photoid;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
        arrayphonenum[a] = this.phonenum;
        a++;
    }

    public void setarrayPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        arrayname[c] = this.name;
        c++;
    }

    public void setPhotoids(long photoid) {
        this.photoid = photoid;
    }

    public void setPhonenums(String phonenum) {
        this.phonenum = phonenum;
    }

    public void setNames(String name) {
        this.name = name;
    }

    public void setarrayName(String name) {
        this.name = name;
    }
}
