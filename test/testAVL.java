import org.junit.Test;
import ru.mail.polis.AVLTree;

import java.util.TreeSet;


public class testAVL {
    @Test
    public void testAVLTree() {
        AVLTree<Integer> set = new AVLTree<Integer>();
        TreeSet<Integer> OK = new TreeSet<>();
        for (int i = 0; i <= 10; i++) {
            assert set.add(i) == OK.add(i);
        }
        System.out.println(OK.toString());
        System.out.println(set.inorderTraverse().toString());
        for (int i = 10; i >= 0; i--) {
            assert set.contains(i) == OK.contains(i);
            assert set.remove(i) == OK.remove(i);
        }
    }
}
