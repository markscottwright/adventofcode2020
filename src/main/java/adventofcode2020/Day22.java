package adventofcode2020;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Day22 {

    static int nextGameNum = 1;

    static class Combat {
        HashSet<List<ArrayList<Integer>>> previousRounds = new HashSet<>();

        private ArrayDeque<Integer> player1;
        private ArrayDeque<Integer> player2;
        private int round;
        private int game;
        Player winner = Player.player1;

        public Combat(ArrayDeque<Integer> player1, ArrayDeque<Integer> player2,
                int game) {
            this.player1 = player1;
            this.player2 = player2;
            this.round = 0;
            this.game = game;
        }

        static Combat parse(List<String> lines) {
            ArrayDeque<Integer> player1 = new ArrayDeque<>();
            ArrayDeque<Integer> player2 = new ArrayDeque<>();
            ArrayDeque<Integer> currentPlayer = player1;
            for (String l : lines) {
                if (l.startsWith("Player 1:"))
                    currentPlayer = player1;
                else if (l.startsWith("Player 2:"))
                    currentPlayer = player2;
                else if (!l.isBlank())
                    currentPlayer.addLast(Integer.parseInt(l));
            }

            return new Combat(player1, player2, 1);
        }

        boolean playRound() {
            round++;
            Integer player1Card = player1.pop();
            Integer player2Card = player2.pop();
            if (player1Card > player2Card) {
                player1.addLast(player1Card);
                player1.addLast(player2Card);
            } else {
                player2.addLast(player2Card);
                player2.addLast(player1Card);
            }
            return !(player1.isEmpty() || player2.isEmpty());
        }

        enum Player {
            player1, player2
        }

        void log(String s) {
            // System.out.println(s);
        }

        Player playRecursiveGame() {
            log("\n=== Game " + game + " ===\n");

            while (true) {
                round++;
                log("-- Round " + round + " (Game " + game + ") --");
                if (player1.isEmpty()) {
                    winner = Player.player2;
                    log("The winner of game " + game + " is player 2!");
                    return winner;
                } else if (player2.isEmpty()) {
                    winner = Player.player1;
                    log("The winner of game " + game + " is player 1!");
                    return winner;
                } else if (previousRounds
                        .contains(Arrays.asList(new ArrayList<Integer>(player1),
                                new ArrayList<Integer>(player2)))) {
                    log(player1 + ":" + player2 + " found in cache");
                    winner = Player.player1;
                    log("The winner of game " + game + " is player 1!");
                    return winner;
                }

                previousRounds
                        .add(Arrays.asList(new ArrayList<Integer>(player1),
                                new ArrayList<Integer>(player2)));
                log("Player 1's deck:" + player1);
                log("Player 2's deck:" + player2);
                Integer player1Card = player1.pop();
                Integer player2Card = player2.pop();
                log("Player 1 plays:" + player1Card);
                log("Player 2 plays:" + player2Card);

                if (player1.size() < player1Card
                        || player2.size() < player2Card) {
                    if (player1Card > player2Card) {
                        log(String.format("Player %d wins round %d of game %d!",
                                1, round, game));
                        player1.addLast(player1Card);
                        player1.addLast(player2Card);
                    } else {
                        log(String.format("Player %d wins round %d of game %d!",
                                2, round, game));
                        player2.addLast(player2Card);
                        player2.addLast(player1Card);
                    }
                } else {

                    ArrayDeque<Integer> player1SubDeck = lastNElements(player1,
                            player1Card);
                    ArrayDeque<Integer> player2SubDeck = lastNElements(player2,
                            player2Card);
                    log("Playing a sub-game to determine the winner...");
                    var winner = new Combat(player1SubDeck, player2SubDeck,
                            ++nextGameNum).playRecursiveGame();
                    if (winner == Player.player1) {
                        log(String.format("Player %d wins round %d of game %d!",
                                1, round, game));
                        player1.addLast(player1Card);
                        player1.addLast(player2Card);
                    } else {
                        log(String.format("Player %d wins round %d of game %d!",
                                2, round, game));
                        player2.addLast(player2Card);
                        player2.addLast(player1Card);
                    }
                }
            }
        }

        private static ArrayDeque<Integer> lastNElements(
                ArrayDeque<Integer> deque, int num) {
            assert deque.size() >= num;
            var out = new ArrayDeque<Integer>();
            Iterator<Integer> iterator = deque.iterator();
            while (num-- > 0) {
                out.addLast(iterator.next());
            }
            return out;
        }

        @Override
        public String toString() {
            return "Combat [round=" + round + ", player1=" + player1
                    + ", player2=" + player2 + "]";
        }

        long winningScore() {
            var winningPlayersCards = new ArrayList<>(
                    winner == Player.player1 ? player1 : player2);

            long score = 0;
            for (int i = 0; i < winningPlayersCards.size(); i++)
                score += (winningPlayersCards.size() - i)
                        * winningPlayersCards.get(i);
            return score;
        }

        public long play() {
            do {
            } while (playRound());
            winner = player1.isEmpty() ? Player.player2 : Player.player1;
            return winningScore();
        }
    }

    public static void main(String[] strings) {
        Combat game1 = Combat.parse(Util.inputLinesForDay(22));
        System.out.println("Day 22 part 1: " + game1.play());

        Combat game2 = Combat.parse(Util.inputLinesForDay(22));
        game2.playRecursiveGame();
        System.out.println("Day 22 part 2: " + game2.winningScore());
    }

}
