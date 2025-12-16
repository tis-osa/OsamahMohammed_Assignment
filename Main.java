import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    private static final int MAX_ROUND_VALUE = 6;
    private static final int MAX_DICE_ROLL = 20;

    static int roundsWon = 0; // This is for player(YOU)
    static int currentRound = 1; // Actual current round
    static int diceYOU = -1, diceOPPONENT = -1; // Current Dice Count
    static int prevDiceYOU = diceYOU, prevDiceOPPONENT = diceOPPONENT; // Previous Dice Count

    static boolean[][] standing = new boolean[][] {{false, false},{false, false}}; // Stand move array

    private static boolean running = true, chosen = false; // If game is running, and if the player has picked a choice

    private static void rollDice(int player) { // Rolls dice (YOU: 0 / OPPONENT: 1)
        if (player == 0) {
            prevDiceYOU = diceYOU;
            diceYOU = (new Random().nextInt(MAX_DICE_ROLL))+1;
        } else {
            prevDiceOPPONENT = diceOPPONENT;
            diceOPPONENT = (new Random().nextInt(MAX_DICE_ROLL))+1;
        }
    }

    private static boolean isDiceHigher(int dice1, int dice2) { // Checks if Dice1 is higher than Dice2
        if (dice1 > dice2)
            return true;
        return false;
    }

    private static boolean isDiceEqual(int dice1, int dice2) { // Checks if Dice1 is equal to Dice2
        return (dice1 == dice2);
    }

    private static void randomActionOpponent() { // Randomizes the opponent's next move (Draw: 1-2 OR Stand: 0)
        int random = new Random().nextInt(3);
        System.out.println(random);
        if (random==0)
            if (standing[1][1] == true)
                standing[1][0] = true;
            else
                rollDice(1);
        else {
            rollDice(1);
        }
            
    }

    private static void resetStat() { // Resets Stand values of both players
        for (int i = 0 ; i < standing.length ; i++) {
            if (standing[i][0] == true) {
                standing[i][0] = false;
                standing[i][1] = false;
            } else {
                standing[i][1] = true;
            }
        }
    }

    private static void waitTime(long seconds) { // Wait time in SECONDS
        try {
            Thread.sleep(seconds*1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello, and welcome to my \"STAND AT IT!\" game!");
        System.out.println("Rules are simple;\nYou and your oppononent have two options: Draw, or Stand.\n >> Draw allows the player to throw a dice of "+MAX_DICE_ROLL+".\n >> Stand would allow the player to not play this round IF they have drawn the round before.\n\nThe game plays in rounds of 5, first person to win 3 rounds wins the game.\n\nTo play, both players must draw on the first round, the highest number wins. However, on any other round the players could stand IF they had ALREADY drawn the round before.\n\nIf one player stands, the other has to draw to win over the standing player's previous draw.\nIf both players stand, each player must draw to win over their own previous draw.\n\nThat's it. Enjoy! And don't forget that you can STAND AT IT.\n==========================");
    
        String pick;
        while (running) { // Choice is given here
            if (currentRound != 1) { // Not Round 1 due to standing in round 1 always being false
                resetStat(); // Resetting stand stats
            }

            System.out.println("What do you wish to do?\n>> Draw\n>> Stand\n\nplayer(YOU)'s Previous Draw: "+diceYOU+"\nOpponent's Previous Draw: "+diceOPPONENT+"\n\nCurrent Round: "+currentRound+"\nRounds Won: "+roundsWon+"\n==========================");
            System.out.print(">> ");
            pick = sc.next();
            pick = pick.toLowerCase();
            
            switch (pick) {
                case "draw":
                    if (currentRound == 1) { // If it's the first round, both players MUST draw
                        rollDice(0);
                        rollDice(1);
                    } else { // Any other round.
                        rollDice(0);
                        randomActionOpponent();
                    }
                    chosen = true;
                    break;
                case "stand":
                    if (currentRound == 1) // CANNOT STAND first round
                        System.out.println("First round, both players must draw.");
                    else { 
                        if (standing[0][1] == true) { // Checking if player(YOU) can stand
                            standing[0][0] = true;
                            randomActionOpponent();
                            chosen = true;
                        } else // No ability
                            System.out.println("Can't stand, must draw.");
                    }
                    break;
                default: // If another input
                    System.out.println("Wrong input! Try again.");
                    break;
            }

            if (chosen == true) { // Where everything happens, kinda unorganized and very unnecessary, still works though.
                chosen = false; // Reset chosen
                if (standing[0][0] == true) { // If player(YOU) stands
                    rollDice(0);
                    if (standing[1][0] == true) { // If OPPONENT stands
                        if (isDiceHigher(diceYOU, prevDiceYOU) == true) { // if player(YOU) can pass YOUR draw
                            if (isDiceHigher(diceOPPONENT, prevDiceOPPONENT) == true) { // if OPPONENT can pass THEIR draw
                                if (isDiceHigher(diceYOU, diceOPPONENT) == true) { // YOUR CURRENT DRAW VS OPPONENT CURRENT DRAW
                                    System.out.println("You both stood. And you both passed your draw. But you won! (YOU:"+diceYOU+" > OP:"+diceOPPONENT+")");
                                    roundsWon++;
                                } else {
                                    if (isDiceEqual(diceYOU, diceOPPONENT) == true)
                                        System.out.println("You both stood. And you both passed your draw. But, no one won! (YOU:"+diceYOU+" == OP:"+diceOPPONENT+")");
                                    else
                                        System.out.println("You both stood. And you both passed your draw. But you lost! (YOU:"+diceYOU+" < OP:"+diceOPPONENT+")");
                                }

                            } else { // OPPONENT failed their draw
                                System.out.println("You both stood. But, he failed his draw! You won! (OP:"+diceOPPONENT+" < OP:"+prevDiceOPPONENT+")");
                                roundsWon++;
                            }
                        } else // player(YOU) failed draw
                            System.out.println("You stood. And you failed the draw! (YOU:"+diceYOU+" < YOU:"+prevDiceYOU+")");
                    } else { // if OPPONENT draws
                        if (isDiceHigher(diceOPPONENT, prevDiceYOU) == true) 
                            System.out.println("You stood, they didn't. And you lost! (OP:"+diceOPPONENT+" > YOU:"+prevDiceYOU+")");
                        else {
                            if (isDiceEqual(prevDiceYOU, diceOPPONENT) == true)
                                System.out.println("You stood, they didn't. And no one won! (OP:"+diceOPPONENT+" == YOU:"+prevDiceYOU+")");
                            else {
                                System.out.println("You stood, they didn't. And you won! (OP:"+diceOPPONENT+" < YOU:"+prevDiceYOU+")");
                                roundsWon++;
                            }
                        }
                            
                    }
                } else { // If player(YOU) draw
                    if (isDiceHigher(diceYOU, diceOPPONENT) == true) { // player(YOU) vs OPPONENT
                        System.out.println("You both drew. And you won! (YOU:"+diceYOU+" > OP:"+diceOPPONENT+")");
                        roundsWon++;
                    } else {
                        if (isDiceEqual(diceYOU, diceOPPONENT) == true) // CHECKING IF EQUAL
                            System.out.println("You both drew. And no one won! (YOU:"+diceYOU+" == OP:"+diceOPPONENT+")");
                        else // player(YOU) LOST
                            System.out.println("You both drew. And you lost! (YOU:"+diceYOU+" < OP:"+diceOPPONENT+")");
                    }
                }

                currentRound++;
            }

            System.out.println("==========================");
            waitTime(4);
            System.out.println("\n\n\n\n\n\n\n\n\n\n");

            if (currentRound >= MAX_ROUND_VALUE) { // If the current round reaches the final round, game is over.
                running = false;
            }
        }
        if (roundsWon >= 3) { // Player wins if won 3 or more rounds
            System.out.println("Congratulations! You have won the game! You won "+roundsWon+" rounds!");
        } else { // player loses if under 3 wins
            System.out.println("You failed! Total Rounds played: "+(currentRound-1)+". Rounds won: "+roundsWon);
        }

        System.out.println("\n\nThanks for playing! I hope you enjoyed.");
    }
}