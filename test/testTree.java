import org.junit.Test;
import ru.mail.polis.AVLTree;
import ru.mail.polis.ISortedSet;
import ru.mail.polis.RedBlackTree;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;


public class testTree {
    @Test
    public void testAVLTree() {
        SortedSet<Integer> OK = new TreeSet<>();
        ISortedSet<Integer> set = new AVLTree<>();
        Random r = new Random();
        for (int i = 0; i < 1000 ; i++) {
            int value = r.nextInt(1000);
            assert OK.add(value) == set.add(value);
            assert OK.size() == set.size();
            assert OK.last().equals(set.last());
        }
        for (int i = 0; i < 1000; i++) {
            int value = r.nextInt(1000);
            assert OK.size() == set.size();
            assert OK.remove(value) == set.remove(value);
            assert OK.first() == set.first();
            assert OK.last().equals(set.last());
        }
    }

    @Test
    public void testRedBlackTree() {
        SortedSet<Integer> OK = new TreeSet<>();
        ISortedSet<Integer> set = new RedBlackTree<>();
        /**
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            int value = r.nextInt(1000);
            assert OK.add(value) == set.add(value);
            assert OK.size() == set.size();
        }
        for (int i = 0; i < 1000; i++) {
            int value = r.nextInt(1000);
            assert OK.remove(value) == set.remove(value);
            assert OK.size() == set.size();
            assert OK.first() == set.first();
            assert OK.last().equals(set.last());
        }
         **/
        for (int i = -10; i <0; i++){
            set.add(i);
            OK.add(i);
            System.out.println(set.inorderTraverse());
            System.out.println(OK.toString());
            assert (set.contains(i) == OK.contains(i));
        }
        for (int i  = 0; i >= -10; i--){
            set.remove(i);
            OK.remove(i);
            System.out.println(set.inorderTraverse());
            System.out.println(OK.toString());
            assert (set.contains(i) == OK.contains(i));
        }
    }
}
