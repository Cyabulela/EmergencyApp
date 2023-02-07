package com.example.sosapp.verify;

import junit.framework.TestCase;

public class ValidTest extends TestCase {

    public void testValidNumber() {
        assertEquals(true , Valid.validNumber("0663659567"));
    }

    public void testValidNumberIII() {
        assertNotSame(true , Valid.validNumber("066363567"));
    }

    public void testValidNumberII() {
        assertNotSame(true , Valid.validNumber("555-674-89"));
    }
}