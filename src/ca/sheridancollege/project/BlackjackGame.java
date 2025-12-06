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
public class BlackjackGame extends Game {

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
        // deck and dealer will be initialized in setupRound()
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
        // Initialize dealer
        dealer = new Dealer("Dealer");

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
     * The turn structure is explicitly sequential: Player's full turn, then Dealer's full turn.
     */
    private void playRound() {
        System.out.println("\n--- New Round ---");
        setupRound();

        // 1. Place Bet
        placeBet();

        // 2. Deal Initial Cards
        dealInitialCards();

        // Show initial state
        System.out.println("\nDealer's face-up card: " + dealer.getFaceUpCard());
        System.out.println("Your hand: " + player.getHand());

        // Check for natural Blackjack immediately
        if (hasNaturalBlackjack(player.getHand())) {
            determineWinner(); // Will handle Blackjack payout
            return; // Round ends immediately
        }

        // 3. Player's Turn (The whole turn)
        System.out.println("\n--- Your Turn ---");
        boolean playerBusted = playPlayerTurn();

        // 4. Dealer's Turn (Only if player didn't bust)
        if (!playerBusted) {
            System.out.println("\n--- Dealer's Turn ---");
            playDealerTurn();
        } else {
             // If player busted, dealer doesn't play.
             // The determineWinner() method handles the "Bust! Dealer Wins" message.
        }

        // 5. Determine Final Winner
        determineWinner();
    }

    // --- Helper Methods for playRound ---

    /**
     * Prepares for a new round by creating a fresh deck and resetting hands.
     */
    private void setupRound() {
        deck = new Deck(); // Use a fresh deck each round
        deck.shuffle();
        player.resetHand();
        dealer.resetHand();
    }

    /**
     * Deals two cards to the player and two to the dealer.
     */
    private void dealInitialCards() {
        player.getHand().addCard(deck.dealCard());
        dealer.getHand().addCard(deck.dealCard()); // Hole card
        player.getHand().addCard(deck.dealCard());
        dealer.getHand().addCard(deck.dealCard()); // Face-up card
    }

    /**
     * Checks if a hand is a natural Blackjack (2 cards totaling 21).
     * @param hand The hand to check.
     * @return true if it's a natural Blackjack, false otherwise.
     */
    private boolean hasNaturalBlackjack(Hand hand) {
        return hand.calculateTotal() == 21 && hand.getSize() == 2;
    }

    /**
     * Handles the player's full turn. The player can choose to Hit or Stand repeatedly.
     * @return true if the player busts, false if they stand.
     */
    private boolean playPlayerTurn() {
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
     * Handles the dealer's full turn. The dealer hits until their total is 17 or higher.
     */
    private void playDealerTurn() {
        // Show dealer's full hand (including the hole card)
        System.out.println("Dealer's hand: " + dealer.getHand());

        // Dealer hits on 16 or less, stands on 17 or more
        while (dealer.getHand().calculateTotal() < 17) {
            BlackjackCard newCard = deck.dealCard();
            dealer.getHand().addCard(newCard);
            System.out.println("Dealer hits and draws: " + newCard);
            System.out.println("Dealer's hand: " + dealer.getHand());
        }
        // Dealer stands or busts. The exact result is handled in determineWinner().
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
        if (hasNaturalBlackjack(player.getHand())) {
             // Check if dealer also has Blackjack for a push
             if (hasNaturalBlackjack(dealer.getHand())) {
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