package aqs.exchanger;


import java.util.concurrent.Exchanger;

/**
 * doc上面的案例：
 *
 * @author: mahao
 * @date: 2019/9/23
 */


class FillAndEmpty {


    Exchanger<DataBuffer> exchanger = new Exchanger<DataBuffer>();
    DataBuffer initialEmptyBuffer = null;
    DataBuffer initialFullBuffer = null;

    class FillingLoop implements Runnable {
        public void run() {
            DataBuffer currentBuffer = initialEmptyBuffer;
            try {
                while (currentBuffer != null) {
                    addToBuffer(currentBuffer);
                    if (currentBuffer.isFull())
                        currentBuffer = exchanger.exchange(currentBuffer);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        private void addToBuffer(DataBuffer currentBuffer) {

        }
    }

    class EmptyingLoop implements Runnable {
        public void run() {
            DataBuffer currentBuffer = initialFullBuffer;
            try {
                while (currentBuffer != null) {
                    takeFromBuffer(currentBuffer);
                    if (currentBuffer.isEmpty())
                        currentBuffer = exchanger.exchange(currentBuffer);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        private void takeFromBuffer(DataBuffer currentBuffer) {


        }
    }

    static class DataBuffer {

        final int max = 100;
        byte[] buf = new byte[128];


        public boolean isFull() {
            return buf.length == max;
        }

        public boolean isEmpty() {
            return buf.length == 0;
        }
    }

    void start() {
        new Thread(new FillingLoop()).start();
        new Thread(new EmptyingLoop()).start();
    }
}
