/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.sheridancollege.project;

/**
 *
 * @author win 11
 */
import java.util.Scanner;

/**
 * The main controller class for the Blackjack game.
 * It manages the game flow, player interactions, dealer actions, and game state.
 */
public class BlackjackGame extends Game{

    private Deck deck;
    private BlackjackPlayer player;
    private Dealer dealer;
    private Scanner input;

    /**
     * Constructor for the BlackjackGame.
     * It initializes the game components.
     * @param name The name of the game.
     */
    public BlackjackGame(String name) {
        super(name);
        input = new Scanner(System.in);
        deck = new Deck();
        dealer = new Dealer("Dealer");
    }

    /**
     * Starts the main game loop.
     * It handles player registration and then loops through rounds of Blackjack.
     */
    @Override
    public void play() {
        System.out.println("Welcome to " + getName() + "!");

        // Player Registration
        System.out.print("Enter your name: ");
        String playerName = input.nextLine();
        // Ensure name is not empty
        while (playerName.trim().isEmpty()) {
            System.out.print("Name cannot be empty. Please enter your name: ");
            playerName = input.nextLine();
        }

        // Initialize player with a starting balance, e.g., 100.0
        player = new BlackjackPlayer(playerName, 100.0);
        System.out.println("Welcome, " + player.getName() + "! You start with $" + player.getBalance());

        // Main Game Loop
        boolean playAgain = true;
        while (playAgain && player.getBalance() > 0) {
            playRound();
            if (player.getBalance() > 0) {
                playAgain = askToPlayAgain();
            } else {
                System.out.println("\nYou are out of chips! Game Over.");
            }
        }

        declareWinner();
        input.close();
    }
/**
     * Plays a single round of Blackjack.
     */
    private void playRound() {
        System.out.println("\n--- New Round ---");
        deck = new Deck(); // Use a fresh deck each round
        deck.shuffle();
        player.resetHand();
        dealer.resetHand();

        // 1. Place Bet
        placeBet();

        // 2. Deal Initial Cards
        player.getHand().addCard(deck.dealCard());
        dealer.getHand().addCard(deck.dealCard()); // Hole card
        player.getHand().addCard(deck.dealCard());
        dealer.getHand().addCard(deck.dealCard()); // Face-up card

        // Show initial state
        System.out.println("\nDealer's face-up card: " + dealer.getFaceUpCard());
        System.out.println("Your hand: " + player.getHand());

        // Check for natural Blackjack
        if (player.getHand().calculateTotal() == 21) {
            determineWinner(); // Will handle Blackjack payout
            return;
        }

        // 3. Player's Turn
        boolean playerBusted = playerTurn();
        if (playerBusted) {
            System.out.println("Bust! You went over 21.");
            determineWinner();
            return;
        }

        // 4. Dealer's Turn
        dealerTurn();

        // 5. Determine Winner
        determineWinner();
    }

    /**
     * Handles the player's turn. The player can choose to Hit or Stand.
     * @return true if the player busts, false otherwise.
     */
    private boolean playerTurn() {
        while (true) {
            System.out.print("Would you like to [H]it or [S]tand? ");
            String choice = input.nextLine().toUpperCase();

            if (choice.equals("H")) {
                BlackjackCard newCard = deck.dealCard();
                player.getHand().addCard(newCard);
                System.out.println("You drew: " + newCard);
                System.out.println("Your hand: " + player.getHand());

                if (player.getHand().calculateTotal() > 21) {
                    return true; // Player busted
                }
            } else if (choice.equals("S")) {
                System.out.println("You chose to Stand.");
                return false; // Player finished turn without busting
            } else {
                System.out.println("Invalid input. Please enter 'H' or 'S'.");
            }
        }
    }

    /**
     * Handles the dealer's turn. The dealer hits until their total is 17 or higher.
     */
    private void dealerTurn() {
        System.out.println("\n--- Dealer's Turn ---");
        // Show dealer's full hand (including the hole card)
        System.out.println("Dealer's hand: " + dealer.getHand());

        // Dealer hits on 16 or less, stands on 17 or more
        while (dealer.getHand().calculateTotal() < 17) {
            BlackjackCard newCard = deck.dealCard();
            dealer.getHand().addCard(newCard);
            System.out.println("Dealer hits and draws: " + newCard);
            System.out.println("Dealer's hand: " + dealer.getHand());
        }

        if (dealer.getHand().calculateTotal() > 21) {
            System.out.println("Dealer busts!");
        } else {
            System.out.println("Dealer stands with a total of " + dealer.getHand().calculateTotal());
        }
    }
    /**
     * Prompts the player to place a bet.
     */
    private void placeBet() {
        while (true) {
            System.out.print("You have $" + player.getBalance() + ". Enter your bet amount: ");
            try {
                double amount = Double.parseDouble(input.nextLine());
                player.placeBet(amount);
                System.out.println("Bet of $" + amount + " placed.");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Determines the winner of the round and updates the player's balance.
     */
    private void determineWinner() {
        System.out.println("\n--- Round Result ---");
        int playerTotal = player.getHand().calculateTotal();
        int dealerTotal = dealer.getHand().calculateTotal();
        double bet = player.getCurrentBet();

        // Check for Player Blackjack (natural 21 on first two cards)
        if (playerTotal == 21 && player.getHand().getSize() == 2) {
             // Check if dealer also has Blackjack for a push
             if (dealerTotal == 21 && dealer.getHand().getSize() == 2) {
                 System.out.println("Both have Blackjack! It's a Push. Bet returned.");
                 player.addWinnings(bet);
             } else {
                 System.out.println("Blackjack! You win 3:2 payout!");
                 player.addWinnings(bet * 2.5); // Bet returned + 1.5x winnings
             }
        }
        // Player Busted
        else if (playerTotal > 21) {
            System.out.println("You busted. Dealer Wins.");
            // No winnings added; bet is already deducted
        }
        // Dealer Busted
        else if (dealerTotal > 21) {
            System.out.println("Dealer busted. You Win!");
            player.addWinnings(bet * 2); // Bet returned + 1x winnings
        }
        // Player Score > Dealer Score
        else if (playerTotal > dealerTotal) {
            System.out.println("You win!");
            player.addWinnings(bet * 2); // Bet returned + 1x winnings
        }
        // Dealer Score > Player Score
        else if (dealerTotal > playerTotal) {
            System.out.println("Dealer wins.");
            // No winnings added; bet is already deducted
        }
        // Push (Tie)
        else {
            System.out.println("It's a Push. Bet returned.");
            player.addWinnings(bet);
        }

        System.out.println("Your new balance is $" + player.getBalance());
    }

    /**
     * Prompts the user to see if they want to play another round.
     * @return true to play again, false otherwise.
     */
    private boolean askToPlayAgain() {
        while (true) {
            System.out.print("\nDo you want to play another round? (Y/N): ");
            String choice = input.nextLine().toUpperCase();
            if (choice.equals("Y")) {
                return true;
            } else if (choice.equals("N")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'Y' or 'N'.");
            }
        }
    }

    /**
     * Declares the final winner at the end of the game session.
     */
    @Override
    public void declareWinner() {
        System.out.println("\n--- Game Over ---");
        System.out.println("Thanks for playing, " + player.getName() + "!");
        System.out.println("You finished with a balance of $" + player.getBalance());
    }

    /**
     * Main method to start the game.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame("Blackjack");
        game.play();
    }
}

