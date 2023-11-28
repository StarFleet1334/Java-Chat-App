package client_side;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Set<ArrayList<String>> set = new HashSet<>();


        set.add(new ArrayList<>(Arrays.asList("22222","127.0.0.1")));
        set.add(new ArrayList<>(Arrays.asList("22222","127.0.0.1")));

        System.out.println(set);

     }
}
