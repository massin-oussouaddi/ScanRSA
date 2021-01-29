package fr.group11.rsa;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class ScanRSA {
    private ScanKey private_key;
    private ScanKey public_key;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private final BigInteger prime1 = new BigInteger("93261201294115875151943071693225905626862013735374417540424048612668780343587771226333339058034762149540307330987786291987224003757672135674670711299710575943012595388754244080107664767777349273180282776475548700301389956159427325160247599478827451186315046964279195437528561368622278102414197470890601137849");
    private final BigInteger prime2 = new BigInteger("163332915385669219443932411435520654043179386715214525889385446480746394061419253368516571700246844281370953089838869833830284986842038408598781938820705048217163739493338544154205479348684470703613380258946092161714747557985636531480181155237757824938386576868072324287117780675669575730706925670078000411511");
    private ScanRSA(){
        BigInteger n = prime1.multiply(prime2);
        BigInteger c = lcm(prime1.subtract(BigInteger.ONE).toString(),prime2.subtract(BigInteger.ONE).toString());
        BigInteger e = new BigInteger("17");
        BigInteger d = e.modInverse(c);
        public_key = new ScanKey(n,e);
        private_key = new ScanKey(n,d);
    }

    private BigInteger encrypt(BigInteger message){
        return message.modPow(public_key.getPow(), public_key.getN());
    }

    private BigInteger decrypt(BigInteger decrypt){
        return decrypt.modPow(private_key.getPow(),private_key.getN());
    }

    private BigInteger lcm(String a, String b)
    {
        BigInteger s = new BigInteger(a);
        BigInteger s1 = new BigInteger(b);
        BigInteger mul = s.multiply(s1);
        BigInteger gcd = s.gcd(s1);
        return mul.divide(gcd);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ScanRSA ScanRSA = new ScanRSA();
        System.out.println(ANSI_RED + " Welcome to RSA-SCAN Service" + ANSI_RESET);
        System.out.println(ANSI_BLUE + " You can choose either to Encrypt or Decrypt ");
        System.out.println(ANSI_BLUE + " What would you like to do ?" + ANSI_RESET);
        String choice = sc.nextLine();
        if (choice.equalsIgnoreCase("Encrypt")) {
            System.out.println(ANSI_PURPLE + " What do you wanna encrypt ?" + ANSI_RESET);
            String toEncrypt = sc.nextLine();
            if (toEncrypt.length() <= 14) {
                BigInteger k = new BigInteger(toEncrypt.getBytes());
                BigInteger ab = ScanRSA.encrypt(k);
                System.out.println("Encrypted : " + ab.toString());
                System.out.println("Do you want it copied to your ClipBoard ?");
                String choice1 = sc.nextLine();
                if (choice1.equalsIgnoreCase("yes") || choice1.equalsIgnoreCase("oui")) {
                    StringSelection stringSelection = new StringSelection(ab.toString());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                }
                System.out.println(ANSI_GREEN + " Thank you for using SCAN-RSA Service");
            } else {
                ArrayList<String> strings = new ArrayList<>();
                int index = 0;
                while (index < toEncrypt.length()) {
                    strings.add(toEncrypt.substring(index, Math.min(index + 14, toEncrypt.length())));
                    index += 14;
                }
                String encrypt = "";
                for (String eo : strings) {
                    BigInteger t = new BigInteger(eo.getBytes());
                    BigInteger et = ScanRSA.encrypt(t);
                    encrypt += et.toString() + "§";
                }
                encrypt = removeLastChar(encrypt);
                System.out.println("Encrypted : " + encrypt);
                System.out.println("Do you want it copied to your ClipBoard ?");
                String choice1 = sc.nextLine();
                if (choice1.equalsIgnoreCase("yes") || choice1.equalsIgnoreCase("oui")) {
                    StringSelection stringSelection = new StringSelection(encrypt);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                }
                System.out.println(ANSI_GREEN + " Thank you for using SCAN-RSA Service");
            }

        } else if (choice.equalsIgnoreCase("Decrypt")) {
            System.out.println(ANSI_PURPLE + " What do you wanna decrypt ?" + ANSI_RESET);
            String toDecrypt = sc.nextLine();
            if(toDecrypt.contains("§")) {
                String[] multi = toDecrypt.split("§");
                String decrypt = "";
                for(String s : multi) {
                    BigInteger bo = new BigInteger(s);
                    BigInteger fin = ScanRSA.decrypt(bo);
                    String ep = new String(fin.toByteArray(),StandardCharsets.UTF_8);
                    decrypt+=ep;
                }
                System.out.println(decrypt);
                System.out.println("Do you want it copied to your ClipBoard ?");
                String choice1 = sc.nextLine();
                if (choice1.equalsIgnoreCase("yes") || choice1.equalsIgnoreCase("oui")) {
                    StringSelection stringSelection = new StringSelection(decrypt);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                }
                System.out.println(ANSI_GREEN + " Thank you for using SCAN-RSA Service");
            } else {
                BigInteger bo = new BigInteger(toDecrypt);
                BigInteger fin = ScanRSA.decrypt(bo);
                String ep = new String(fin.toByteArray(), StandardCharsets.UTF_8);
                System.out.println(ep);
                System.out.println("Do you want it copied to your ClipBoard ?");
                String choice1 = sc.nextLine();
                if (choice1.equalsIgnoreCase("yes") || choice1.equalsIgnoreCase("oui")) {
                    StringSelection stringSelection = new StringSelection(ep);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                }
                System.out.println(ANSI_GREEN + " Thank you for using SCAN-RSA Service");
            }
        } else {
            System.out.println(ANSI_RED + "ERROR, choose between the proposed choices" + ANSI_RESET);
            main(null);
        }
    }

        private static String removeLastChar(String s) {
            return (s == null || s.length() == 0)
                    ? null
                    : (s.substring(0, s.length() - 1));
        }
    }