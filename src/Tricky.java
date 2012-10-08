import java.util.LinkedList;
import java.util.List;

public class Tricky {
    public static void main(String[] args) {

        int count =4;
        List<TestFail> list = new LinkedList<TestFail>();

        for(int i=0; i<2;++i) {
            list.add(new TestFail(1));
        }

        while(true) {
            for(TestFail tf : list) {
                if(tf.isFailed()) {
                    System.out.println(count);
                    System.out.println(count /60 + " " + (count % 60));
                    return;
                } else {
                    tf.generateNext();
                }
            }

            ++count;
        }

    }

    public static Integer generateRandomNumber() {
        int t;
        t  = (int) (Math.random()*100);
        return t;
    }

    private static class TestFail{
        private Integer a,b,c,d;
        private Integer rates;

        public TestFail(Integer rates) {
            a = generateRandomNumber();
            b = generateRandomNumber();
            c = generateRandomNumber();
            d = generateRandomNumber();

            this.rates = rates;
        }

        public boolean isFailed() {
            return a < rates && b < rates && c <rates && d <rates;
        }

        public void generateNext() {
            a = b; b = c; c = d;
            d = generateRandomNumber();
        }
    }
}
