/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.sheridancollege.project;

/**
 *
 * @author win 11
 */
public class BlackjackPlayer extends Player {

    private double balance;
    private double currentBet;
    private Hand hand;

    /**
     * Constructor for a BlackjackPlayer.
     * @param name The player's name.
     * @param startingBalance The player's initial bankroll.
     */
    public BlackjackPlayer(String name, double startingBalance) {
        super(name);
        this.balance = startingBalance;
        this.hand = new Hand();
        this.currentBet = 0;
    }

    public double getBalance() {
        return balance;
    }

    public Hand getHand() {
        return hand;
    }

    /**
     * Places a bet for the current round.
     * The bet amount is deducted from the player's balance.
     * @param amount The amount to bet.
     * @throws IllegalArgumentException if the bet amount is invalid or exceeds the balance.
     */
    public void placeBet(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Bet amount must be greater than 0.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance for this bet.");
        }
        this.currentBet = amount;
        this.balance -= amount;
    }

    /**
     * Returns the player's current bet amount.
     * @return The current bet.
     */
    public double getCurrentBet() {
        return currentBet;
    }

    /**
     * Adds winnings to the player's balance.
     * @param amount The amount won.
     */
    public void addWinnings(double amount) {
        this.balance += amount;
        this.currentBet = 0; // Reset bet for the next round
    }

    /**
     * Resets the player's hand for a new round.
     * It clears the old hand and creates a new empty one.
     */
    public void resetHand() {
        this.hand = new Hand();
    }

    /**
     * Implementation of the abstract play method.
     * This is a placeholder as the main game logic will handle the player's
     * interactive Hit/Stand turn in the Game class.
     */
    @Override
    public void play() {
        // Logic for player turn will be handled in the BlackjackGame class
    }
}
