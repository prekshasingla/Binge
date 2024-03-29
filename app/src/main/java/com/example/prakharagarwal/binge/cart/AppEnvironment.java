package com.example.prakharagarwal.binge.cart;

/**
 * Created by prakharagarwal on 29/07/18.
 */

public enum AppEnvironment {

    SANDBOX {
        @Override
        public String merchant_Key() {
            return "d42tLe6Y";
        } //LLKwG0

        @Override
        public String merchant_ID() {
            return "6346573";
        } //393463

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "01EGfvermr"; //qauKbEAJ
        }

        @Override
        public boolean debug() {
            return true;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
            return "dcwQGU";
        }  //O15vkB

        @Override
        public String merchant_ID() {
            return "4931752";
        }   //4819816

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "dzC2i5pp";
        }     //LU1EhObh

        @Override
        public boolean debug() {
            return false;
        }
    };

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();


}
