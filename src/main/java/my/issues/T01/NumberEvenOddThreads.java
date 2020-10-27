package my.issues.T01;

public class NumberEvenOddThreads {

    static final class LastNumber {
        int num;
        final int limit;

        LastNumber(int num, int limit) {
            this.num = num;
            this.limit = limit;
        }
    }

    static final class NumberOddEven implements Runnable {
        private final LastNumber last;
        private final int init;

        NumberOddEven(LastNumber last, int init) {
            this.last = last;
            this.init = init;
        }

        @Override
        public void run() {
            int i = init;
            synchronized (last) {
                while (i <= last.limit) {
                    while (last.num != i) {
                        try {
                            last.wait();
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName() + " Number : " + i);
                    last.num = i + 1;
                    i += 2;
                    last.notify();

                }
            }
        }
    }

    public static void main(String[] args) {
        LastNumber last = new LastNumber(0, 9);
        NumberOddEven odd = new NumberOddEven(last, 1);
        NumberOddEven even = new NumberOddEven(last, 0);
        new Thread(odd, "Odd ").start();
        new Thread(even, "Even").start();

    }
}
