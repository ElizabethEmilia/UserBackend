package test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NumTest {

    @Test
    public void test1(){

        List<Integer> arr1 = new ArrayList<>();
        arr1.add(1);
        arr1.add(2);
        List<Integer> arr2 = new ArrayList<>();
        arr1.add(3);
        arr1.add(4);
        arr1.addAll(arr2);
        System.out.println(arr1);

    }


}
