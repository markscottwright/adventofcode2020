package adventofcode2020;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Day18 {
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
            return "Expression: " + items + "\n    :" + originalItems;
        }

        static Expression parse(String l) {
            ArrayList<String> items = new ArrayList<>();
            var pattern = Pattern.compile("[0-9]+|[()+*]");
            var matcher = pattern.matcher(l);
            while (matcher.find())
                items.add(matcher.group());
            return new Expression(toRpn(items), items);
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

        private long pop(ArrayList<Long> stack) {
            return stack.remove(stack.size() - 1);
        }

        static ArrayList<String> toRpn(ArrayList<String> items) {
            ArrayList<String> rpn = new ArrayList<>();
            toRpn(rpn, items, 0);
            return rpn;
        }

        static int pushNext(ArrayList<String> rpn, ArrayList<String> items,
                int pos) {
            String item = items.get(pos);
            if (isNumber(item)) {
                rpn.add(item);
                return pos + 1;
            } else if (item.equals("(")) {
                return toRpn(rpn, items, pos + 1);
            } else {
                throw new RuntimeException(
                        "Unexpectd item " + item + " at:" + pos);
            }
        }

        static int toRpn(ArrayList<String> rpn, ArrayList<String> items,
                int i) {
            while (i < items.size()) {
                String item = items.get(i);
                if (isNumber(item)) {
                    rpn.add(item);
                    i += 1;
                } else if (item.equals(")"))
                    return i + 1;
                else if (item.equals("(")) {
                    i = toRpn(rpn, items, i + 1);
                } else if (item.equals("+") || item.equals("*")) {
                    i = pushNext(rpn, items, i + 1);
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
                Expression::parse)) {
            part1Sum += expression.eval();
        }
        ;
        System.out.println("Day 18 part 1: " + part1Sum);
    }

}
