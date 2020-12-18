package adventofcode2020;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Day18 {
    public static <T> T pop(ArrayList<T> stack) {
        return stack.remove(stack.size() - 1);
    }

    public static <T> T last(ArrayList<T> stack) {
        return stack.get(stack.size() - 1);
    }

    static class Expression {
        private ArrayList<String> items;
        private ArrayList<String> originalItems;

        public Expression(ArrayList<String> items,
                ArrayList<String> originalItems) {
            this.items = items;
            this.originalItems = originalItems;
        }

        @Override
        public String toString() {
            return "Expression: " + items + " (" + originalItems + ")";
        }

        static Expression parsePart1(String l) {
            ArrayList<String> items = new ArrayList<>();
            var pattern = Pattern.compile("[0-9]+|[()+*]");
            var matcher = pattern.matcher(l);
            while (matcher.find())
                items.add(matcher.group());
            return new Expression(toRpnPart1(items), items);
        }

        static Expression parsePart2(String l) {
            ArrayList<String> items = new ArrayList<>();
            var pattern = Pattern.compile("[0-9]+|[()+*]");
            var matcher = pattern.matcher(l);
            while (matcher.find())
                items.add(matcher.group());
            return new Expression(toRpnPart2(items), items);
        }

        long eval() {
            ArrayList<Long> stack = new ArrayList<>();
            for (String i : items) {
                if (isNumber(i))
                    stack.add(Long.parseLong(i));
                else if (i.equals("*"))
                    stack.add(pop(stack) * pop(stack));
                else if (i.equals("+"))
                    stack.add(pop(stack) + pop(stack));

            }
            assert stack.size() == 1;
            return stack.get(0);
        }

        /**
         * Should have been using this all along
         * https://en.wikipedia.org/wiki/Shunting-yard_algorithm
         * 
         * @param items
         *            expression items in infix order
         * @return items in RPN order with part2 precedence (aka * binds looser than
         *         +)
         */
        static ArrayList<String> toRpnPart2(ArrayList<String> items) {
            var out = new ArrayList<String>();
            var operatorStack = new ArrayList<String>();

            for (String item : items) {
                if (isNumber(item)) {
                    out.add(item);
                } else if (item.equals("("))
                    operatorStack.add(item);
                else if (item.equals(")")) {
                    String operator;
                    while (!(operator = pop(operatorStack)).equals("("))
                        out.add(operator);
                } else if (!operatorStack.isEmpty()
                        && precedence(item) <= precedence(last(operatorStack))
                        && !last(operatorStack).equals("(")) {
                    String previousStackTop = pop(operatorStack);
                    out.add(previousStackTop);
                    operatorStack.add(item);
                } else {
                    operatorStack.add(item);
                }
            }

            while (operatorStack.size() > 0)
                out.add(pop(operatorStack));
            return out;
        }

        private static int precedence(String item) {
            if (item.equals("("))
                return 3;
            else if (item.equals("+"))
                return 2;
            assert item.equals("*"); // we only have two operators
            return 1;
        }

        static ArrayList<String> toRpnPart1(ArrayList<String> items) {
            ArrayList<String> rpn = new ArrayList<>();
            toRpnPart1(rpn, items, 0);
            return rpn;
        }

        static int pushNextPart1(ArrayList<String> rpn, ArrayList<String> items,
                int pos) {
            String item = items.get(pos);
            if (isNumber(item)) {
                rpn.add(item);
                return pos + 1;
            } else if (item.equals("(")) {
                return toRpnPart1(rpn, items, pos + 1);
            } else {
                throw new RuntimeException(
                        "Unexpectd item " + item + " at:" + pos);
            }
        }

        static int toRpnPart1(ArrayList<String> rpn, ArrayList<String> items,
                int i) {
            while (i < items.size()) {
                String item = items.get(i);
                if (isNumber(item)) {
                    rpn.add(item);
                    i += 1;
                } else if (item.equals(")"))
                    return i + 1;
                else if (item.equals("(")) {
                    i = toRpnPart1(rpn, items, i + 1);
                } else if (item.equals("+") || item.equals("*")) {
                    i = pushNextPart1(rpn, items, i + 1);
                    rpn.add(item);
                }
            }
            return i;
        }

        static private boolean isNumber(String item) {
            return (Character.isDigit(item.charAt(0)));
        }
    }

    public static void main(String[] strings) {
        long part1Sum = 0;
        for (Expression expression : Util.parseAndCollectForDay(18,
                Expression::parsePart1)) {
            part1Sum += expression.eval();
        }
        System.out.println("Day 18 part 1: " + part1Sum);

        long part2Sum = Util.parseAndCollectForDay(18, Expression::parsePart2)
                .stream().mapToLong(Expression::eval).sum();
        System.out.println("Day 18 part 2: " + part2Sum);
    }

}
