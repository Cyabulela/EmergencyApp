package com.example.sosapp.state;

public class LockFunctions {

    private static Status state = Status.Locked;

     public static void lock() {
         state = Status.Locked;
     }

     public static void unlock() {
         state = Status.Unlocked;
     }

     public static Status getState() {
         return state;
     }
}
