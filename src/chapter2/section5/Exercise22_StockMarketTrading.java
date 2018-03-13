package chapter2.section5;

import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 14/04/17.
 */
public class Exercise22_StockMarketTrading {

    private class Order implements Comparable<Order>{

        private int value;
        private int shares;

        Order(int value, int shares) {
            this.value = value;
            this.shares = shares;
        }

        @Override
        public int compareTo(Order that) {
            return this.value - that.value;
        }
    }

    private static final String BUY_ORDER = "Buy";
    private static final String SELL_ORDER = "Sell";

    public static void main(String[] args) {
        String order1 = BUY_ORDER + " 100" + " 10";
        String order2 = BUY_ORDER + " 99" + " 20";
        String order3 = SELL_ORDER + " 120" + " 5";
        String order4 = SELL_ORDER + " 90" + " 8";
        String order5 = SELL_ORDER + " 125" + " 5";
        String order6 = BUY_ORDER + " 110" + " 10";
        String order7 = BUY_ORDER + " 115" + " 2";
        String order8 = SELL_ORDER + " 110" + " 4";
        String order9 = SELL_ORDER + " 100" + " 3";

        String[] orders = {order1, order2, order3, order4, order5, order6, order7, order8, order9};

        new Exercise22_StockMarketTrading().matchBuyersAndSellers(orders);

        StdOut.println("\nExpected: ");
        StdOut.println("8 shares sold for 100");
        StdOut.println("2 shares sold for 115");
        StdOut.println("2 shares sold for 110");
        StdOut.println("3 shares sold for 110");
    }

    private void matchBuyersAndSellers(String[] orders) {
        PriorityQueueResize<Order> buyersPQ = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MAX);
        PriorityQueueResize<Order> sellersPQ = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

        for(int i = 0; i < orders.length; i++) {
            String[] values = orders[i].split(" ");
            int orderValue = Integer.parseInt(values[1]);
            int quantityOfShares = Integer.parseInt(values[2]);

            Order order = new Order(orderValue, quantityOfShares);
            int sharesRemaining;

            if (values[0].charAt(0) == 'B') {

                while(quantityOfShares > 0) {
                    if (sellersPQ.size() > 0) {
                        Order lowestPriceOrder = sellersPQ.peek();

                        if (lowestPriceOrder.value <= orderValue) {
                            int numberOfSharesSold = Math.min(lowestPriceOrder.shares, quantityOfShares);

                            StdOut.println(numberOfSharesSold + " shares sold for " + lowestPriceOrder.value);
                            sharesRemaining = lowestPriceOrder.shares - quantityOfShares;

                            quantityOfShares -= numberOfSharesSold;

                            if (sharesRemaining <= 0) {
                                sellersPQ.deleteTop();
                            } else {
                                lowestPriceOrder.shares = sharesRemaining;
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }

                if (quantityOfShares > 0) {
                    order.shares = quantityOfShares;
                    buyersPQ.insert(order);
                }
            } else {
                while(quantityOfShares > 0) {
                    if (buyersPQ.size() > 0) {
                        Order highestPriceOrder = buyersPQ.peek();

                        if (highestPriceOrder.value >= orderValue) {
                            int numberOfSharesSold = Math.min(highestPriceOrder.shares, quantityOfShares);

                            StdOut.println(numberOfSharesSold + " shares sold for " + highestPriceOrder.value);
                            sharesRemaining = highestPriceOrder.shares - quantityOfShares;

                            quantityOfShares -= numberOfSharesSold;

                            if (sharesRemaining <= 0) {
                                buyersPQ.deleteTop();
                            } else {
                                highestPriceOrder.shares = sharesRemaining;
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }

                if (quantityOfShares > 0) {
                    order.shares = quantityOfShares;
                    sellersPQ.insert(order);
                }
            }
        }
    }
}
